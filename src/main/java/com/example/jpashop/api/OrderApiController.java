package com.example.jpashop.api;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import com.example.jpashop.repository.order.query.OrderFlatDto;
import com.example.jpashop.repository.order.query.OrderQueryDto;
import com.example.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
//            order.getMember().getName();
//            order.getDelivery().getAddress();
//
//            List<OrderItem> orderItems = order.getOrderItems();
//            orderItems.stream().forEach(o->o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAllByString((new OrderSearch())).stream().map(OrderDto::new).collect(toList());
    }
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders =  orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref= " + order + " id=" + order.getId());
        }
        List<OrderDto> result = orders.stream()
                .map(o->new OrderDto(o))
                .collect(toList());
        return result;
    }
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders =  orderRepository.findAllWithMemberDelivery(offset, limit);

//왜 이 코드때문에 페치조인이 안나갔지??????
//  for (Order order : orders) {
//      System.out.println("order ref= " + order + " id=" + order.getId());
//  }

        List<OrderDto> result = orders.stream()
                .map(o->new OrderDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        return orderQueryRepository.findAllByDto_flat();
    }

    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(o->o.getItem().getName());
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
