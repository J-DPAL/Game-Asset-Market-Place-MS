package pallares.gameassetmarketplace.users.mappingLayer;

import org.mapstruct.Named;
import pallares.gameassetmarketplace.users.dataAccessLayer.EmailAddress;
import pallares.gameassetmarketplace.users.dataAccessLayer.User;
import pallares.gameassetmarketplace.users.dataAccessLayer.UserIdentifier;
import pallares.gameassetmarketplace.users.presentationLayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "emailAddress", expression = "java(new EmailAddress(userRequestModel.getEmailAddress()))"),
        @Mapping(target = "username", expression = "java(user.getUsername())"),
        @Mapping(target = "role", expression = "java(user.getRole())")
    })
    User requestModelToEntity(UserRequestModel userRequestModel, UserIdentifier userIdentifier,
                              EmailAddress emailAddress);
}
