package com.example.bommeong.biz.user.repository;

import com.example.bommeong.biz.user.domain.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    RefreshEntity findByUsername(String username);

    @Transactional
    void deleteByRefresh(String refresh);
}
