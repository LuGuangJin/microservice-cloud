package tech.jabari.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import tech.jabari.user.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
