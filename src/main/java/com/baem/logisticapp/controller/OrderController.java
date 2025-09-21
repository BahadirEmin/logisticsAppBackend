package com.baem.logisticapp.controller;

import com.baem.logisticapp.dto.OrderCreateDTO;
import com.baem.logisticapp.dto.OrderResponseDTO;
import com.baem.logisticapp.dto.OrderUpdateDTO;
import com.baem.logisticapp.entity.Order;
import com.baem.logisticapp.service.DocumentService;
import com.baem.logisticapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "Order management operations")
public class OrderController {

    private final OrderService orderService;
    private final DocumentService documentService;

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

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponseDTO>> searchOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long salesPersonId,
            @RequestParam(required = false) Long fleetPersonId,
            @RequestParam(required = false) Long operationPersonId,
            @RequestParam(required = false) String tripStatus) {
        return ResponseEntity
                .ok(orderService.searchOrders(customerId, salesPersonId, fleetPersonId, operationPersonId, tripStatus));
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
        return ResponseEntity
                .ok(orderService.assignToOperationByOperation(orderId, newOperationPersonId, currentOperationPersonId));
    }

    @PostMapping("/{orderId}/assign-fleet-resources")
    @Operation(summary = "Assign fleet resources to order", description = "Assign vehicle, trailer, and/or driver to an order. All parameters are optional.")
    public ResponseEntity<OrderResponseDTO> assignFleet(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "Vehicle ID (optional)") @RequestParam(required = false) Long vehicleId,
            @Parameter(description = "Trailer ID (optional)") @RequestParam(required = false) Long trailerId,
            @Parameter(description = "Driver ID (optional)") @RequestParam(required = false) Long driverId) {
        return ResponseEntity.ok(orderService.assignFleet(orderId, vehicleId, trailerId, driverId));
    }

    @GetMapping("/operation-person/{operationPersonId}")
    @Operation(summary = "Get orders by operation person", description = "Retrieve all orders assigned to a specific operation person")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByOperationPerson(
            @Parameter(description = "Operation Person ID") @PathVariable Long operationPersonId) {
        return ResponseEntity.ok(orderService.getOrdersByOperationPerson(operationPersonId));
    }

    @GetMapping("/{orderId}/driver-information-document")
    @Operation(summary = "Download driver information document", description = "Generate and download Word document with driver information for the order")
    public ResponseEntity<byte[]> downloadDriverInformationDocument(
            @Parameter(description = "Order ID") @PathVariable Long orderId) {

        try {
            log.info("Starting driver information document generation for order: {}", orderId);

            OrderResponseDTO orderResponse = orderService.getOrderById(orderId);
            log.info("Order found: {}", orderResponse.getOrderNumber());

            Order orderEntity = orderService.getOrderEntityById(orderId);
            log.info("Order entity retrieved, driver: {}",
                    orderEntity.getAssignedDriver() != null ? orderEntity.getAssignedDriver().getFullName() : "null");

            // Word document'i oluştur
            byte[] documentBytes = documentService.generateDriverInformationDocument(orderEntity);

            log.info("Document generated successfully, size: {} bytes", documentBytes.length);

            // Response headers'ı ayarla
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    "driver_information_" + orderResponse.getOrderNumber() + ".docx");
            headers.setContentLength(documentBytes.length);

            return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generating driver information document for order {}: {}", orderId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}