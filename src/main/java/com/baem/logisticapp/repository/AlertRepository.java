package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Alert;
import com.baem.logisticapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Aktif alertler
    List<Alert> findByIsActiveTrueOrderByCreatedAtDesc();

    // Okunmamış alertler
    List<Alert> findByIsActiveTrueAndIsReadFalseOrderByCreatedAtDesc();

    // Kullanıcıya atanmış alertler
    List<Alert> findByAssignedToUserAndIsActiveTrueOrderByCreatedAtDesc(User user);

    // Kullanıcı ID'sine göre atanmış alertler
    List<Alert> findByAssignedToUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);

    // Öncelik bazında alertler
    List<Alert> findByPriorityAndIsActiveTrueOrderByCreatedAtDesc(Alert.AlertPriority priority);

    // Bugün verilmesi gereken alertler
    @Query("SELECT a FROM Alert a WHERE a.alertDate <= :today AND a.isActive = true AND a.isRead = false")
    List<Alert> findAlertsToShowToday(@Param("today") LocalDate today);

    // Belirli entity type ve ID için alertler
    List<Alert> findByRelatedEntityTypeAndRelatedEntityIdAndIsActiveTrue(String entityType, Long entityId);

    // Alert type bazında
    List<Alert> findByAlertTypeAndIsActiveTrueOrderByCreatedAtDesc(Alert.AlertType alertType);

    // Yakında expire olacak alertler (30 gün içinde)
    @Query("SELECT a FROM Alert a WHERE a.expiryDate BETWEEN :startDate AND :endDate AND a.isActive = true")
    List<Alert> findExpiringAlerts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Kritik alertler (High ve Critical priority)
    @Query("SELECT a FROM Alert a WHERE a.priority IN ('HIGH', 'CRITICAL') AND a.isActive = true AND a.isRead = false ORDER BY a.priority DESC, a.createdAt DESC")
    List<Alert> findCriticalAlerts();

    // Alert sayısı (dashboard için)
    Long countByIsActiveTrueAndIsReadFalse();

    // Kullanıcı bazında okunmamış alert sayısı
    Long countByAssignedToUserAndIsActiveTrueAndIsReadFalse(User user);
}
