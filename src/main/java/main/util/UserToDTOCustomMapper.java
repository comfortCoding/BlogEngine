package main.util;

import main.model.User;
import main.model.dto.UserDTO;
import org.mapstruct.*;

@Mapper
public interface UserToDTOCustomMapper {

    default UserDTO userToDTOCustomMapper(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPhoto(user.getPhoto());
        userDTO.setEmail(user.getEmail());
        userDTO.setModeration(user.isModerator());
        userDTO.setSettings(user.isModerator());

        return userDTO;
    }
}
