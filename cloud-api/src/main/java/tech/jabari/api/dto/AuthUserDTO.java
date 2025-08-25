package tech.jabari.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AuthUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer status;

    // 角色信息
    private List<String> roles;
}



