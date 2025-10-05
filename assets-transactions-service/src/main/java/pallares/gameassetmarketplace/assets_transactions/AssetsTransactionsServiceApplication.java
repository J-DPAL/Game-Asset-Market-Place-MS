package pallares.gameassetmarketplace.assets_transactions;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@EnableFeignClients(basePackages = "pallares.gameassetmarketplace.assets_transactions.domainClientLayer")
public class AssetsTransactionsServiceApplication {
	@Bean
	RestTemplate restTemplate() { RestTemplate restTemplate = new RestTemplate();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory); return restTemplate; }
	public static void main(String[] args) {
		SpringApplication.run(AssetsTransactionsServiceApplication.class, args);
	}

}
