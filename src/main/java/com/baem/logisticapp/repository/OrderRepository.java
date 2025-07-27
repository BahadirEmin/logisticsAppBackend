package com.baem.logisticapp.repository;

import com.baem.logisticapp.entity.Order;
import com.baem.logisticapp.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Müşteriye göre siparişler
    List<Order> findByCustomerId(Long customerId);

    // Duruma göre siparişler
    List<Order> findByTripStatus(TripStatus tripStatus);

    // Satış personeline göre siparişler
    List<Order> findBySalesPersonId(Long salesPersonId);

    // Operasyoncuya göre siparişler
    List<Order> findByOperationPersonId(Long operationPersonId);

    // Filocuya göre siparişler
    List<Order> findByFleetPersonId(Long fleetPersonId);

    // Gümrükçüye göre siparişler
    List<Order> findByCustomsPersonId(Long customsPersonId);

    // Kamyona göre siparişler
    List<Order> findByAssignedTruckId(Long truckId);

    // Trailer'a göre siparişler
    List<Order> findByAssignedTrailerId(Long trailerId);

    // Müşteri ve duruma göre siparişler
    List<Order> findByCustomerIdAndTripStatus(Long customerId, TripStatus tripStatus);

    // Satış personeli ve duruma göre siparişler
    List<Order> findBySalesPersonIdAndTripStatus(Long salesPersonId, TripStatus tripStatus);

    // Aktif siparişler (Teslim edilmemiş)
    List<Order> findByTripStatusNot(TripStatus tripStatus);

    // Varış şehrine göre siparişler
    List<Order> findByArrivalCityContainingIgnoreCase(String city);

    // Çıkış şehrine göre siparişler
    List<Order> findByDepartureCityContainingIgnoreCase(String city);
}