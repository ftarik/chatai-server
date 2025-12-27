package fr.fgroup.chatai.dao.services;

import fr.fgroup.chatai.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * UserDaoService - Data access service for UserEntity operations.
 * 
 * This service provides abstractions for database operations on UserEntity.
 * It hides the details of Spring Data JPA from the business logic layer.
 * 
 * Operations include:
 * - Creating/updating users (save)
 * - Deleting users
 * - Finding users by specification criteria
 * - Paginated retrieval of users
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2023-03-05
 */
public interface UserDaoService {

  /**
   * Saves a UserEntity to the database.
   * 
   * Creates a new user if ID is null, otherwise updates the existing user.
   * 
   * @param entity the UserEntity to save
   * @return the saved entity (with generated ID if new)
   */
  UserEntity save(UserEntity entity);

  /**
   * Deletes a UserEntity from the database.
   * 
   * @param entity the UserEntity to delete
   */
  void delete(UserEntity entity);

  /**
   * Finds a single UserEntity matching the given specification.
   * 
   * @param specification the JPA specification criteria
   * @return the matching UserEntity
   * @throws ResourceNotFoundException if no user matches the specification
   */
  UserEntity findOne(Specification<UserEntity> specification);

  /**
   * Finds a paginated list of users matching the given specification.
   * 
   * @param specification the JPA specification criteria
   * @param pageable pagination and sorting information
   * @return a Page of matching users
   */
  Page<UserEntity> findAll(Specification<UserEntity> specification, Pageable pageable);
}
