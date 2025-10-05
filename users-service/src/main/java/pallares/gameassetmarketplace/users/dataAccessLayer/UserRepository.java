package pallares.gameassetmarketplace.users.dataAccessLayer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByRole(UserRoleEnum userRoleEnum);

    User findByUserIdentifier_UserId(String userId);

    User findByUserIdentifier_UserIdAndRole(String userId, UserRoleEnum userRoleEnum);
}