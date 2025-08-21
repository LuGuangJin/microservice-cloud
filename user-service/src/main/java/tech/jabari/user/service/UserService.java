package tech.jabari.user.service;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.jabari.api.dto.UserDTO;
import tech.jabari.user.entity.User;
import tech.jabari.user.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @SentinelResource(
            value = "getUserInfo", // 资源名
            fallback = "getUserFallback",  // 降级兜底方法
            exceptionsToIgnore = {IllegalArgumentException.class}  // 忽略特定异常
    )
    public UserDTO getUserById(Long id) {
        // 模拟慢调用（触发熔断）
        if (id == 999) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        User u = userMapper.selectById(id);
        if (u == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO(u.getId(),u.getUsername(),u.getPhone());
        return userDTO;
    }

    // 降级兜底方法（参数需与原方法一致，可加Throwable）
    public UserDTO getUserFallback(Long id, Throwable t) {
        return UserDTO.defaultUser(); // 返回默认用户
    }

}
