package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderResponseDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderCreateDTO createDTO);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO updateOrder(Long id, OrderUpdateDTO updateDTO);

    void deleteOrder(Long id);

    List<OrderResponseDTO> getOrdersByCustomerId(Long customerId);

    List<OrderResponseDTO> getOrdersBySalesPersonId(Long salesPersonId);

    List<OrderResponseDTO> getOrdersByTripStatus(String tripStatus);
}