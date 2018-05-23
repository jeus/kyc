package com.b2mark.kyc.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface KycJpaRepository extends JpaRepository<Kycinfo, Long> {

    Collection<Kycinfo> findByUid(Integer uid);
}
