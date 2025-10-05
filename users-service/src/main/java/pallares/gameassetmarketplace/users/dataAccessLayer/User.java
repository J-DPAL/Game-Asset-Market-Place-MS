package pallares.gameassetmarketplace.users.dataAccessLayer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //private identifier

    @Embedded
    private UserIdentifier userIdentifier; //public identifier

    private String firstName;
    private String lastName;
    private String username;
    private EmailAddress emailAddress;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Embedded
    @Column(name = "phone_number")
    private PhoneNumber phoneNumber;

    private LocalDate accountCreationDate;

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull String username, @NotNull EmailAddress emailAddress, @NotNull UserRoleEnum userRoleEnum, @NotNull LocalDate accountCreationDate,
                @NotNull PhoneNumber phoneNumber) {
        this.userIdentifier = new UserIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.role = userRoleEnum;
        this.accountCreationDate = accountCreationDate;
        this.phoneNumber = phoneNumber;
    }
}
