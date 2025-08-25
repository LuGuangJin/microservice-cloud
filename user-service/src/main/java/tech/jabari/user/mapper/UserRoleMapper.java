package tech.jabari.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.jabari.user.entity.UserRole;

import java.util.List;


@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


    @Select("SELECT r.code FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{x}")
    List<String> selectRolesByUserId(@Param("x") Long userId);

}
