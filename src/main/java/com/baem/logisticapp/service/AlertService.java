package com.baem.logisticapp.service;

import com.baem.logisticapp.dto.AlertResponseDTO;
import com.baem.logisticapp.entity.Alert;
import com.baem.logisticapp.entity.Driver;
import com.baem.logisticapp.entity.User;
import com.baem.logisticapp.entity.Vehicle;
import com.baem.logisticapp.repository.AlertRepository;
import com.baem.logisticapp.repository.DriverRepository;
import com.baem.logisticapp.repository.UserRepository;
import com.baem.logisticapp.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public AlertService(AlertRepository alertRepository,
                       DriverRepository driverRepository,
                       VehicleRepository vehicleRepository,
                       UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    // Tüm aktif alertleri getir
    public List<AlertResponseDTO> getAllActiveAlerts() {
        List<Alert> alerts = alertRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        return alerts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Okunmamış alertleri getir
    public List<AlertResponseDTO> getUnreadAlerts() {
        List<Alert> alerts = alertRepository.findByIsActiveTrueAndIsReadFalseOrderByCreatedAtDesc();
        return alerts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Kritik alertleri getir
    public List<AlertResponseDTO> getCriticalAlerts() {
        List<Alert> alerts = alertRepository.findCriticalAlerts();
        return alerts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Alert'i okundu olarak işaretle
    public void markAsRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        alert.setIsRead(true);
        alert.setReadAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    // Alert'i deaktif et
    public void deactivateAlert(Long alertId, Long userId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        alert.setIsActive(false);
        alert.setDeactivatedBy(user);
        alert.setDeactivatedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    // Driver için visa alert'i oluştur
    public void createDriverVisaAlert(Long driverId, LocalDate visaExpiryDate) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Alert alert = Alert.builder()
                .alertType(Alert.AlertType.VISA_EXPIRING)
                .priority(determinePriority(visaExpiryDate))
                .title("Driver Visa Expiring")
                .description(String.format("Driver %s %s's visa will expire on %s", 
                           driver.getFirstName(), driver.getLastName(), visaExpiryDate))
                .relatedEntityType("DRIVER")
                .relatedEntityId(driverId)
                .expiryDate(visaExpiryDate)
                .alertDate(visaExpiryDate.minusDays(30)) // 30 gün önce alert
                .build();

        alertRepository.save(alert);
    }

    // Driver için ehliyet alert'i oluştur
    public void createDriverLicenseAlert(Long driverId, LocalDate licenseExpiryDate) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Alert alert = Alert.builder()
                .alertType(Alert.AlertType.LICENSE_EXPIRING)
                .priority(determinePriority(licenseExpiryDate))
                .title("Driver License Expiring")
                .description(String.format("Driver %s %s's license will expire on %s", 
                           driver.getFirstName(), driver.getLastName(), licenseExpiryDate))
                .relatedEntityType("DRIVER")
                .relatedEntityId(driverId)
                .expiryDate(licenseExpiryDate)
                .alertDate(licenseExpiryDate.minusDays(30))
                .build();

        alertRepository.save(alert);
    }

    // Vehicle için muayene alert'i oluştur
    public void createVehicleInspectionAlert(Long vehicleId, LocalDate inspectionDueDate) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Alert alert = Alert.builder()
                .alertType(Alert.AlertType.VEHICLE_INSPECTION_DUE)
                .priority(determinePriority(inspectionDueDate))
                .title("Vehicle Inspection Due")
                .description(String.format("Vehicle %s %s %s inspection is due on %s", 
                           vehicle.getBrand(), vehicle.getModel(), vehicle.getLicensePlate(), inspectionDueDate))
                .relatedEntityType("VEHICLE")
                .relatedEntityId(vehicleId)
                .expiryDate(inspectionDueDate)
                .alertDate(inspectionDueDate.minusDays(15)) // 15 gün önce alert
                .build();

        alertRepository.save(alert);
    }

    // Otomatik alert kontrol sistemi - her gün çalışır
    @Scheduled(cron = "0 0 8 * * ?") // Her gün sabah 8'de çalış
    public void checkAndCreateAlerts() {
        logger.info("🔔 ALERT SYSTEM: Starting scheduled alert check at {}", LocalDateTime.now());
        logger.info("📋 ALERT SYSTEM: Checking driver visas, licenses and vehicle inspections...");
        
        int totalCreated = 0;
        totalCreated += checkDriverVisas();
        totalCreated += checkDriverLicenses();
        totalCreated += checkVehicleInspections();
        
        logger.info("✅ ALERT SYSTEM: Completed alert check. Total new alerts created: {}", totalCreated);
    }

    private int checkDriverVisas() {
        logger.info("🚗 Checking driver visas...");
        List<Driver> drivers = driverRepository.findAll();
        LocalDate checkDate = LocalDate.now().plusDays(30); // 30 gün sonraya kadar kontrol et
        int createdCount = 0;

        logger.info("📊 Found {} drivers to check for visa expiry", drivers.size());
        
        for (Driver driver : drivers) {
            if (driver.getVisaExpiryDate() != null && 
                driver.getVisaExpiryDate().isBefore(checkDate)) {
                
                // Bu driver için zaten alert var mı kontrol et
                List<Alert> existingAlerts = alertRepository.findByRelatedEntityTypeAndRelatedEntityIdAndIsActiveTrue("DRIVER", driver.getId());
                boolean hasVisaAlert = existingAlerts.stream()
                        .anyMatch(alert -> alert.getAlertType() == Alert.AlertType.VISA_EXPIRING);
                
                if (!hasVisaAlert) {
                    createDriverVisaAlert(driver.getId(), driver.getVisaExpiryDate());
                    createdCount++;
                    logger.info("📝 Created visa alert for driver: {} {} (expires: {})", 
                        driver.getFirstName(), driver.getLastName(), driver.getVisaExpiryDate());
                }
            }
        }
        
        logger.info("✅ Visa check completed. Created {} new alerts", createdCount);
        return createdCount;
    }

    private int checkDriverLicenses() {
        logger.info("🪪 Checking driver licenses...");
        List<Driver> drivers = driverRepository.findAll();
        LocalDate checkDate = LocalDate.now().plusDays(30);
        int createdCount = 0;

        for (Driver driver : drivers) {
            if (driver.getLicenseExpiryDate() != null && 
                driver.getLicenseExpiryDate().isBefore(checkDate)) {
                
                List<Alert> existingAlerts = alertRepository.findByRelatedEntityTypeAndRelatedEntityIdAndIsActiveTrue("DRIVER", driver.getId());
                boolean hasLicenseAlert = existingAlerts.stream()
                        .anyMatch(alert -> alert.getAlertType() == Alert.AlertType.LICENSE_EXPIRING);
                
                if (!hasLicenseAlert) {
                    createDriverLicenseAlert(driver.getId(), driver.getLicenseExpiryDate());
                    createdCount++;
                    logger.info("📝 Created license alert for driver: {} (expires: {})", 
                        driver.getFirstName() + " " + driver.getLastName(), driver.getLicenseExpiryDate());
                }
            }
        }
        
        logger.info("✅ License check completed. Created {} new alerts", createdCount);
        return createdCount;
    }

    private int checkVehicleInspections() {
        logger.info("🔧 Checking vehicle inspections...");
        List<Vehicle> vehicles = vehicleRepository.findAll();
        LocalDate checkDate = LocalDate.now().plusDays(15); // Araçlar için 15 gün
        int createdCount = 0;

        for (Vehicle vehicle : vehicles) {
            if (vehicle.getInspectionExpiryDate() != null && 
                vehicle.getInspectionExpiryDate().isBefore(checkDate)) {
                
                List<Alert> existingAlerts = alertRepository.findByRelatedEntityTypeAndRelatedEntityIdAndIsActiveTrue("VEHICLE", vehicle.getId());
                boolean hasInspectionAlert = existingAlerts.stream()
                        .anyMatch(alert -> alert.getAlertType() == Alert.AlertType.VEHICLE_INSPECTION_DUE);
                
                if (!hasInspectionAlert) {
                    createVehicleInspectionAlert(vehicle.getId(), vehicle.getInspectionExpiryDate());
                    createdCount++;
                    logger.info("📝 Created inspection alert for vehicle: {} {} (due: {})", 
                        vehicle.getBrand(), vehicle.getModel(), vehicle.getInspectionExpiryDate());
                }
            }
        }
        
        logger.info("✅ Vehicle inspection check completed. Created {} new alerts", createdCount);
        return createdCount;
    }

    private Alert.AlertPriority determinePriority(LocalDate expiryDate) {
        long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        
        if (daysUntilExpiry <= 0) return Alert.AlertPriority.CRITICAL;
        if (daysUntilExpiry <= 7) return Alert.AlertPriority.HIGH;
        if (daysUntilExpiry <= 30) return Alert.AlertPriority.MEDIUM;
        return Alert.AlertPriority.LOW;
    }

    private AlertResponseDTO convertToDTO(Alert alert) {
        return new AlertResponseDTO(
                alert.getId(),
                alert.getAlertType().getDisplayName(),
                alert.getPriority().getDisplayName(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getRelatedEntityType(),
                alert.getRelatedEntityId(),
                alert.getExpiryDate(),
                alert.getAlertDate(),
                alert.getIsActive(),
                alert.getIsRead(),
                alert.getCreatedAt(),
                alert.getReadAt(),
                alert.getAssignedToUser() != null ? alert.getAssignedToUser().getUsername() : null,
                alert.getDeactivatedBy() != null ? alert.getDeactivatedBy().getUsername() : null,
                alert.getDeactivatedAt()
        );
    }

    // Dashboard için alert sayısı
    public Long getUnreadAlertCount() {
        return alertRepository.countByIsActiveTrueAndIsReadFalse();
    }

    // Alert'i kullanıcıya ata
    public void assignAlert(Long alertId, Long userId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        alert.setAssignedToUser(user);
        alertRepository.save(alert);
    }

    // Alert'i kullanıcıdan al
    public void unassignAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        
        alert.setAssignedToUser(null);
        alertRepository.save(alert);
    }

    // Belirli kullanıcıya atanmış alertleri getir
    public List<AlertResponseDTO> getAlertsAssignedToUser(Long userId) {
        List<Alert> alerts = alertRepository.findByAssignedToUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId);
        return alerts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
