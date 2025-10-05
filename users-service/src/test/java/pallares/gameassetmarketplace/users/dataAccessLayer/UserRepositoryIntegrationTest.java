package pallares.gameassetmarketplace.users.dataAccessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void whenUsersExist_thenReturnAllUsers() {
        // Arrange
        User user1 = new User("Alice", "Wonderland", "AW",
                new EmailAddress("alice@example.com"), UserRoleEnum.ADMIN,
                LocalDate.of(2020, 1, 1), new PhoneNumber("123-456-7890"));
        User user2 = new User("Bob", "Builder", "BB",
                new EmailAddress("bob@example.com"), UserRoleEnum.BUYER,
                LocalDate.of(2021, 5, 12), new PhoneNumber("321-654-0987"));

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertNotNull(users);
        assertThat(users, hasSize(2));
    }

    @Test
    public void whenUserExists_thenFindByUserIdReturnsUser() {
        // Arrange
        User user = new User("Charlie", "Brown", "CB",
                new EmailAddress("charlie@peanuts.com"), UserRoleEnum.ADMIN,
                LocalDate.of(2019, 2, 14), new PhoneNumber("555-123-4567"));
        User savedUser = userRepository.save(user);
        String userId = savedUser.getUserIdentifier().getUserId();

        // Act
        User foundUser = userRepository.findByUserIdentifier_UserId(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(savedUser.getFirstName(), foundUser.getFirstName());
        assertEquals(userId, foundUser.getUserIdentifier().getUserId());
    }

    @Test
    public void whenUserIdNotFound_thenReturnNull() {
        // Act
        User foundUser = userRepository.findByUserIdentifier_UserId("non-existent-id");

        // Assert
        assertNull(foundUser);
    }

    @Test
    public void whenValidUser_thenCreateUser() {
        // Arrange
        User user = new User("Diana", "Prince", "DP",
                new EmailAddress("diana@amazon.com"), UserRoleEnum.SELLER,
                LocalDate.of(2022, 11, 1), new PhoneNumber("999-888-7777"));

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getUserIdentifier());
        assertNotNull(savedUser.getUserIdentifier().getUserId());
        assertEquals("Diana", savedUser.getFirstName());
    }

    @Test
    public void whenUserIsUpdated_thenChangesPersist() {
        // Arrange
        User user = new User("Eve", "Polastri", "EP",
                new EmailAddress("eve@mi6.gov"), UserRoleEnum.BUYER,
                LocalDate.of(2018, 6, 5), new PhoneNumber("000-111-2222"));
        User savedUser = userRepository.save(user);

        // Act
        savedUser.setRole(UserRoleEnum.ADMIN);
        savedUser.setFirstName("Evelyn");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        User foundUser = userRepository.findByUserIdentifier_UserId(updatedUser.getUserIdentifier().getUserId());
        assertEquals(UserRoleEnum.ADMIN, foundUser.getRole());
        assertEquals("Evelyn", foundUser.getFirstName());
    }

    @Test
    public void whenUserIsDeleted_thenNoLongerExists() {
        // Arrange
        User user = new User("Frank", "Castle", "FC",
                new EmailAddress("frank@punisher.com"), UserRoleEnum.SELLER,
                LocalDate.of(2015, 3, 3), new PhoneNumber("101-202-3030"));
        User savedUser = userRepository.save(user);
        String userId = savedUser.getUserIdentifier().getUserId();

        // Act
        userRepository.delete(savedUser);

        // Assert
        assertNull(userRepository.findByUserIdentifier_UserId(userId));
        assertEquals(0, userRepository.count());
    }
}
