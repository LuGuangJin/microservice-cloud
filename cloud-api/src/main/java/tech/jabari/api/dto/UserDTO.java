package tech.jabari.api.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String phone;

    public UserDTO(Long id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    public static UserDTO defaultUser() {
        return new UserDTO(0L, "默认用户", "13382507298");
    }


}
