package pallares.gameassetmarketplace.assets.dataAccessLayer;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="assets")
@Data
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //private identifier

    @Embedded
    private AssetIdentifier assetIdentifier; //public identifier

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    private BigDecimal price;
    private String fileUrl;
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    @Column(name = "created_date")
    private LocalDate createdDate;

    private LocalDate updatedDate;

    public Asset(@NotNull String name, @NotNull String description, @NotNull AssetType assetType, @NotNull BigDecimal price, @NotNull String fileUrl, @NotNull String thumbnailUrl, @NotNull LicenseType licenseType, @NotNull LocalDate createdDate, @NotNull LocalDate updatedDate) {
        this.assetIdentifier = new AssetIdentifier();
        this.name = name;
        this.description = description;
        this.assetType = assetType;
        this.price = price;
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.licenseType = licenseType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Asset() {
    }
}

