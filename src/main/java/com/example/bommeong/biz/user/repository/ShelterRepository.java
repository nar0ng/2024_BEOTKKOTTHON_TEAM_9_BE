package com.example.bommeong.biz.user.repository;

import com.example.bommeong.biz.user.domain.ShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<ShelterEntity, Long> {
    Optional<ShelterEntity> findByEmail(String email);

    @Query(value = "SELECT s.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) AS distance " +
            "FROM shelter s " +
            "HAVING distance < :maxDistance " +
            "ORDER BY distance",
            nativeQuery = true)
    List<ShelterEntity> findAllWithLocation(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("maxDistance") Double maxDistance
    );
}
