package tech.jabari.api.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.common.result.Result;

@Component
@Slf4j
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public Result<UserDTO> getUser(Long id) {
        log.error("......用户服务-查询用户[{}]功能暂不可用！",id);
        return Result.fail("用户服务-查询用户功能暂不可用！");
    }
}
