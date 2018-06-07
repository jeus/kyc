/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.entity;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface KycCrudRepository extends CrudRepository<Kycinfo,Long> {

    List<Kycinfo> findByUid(Integer uid);
}