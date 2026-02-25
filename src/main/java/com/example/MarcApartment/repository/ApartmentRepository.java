package com.example.MarcApartment.repository;
import com.example.MarcApartment.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Modifying @Transactional @Query(value = "DELETE FROM apartments", nativeQuery = true) void cleanTable();
    @Modifying @Transactional @Query(value = "ALTER TABLE apartments ALTER COLUMN id RESTART WITH 1", nativeQuery = true) void resetIdCounter();
}