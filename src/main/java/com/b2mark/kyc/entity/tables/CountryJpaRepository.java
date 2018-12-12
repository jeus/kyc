/**
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.entity.tables;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<Country, String> {
    Optional<Country> findById(String id);
}
