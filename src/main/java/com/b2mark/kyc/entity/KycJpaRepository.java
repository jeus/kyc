package com.b2mark.kyc.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface KycJpaRepository extends JpaRepository<Kycinfo, Long> {

    Optional<Kycinfo> findByUid(Integer uid);
}
