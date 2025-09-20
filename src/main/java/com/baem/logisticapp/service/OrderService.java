package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderResponseDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;
import com.baem.logisticapp.entity.Order;

import java.util.List;

public interface OrderService {

        OrderResponseDTO createOrder(OrderCreateDTO createDTO);

        OrderResponseDTO getOrderById(Long id);

        List<OrderResponseDTO> getAllOrders();

        OrderResponseDTO updateOrder(Long id, OrderUpdateDTO updateDTO);

        void deleteOrder(Long id);

        List<OrderResponseDTO> searchOrders(Long customerId, Long salesPersonId, Long fleetPersonId,
                        Long operationPersonId,
                        String tripStatus);

        OrderResponseDTO assignToOperation(Long orderId, Long operationPersonId);

        OrderResponseDTO assignToFleet(Long orderId, Long fleetPersonId);

        // Yeni metodlar
        OrderResponseDTO approveQuote(Long orderId, Long approverUserId);

        OrderResponseDTO cancelQuote(Long orderId, Long cancelerUserId);

        OrderResponseDTO assignToOperationByOperation(Long orderId, Long newOperationPersonId,
                        Long currentOperationPersonId);

        OrderResponseDTO assignFleet(Long orderId, Long vehicleId, Long trailerId, Long driverId);

        List<OrderResponseDTO> getOrdersByOperationPerson(Long operationPersonId);

        // Document generation için entity döndüren method
        Order getOrderEntityById(Long id);
}