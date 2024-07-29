package com.example.bommeong.biz.user.repository;

import com.example.bommeong.biz.user.domain.ShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<ShelterEntity, Long> {
    Optional<ShelterEntity> findByEmail(String email);
}
