package com.example.bommeong.biz.adopt.repository;

import com.example.bommeong.biz.adopt.dao.AdoptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdoptRepository extends JpaRepository<AdoptEntity, Long> {
}
