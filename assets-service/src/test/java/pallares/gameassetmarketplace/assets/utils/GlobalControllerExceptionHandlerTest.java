package pallares.gameassetmarketplace.assets.utils;

import org.junit.jupiter.api.Test;
import pallares.gameassetmarketplace.assets.utils.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class GlobalControllerExceptionHandlerTest {

    @Test
    void testDuplicateVinExceptionConstructors() {
        assertNotNull(new DuplicateVinException());
        assertNotNull(new DuplicateVinException("Duplicate VIN"));
        assertNotNull(new DuplicateVinException(new RuntimeException("Cause")));
        assertNotNull(new DuplicateVinException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidInputExceptionConstructors() {
        assertNotNull(new InvalidInputException());
        assertNotNull(new InvalidInputException("Invalid input"));
        assertNotNull(new InvalidInputException(new RuntimeException("Cause")));
        assertNotNull(new InvalidInputException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testNotFoundExceptionConstructors() {
        assertNotNull(new NotFoundException());
        assertNotNull(new NotFoundException("Not found"));
        assertNotNull(new NotFoundException(new RuntimeException("Cause")));
        assertNotNull(new NotFoundException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testDuplicateAssetNameExceptionConstructors() {
        assertNotNull(new DuplicateAssetNameException());
        assertNotNull(new DuplicateAssetNameException("Duplicate name"));
        assertNotNull(new DuplicateAssetNameException(new RuntimeException("Cause")));
        assertNotNull(new DuplicateAssetNameException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidAssetPriceExceptionConstructors() {
        assertNotNull(new InvalidAssetPriceException());
        assertNotNull(new InvalidAssetPriceException("Invalid price"));
        assertNotNull(new InvalidAssetPriceException(new RuntimeException("Cause")));
        assertNotNull(new InvalidAssetPriceException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testAssetFileMissingExceptionConstructors() {
        assertNotNull(new AssetFileMissingException());
        assertNotNull(new AssetFileMissingException("File missing"));
        assertNotNull(new AssetFileMissingException(new RuntimeException("Cause")));
        assertNotNull(new AssetFileMissingException("Message", new RuntimeException("Cause")));
    }
}
