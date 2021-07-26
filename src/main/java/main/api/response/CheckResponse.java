package main.api.response;

import lombok.Data;
import main.model.dto.UserDTO;

@Data
public class CheckResponse {

    private boolean result;

    private UserDTO user;
}
