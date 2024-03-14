package com.example.bommeong.biz.user.repository;

import com.example.bommeong.biz.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
