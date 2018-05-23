package com.b2mark.kyc.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KycCrudRepository extends CrudRepository<Kycinfo,Long> {

    List<Kycinfo> findByUid(Integer uid);
}