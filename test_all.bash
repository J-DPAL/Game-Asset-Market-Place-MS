#!/usr/bin/env bash

: ${HOST=localhost}
: ${PORT=8080}

set -euo pipefail
echo "HOST=$HOST"
echo "PORT=$PORT"

function assertCurl() {
  local expected=$1; shift
  local cmd="$* -w \"%{http_code}\""
  echo "    → running: $cmd"
  local out; out=$(eval "$cmd")
  local code="${out: -3}"
  RESPONSE="${out:0:${#out}-3}"

  if [ "$code" = "$expected" ]; then
    echo "      [OK] HTTP $code"
  else
    echo "      [FAIL] Expected HTTP $expected but got $code"
    echo "      CMD:   $cmd"
    echo "      RESP:  $RESPONSE"
    exit 1
  fi
}

function waitForGateway() {
  echo -n "Waiting for API‑Gateway health... "
  for i in {1..30}; do
    if curl -fs http://$HOST:$PORT/actuator/health | grep -q '"status":"UP"'; then
      echo "OK"
      return
    fi
    sleep 2
    echo -n "."
  done
  echo "FAIL"
  exit 1
}

function setupTestdata() {
  echo
  echo "==> 1. Users Service"
  userJson='{"username":"testuser","emailAddress":"testuser@example.com","phoneNumber":"1234567890","role":"BUYER"}'
  echo "  POST /api/v1/users"
  userId=$(curl -s -X POST http://$HOST:$PORT/api/v1/users \
    -H "Content-Type:application/json" --data-raw "$userJson" | jq -r .userId)
  echo "    created userId=$userId"
  echo "  GET  /api/v1/users"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/users -s
  echo "  GET  /api/v1/users/$userId"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/users/"$userId" -s

  echo
  echo "==> 2. Assets Service"
  assetJson='{"name":"Test Asset","description":"Sample","category":"MODEL","price":99.99,"fileUrl":"http://files/1.obj","thumbnailUrl":"http://files/1.png","licenseType":"ROYALTY_FREE"}'
  echo "  POST /api/v1/assets"
  assetId=$(curl -s -X POST http://$HOST:$PORT/api/v1/assets \
    -H "Content-Type:application/json" --data-raw "$assetJson" | jq -r .assetId)
  echo "    created assetId=$assetId"
  echo "  GET  /api/v1/assets"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/assets -s
  echo "  GET  /api/v1/assets/$assetId"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/assets/"$assetId" -s

  echo
  echo "==> 3. Payments Service"
  paymentJson='{"price":99.99,"currency":"US","paymentType":"CREDIT_CARD","transactionStatus":"PENDING"}'
  echo "  POST /api/v1/payments"
  paymentId=$(curl -s -X POST http://$HOST:$PORT/api/v1/payments \
    -H "Content-Type:application/json" --data-raw "$paymentJson" | jq -r .paymentId)
  echo "    created paymentId=$paymentId"
  echo "  GET  /api/v1/payments"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/payments -s
  echo "  GET  /api/v1/payments/$paymentId"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/payments/"$paymentId" -s

  echo
  echo "==> 4. Asset‑Transactions Aggregator"
  echo "  POST /api/v1/users/$userId/asset_transactions"
  txJson=$(jq -c -n \
    --arg a "$assetId" \
    --arg p "$paymentId" \
    '{assetId:$a,paymentId:$p,status:"PAID",type:"SINGLE_ASSET",discount:{percentage:0,amountOff:0,reason:"none"}}')
  txId=$(curl -s -X POST http://$HOST:$PORT/api/v1/users/"$userId"/asset_transactions \
    -H "Content-Type:application/json" --data-raw "$txJson" | jq -r .assetTransactionId)
  echo "    created txId=$txId"

  echo "  GET  /api/v1/users/$userId/asset_transactions"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/users/"$userId"/asset_transactions -s
  echo "  GET  /api/v1/users/$userId/asset_transactions/$txId"
  assertCurl 200 curl http://$HOST:$PORT/api/v1/users/"$userId"/asset_transactions/"$txId" -s

  echo "  PUT  /api/v1/users/$userId/asset_transactions/$txId"
  updJson=$(jq -c --arg r "updated" '.discount.reason = $r' <<<"$txJson")
  assertCurl 200 curl -X PUT http://$HOST:$PORT/api/v1/users/"$userId"/asset_transactions/"$txId" \
                   -H "Content-Type:application/json" --data-raw "'$updJson'" -s

  echo "  DELETE /api/v1/users/$userId/asset_transactions/$txId"
  assertCurl 204 curl -X DELETE http://$HOST:$PORT/api/v1/users/"$userId"/asset_transactions/"$txId" -s
}

# bring up
if [[ $* == *start* ]]; then
  echo "Bringing up stack..."
  docker-compose down
  docker-compose up -d --build
fi

waitForGateway
setupTestdata

# tear down
if [[ $* == *stop* ]]; then
  echo "Tearing down stack..."
  docker-compose down
fi

echo
echo "All tests passed!"
