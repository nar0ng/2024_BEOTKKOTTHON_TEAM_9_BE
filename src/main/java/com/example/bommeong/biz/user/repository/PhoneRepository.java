package com.example.bommeong.biz.user.repository;

import com.example.bommeong.biz.user.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByNumber(String number);
}
