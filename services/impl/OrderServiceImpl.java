package com.Kilari.services.impl;

import com.Kilari.domain.OrderStatus;
import com.Kilari.domain.PaymentStatus;
import com.Kilari.modal.*;
import com.Kilari.repository.AddressRepository;
import com.Kilari.repository.OrderItemRepository;
import com.Kilari.repository.OrderRepository;
import com.Kilari.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
       if(!user.getAddresses().contains(shippingAddress)){
           user.getAddresses().add(shippingAddress);
       }
       Address address = addressRepository.save(shippingAddress);
        Map<Long,List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                .getSeller().getId()));
        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long,List<CartItem>> entry : itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();
            int totalOrderPrice = items.stream().mapToInt(
                    CartItem::getSellingprice
            ).sum();

            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order creartedOrder = new Order();
            creartedOrder.setSellerId(sellerId);
            creartedOrder.setUser(user);
            creartedOrder.setShippingAddress(address);
            creartedOrder.setTotalMrpPrice(totalOrderPrice);
            creartedOrder.setTotalSellingPrice(totalOrderPrice);
            creartedOrder.setTotalItem(totalItem);
            creartedOrder.setOrderStatus(OrderStatus.PENDING);
            creartedOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(creartedOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSellingPrice(item.getSellingprice());
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setUserId(user.getId());
                orderItem.setSize(item.getSize());
                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("order not found...."));
    }

    @Override
    public List<Order> UserOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
       Order order = findOrderById(orderId);
       order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId())){
            throw new Exception("You dont have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->
                new Exception("order item not exist ... "));
    }
}
