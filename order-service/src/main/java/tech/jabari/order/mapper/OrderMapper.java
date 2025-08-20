package tech.jabari.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import tech.jabari.order.entity.Order;


@Mapper
public interface OrderMapper extends BaseMapper<Order> {


}
