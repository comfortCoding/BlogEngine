package main.util;

import main.model.dto.UserDTO;
import main.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserToDTOMapper {

    private final ModelMapper modelMapper;


    public UserToDTOMapper() {

        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(User.class, UserDTO.class);
    }

    public UserDTO convertToDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }
}
