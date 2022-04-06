package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccountRef;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountRef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountRefRepository extends JpaRepository<AccountRef, Long> {}
