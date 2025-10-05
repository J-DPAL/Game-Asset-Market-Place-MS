package pallares.gameassetmarketplace.users.mappingLayer;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.hateoas.Link;
import pallares.gameassetmarketplace.users.dataAccessLayer.User;
import pallares.gameassetmarketplace.users.presentationLayer.UserController;
import pallares.gameassetmarketplace.users.presentationLayer.UserResponseModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(expression = "java(user.getUserIdentifier().getUserId())", target = "userId")
    @Mapping(expression = "java(user.getEmailAddress().getEmail())", target = "emailAddress")
    @Mapping(expression = "java(user.getUsername())", target = "username")
    @Mapping(expression = "java(user.getRole())", target = "userRoleEnum")
    UserResponseModel entityToResponseModel(User user);

    List<UserResponseModel> entityListToResponseModelList(List<User> users);

    @AfterMapping
    default void addLinks(@MappingTarget UserResponseModel userResponseModel, User user) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .getUserByUserId(userResponseModel.getUserId()))
                .withSelfRel();
        userResponseModel.add(selfLink);

        Link allUsersLink = linkTo(methodOn(UserController.class)
                .getAllUsers())
                .withRel("users");
        userResponseModel.add(allUsersLink);
    }
}
