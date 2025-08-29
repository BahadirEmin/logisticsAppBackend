package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderResponseDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;
import com.baem.logisticapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Order management operations")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateDTO createDTO) {
        return new ResponseEntity<>(orderService.createOrder(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateDTO updateDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/sales-person/{salesPersonId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersBySalesPersonId(@PathVariable Long salesPersonId) {
        return ResponseEntity.ok(orderService.getOrdersBySalesPersonId(salesPersonId));
    }

    @GetMapping("/fleet-person/{fleetPersonId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByFleetPersonId(@PathVariable Long fleetPersonId) {
        return ResponseEntity.ok(orderService.getOrdersByFleetPersonId(fleetPersonId));
    }

    @GetMapping("/status/{tripStatus}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByTripStatus(@PathVariable String tripStatus) {
        return ResponseEntity.ok(orderService.getOrdersByTripStatus(tripStatus));
    }

    @PostMapping("/{orderId}/assign-operation")
    public ResponseEntity<OrderResponseDTO> assignToOperation(
            @PathVariable Long orderId,
            @RequestParam Long operationPersonId) {
        return ResponseEntity.ok(orderService.assignToOperation(orderId, operationPersonId));
    }

    @PostMapping("/{orderId}/assign-fleet")
    public ResponseEntity<OrderResponseDTO> assignToFleet(
            @PathVariable Long orderId,
            @RequestParam Long fleetPersonId) {
        return ResponseEntity.ok(orderService.assignToFleet(orderId, fleetPersonId));
    }

    @PostMapping("/{orderId}/approve-quote")
    public ResponseEntity<OrderResponseDTO> approveQuote(
            @PathVariable Long orderId,
            @RequestParam Long approverUserId) {
        return ResponseEntity.ok(orderService.approveQuote(orderId, approverUserId));
    }

    @PostMapping("/{orderId}/cancel-quote")
    public ResponseEntity<OrderResponseDTO> cancelQuote(
            @PathVariable Long orderId,
            @RequestParam Long cancelerUserId) {
        return ResponseEntity.ok(orderService.cancelQuote(orderId, cancelerUserId));
    }

    @PostMapping("/{orderId}/reassign-operation")
    public ResponseEntity<OrderResponseDTO> reassignOperation(
            @PathVariable Long orderId,
            @RequestParam Long newOperationPersonId,
            @RequestParam Long currentOperationPersonId) {
        return ResponseEntity.ok(orderService.assignToOperationByOperation(orderId, newOperationPersonId, currentOperationPersonId));
    }

    @PostMapping("/{orderId}/assign-fleet-resources")
    @Operation(
        summary = "Assign fleet resources to order",
        description = "Assign vehicle, trailer, and/or driver to an order. All parameters are optional."
    )
    public ResponseEntity<OrderResponseDTO> assignFleet(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "Vehicle ID (optional)") @RequestParam(required = false) Long vehicleId,
            @Parameter(description = "Trailer ID (optional)") @RequestParam(required = false) Long trailerId,
            @Parameter(description = "Driver ID (optional)") @RequestParam(required = false) Long driverId) {
        return ResponseEntity.ok(orderService.assignFleet(orderId, vehicleId, trailerId, driverId));
    }
}