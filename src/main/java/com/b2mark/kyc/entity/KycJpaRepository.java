/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.entity;

import com.b2mark.kyc.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.Optional;

public interface KycJpaRepository extends JpaRepository<Kycinfo, Long> {
    Optional<Kycinfo> findByUid(Integer uid);
    Collection<Kycinfo> findByStatus(Status status);
}
