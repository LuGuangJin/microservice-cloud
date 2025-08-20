package tech.jabari.api.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private OrderDTO order;

    private UserDTO user;

    private ProductDTO product;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(OrderDTO order, UserDTO user, ProductDTO product) {
        this.order = order;
        this.user = user;
        this.product = product;
    }





}
