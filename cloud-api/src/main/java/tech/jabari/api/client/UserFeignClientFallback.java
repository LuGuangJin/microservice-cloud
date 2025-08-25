package tech.jabari.api.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.jabari.api.dto.AuthUserDTO;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;

import java.util.List;

@Component
@Slf4j
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public Result<UserDTO> getUser(Long id) {
        log.error("......用户服务-查询用户[{}]功能暂不可用！",id);
        return Result.fail("用户服务-查询用户功能暂不可用！");
    }

    @Override
    public AuthUserDTO getUserByUsername(String username) {
        log.error("......用户服务-根据用户名【{}】查询用户功能暂不可用！",username);
        return null;
    }

    @Override
    public List<String> selectRolesByUserId(Long id) {
        log.error("......用户服务-根据用户ID[{}]查询用户角色功能暂不可用！",id);
        return null;
    }
}
