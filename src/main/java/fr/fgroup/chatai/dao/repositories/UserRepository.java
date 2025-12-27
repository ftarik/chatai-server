package fr.fgroup.chatai.dao.repositories;

import fr.fgroup.chatai.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * UserRepository - Spring Data JPA repository for UserEntity.
 * 
 * Provides database access for UserEntity with:
 * - Standard CRUD operations (inherited from JpaRepository)
 * - Dynamic query building with specifications (inherited from JpaSpecificationExecutor)
 * 
 * This repository enables flexible querying using JPA Specification pattern,
 * allowing for complex WHERE clauses without writing explicit SQL or HQL.
 * 
 * @author Spring Data
 * @version 1.0
 * @since 2023-03-05
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

}
