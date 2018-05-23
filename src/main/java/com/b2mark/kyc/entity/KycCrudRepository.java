package com.b2mark.kyc.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KycinfoRepository extends CrudRepository<Kycinfo,Long> {

    List<Kycinfo> findByUid(Integer uid);
}