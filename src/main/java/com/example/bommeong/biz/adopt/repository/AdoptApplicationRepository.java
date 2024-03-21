package com.example.bommeong.biz.adopt.repository;

import com.example.bommeong.biz.adopt.dao.AdoptApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptApplicationRepository extends JpaRepository<AdoptApplicationEntity, Long> {
}
