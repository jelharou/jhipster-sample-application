package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartyId;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PartyId entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyIdRepository extends JpaRepository<PartyId, Long> {}
