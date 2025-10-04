package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.OrderAssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAssignmentHistoryRepository extends JpaRepository<OrderAssignmentHistory, Long> {
    
    List<OrderAssignmentHistory> findByOrderIdOrderByAssignedAtDesc(Long orderId);
    
    List<OrderAssignmentHistory> findByOrderIdAndResourceTypeOrderByAssignedAtDesc(Long orderId, OrderAssignmentHistory.ResourceType resourceType);
}
