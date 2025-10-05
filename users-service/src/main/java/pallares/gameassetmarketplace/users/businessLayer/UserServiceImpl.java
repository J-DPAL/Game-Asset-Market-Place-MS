package pallares.gameassetmarketplace.users.businessLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.users.dataAccessLayer.*;
import pallares.gameassetmarketplace.users.mappingLayer.UserRequestMapper;
import pallares.gameassetmarketplace.users.mappingLayer.UserResponseMapper;
import pallares.gameassetmarketplace.users.presentationLayer.UserRequestModel;
import pallares.gameassetmarketplace.users.presentationLayer.UserResponseModel;
import pallares.gameassetmarketplace.users.utils.exceptions.*;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;

    public UserServiceImpl(UserRepository userRepository, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @Override
    public List<UserResponseModel> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userResponseMapper.entityListToResponseModelList(users);
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        // Validate email format
        if (!userRequestModel.getEmailAddress().contains("@")) {
            throw new InvalidEmailException("Invalid email format: " + userRequestModel.getEmailAddress());
        }

        // Check for duplicate email
        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmailAddress().getEmail().equalsIgnoreCase(userRequestModel.getEmailAddress()));
        if (emailExists) {
            throw new DuplicateEmailAddressException("Email already in use: " + userRequestModel.getEmailAddress());
        }

        // Check for duplicate username
        boolean usernameTaken = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(userRequestModel.getUsername()));
        if (usernameTaken) {
            throw new UsernameAlreadyTakenException("Username already taken: " + userRequestModel.getUsername());
        }

        // Check for valid phone number (basic length/format rule)
        if (userRequestModel.getPhoneNumber() == null ||
                userRequestModel.getPhoneNumber().getNumber().length() < 7) {
            throw new InvalidPhoneNumberException("Invalid phone number: " + userRequestModel.getPhoneNumber());
        }

        // Proceed to map and save user
        EmailAddress email = new EmailAddress(userRequestModel.getEmailAddress());
        User user = userRequestMapper.requestModelToEntity(userRequestModel, new UserIdentifier(), email);

        user.setEmailAddress(email);
        user.setUsername(userRequestModel.getUsername());
        user.setRole(userRequestModel.getUserRoleEnum());

        return userResponseMapper.entityToResponseModel(userRepository.save(user));
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel userRequestModel, String userId) {
        User existingUser = userRepository.findByUserIdentifier_UserId(userId);
        if (existingUser == null) {
            throw new NotFoundException("Provided userIdentifier not found: " + userId);
        }

        // Validate email format
        if (!userRequestModel.getEmailAddress().contains("@")) {
            throw new InvalidEmailException("Invalid email format: " + userRequestModel.getEmailAddress());
        }

        // Check if the email belongs to another user
        boolean emailUsedByAnother = userRepository.findAll().stream()
                .anyMatch(u -> !u.getUserIdentifier().getUserId().equals(userId) &&
                        u.getEmailAddress().getEmail().equalsIgnoreCase(userRequestModel.getEmailAddress()));
        if (emailUsedByAnother) {
            throw new DuplicateEmailAddressException("Email already in use: " + userRequestModel.getEmailAddress());
        }

        // Check for username conflict
        boolean usernameTakenByAnother = userRepository.findAll().stream()
                .anyMatch(u -> !u.getUserIdentifier().getUserId().equals(userId) &&
                        u.getUsername().equalsIgnoreCase(userRequestModel.getUsername()));
        if (usernameTakenByAnother) {
            throw new UsernameAlreadyTakenException("Username already taken: " + userRequestModel.getUsername());
        }

        // Check for valid phone number
        if (userRequestModel.getPhoneNumber() == null ||
                userRequestModel.getPhoneNumber().getNumber().length() < 12) {
            throw new InvalidPhoneNumberException("Invalid phone number: " + userRequestModel.getPhoneNumber() + ". Must be 10 digits and 2 dashes. (111-111-1111)");
        }

        // Map and save updated user
        EmailAddress email = new EmailAddress(userRequestModel.getEmailAddress());
        User updatedUser = userRequestMapper.requestModelToEntity(userRequestModel, existingUser.getUserIdentifier(), email);
        updatedUser.setId(existingUser.getId());
        updatedUser.setUsername(userRequestModel.getUsername());
        updatedUser.setRole(userRequestModel.getUserRoleEnum());

        return userResponseMapper.entityToResponseModel(userRepository.save(updatedUser));
    }

    @Override
    public void removeUser(String userId) {
        User existingUser = userRepository.findByUserIdentifier_UserId(userId);
        if (existingUser == null) {
            throw new NotFoundException("Provided userIdentifier not found: " + userId);
        }
        userRepository.delete(existingUser);
    }

    @Override
    public UserResponseModel getUserByUserId(String userId) {
        User user = userRepository.findByUserIdentifier_UserId(userId);
        if (user == null) {
            throw new NotFoundException("Provided userIdentifier not found: " + userId);
        }
        return userResponseMapper.entityToResponseModel(user);
    }
}
