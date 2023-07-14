package com.example.jpashop.repository.order.simplequery;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, OrderStatus orderStatus, Address address, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.name = name;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderDate = orderDate;
    }
}
