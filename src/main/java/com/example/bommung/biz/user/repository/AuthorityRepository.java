package com.example.bommung.biz.user.repository;

import com.example.bommung.biz.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
