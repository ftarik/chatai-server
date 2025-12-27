package fr.fgroup.chatai.dao.specifications;

import fr.fgroup.chatai.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * UserSpecifications - JPA Specification definitions for UserEntity queries.
 * 
 * This class provides reusable specification predicates for building
 * dynamic queries without writing explicit JPQL or SQL.
 * 
 * Used with Spring Data JPA's JpaSpecificationExecutor to enable
 * flexible database queries based on runtime criteria.
 * 
 * Example usage:
 * userRepository.findOne(Specification.where(UserSpecifications.withKey("user-key")))
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2023-03-05
 */
@Component
public class UserSpecifications {

  /**
   * Private constructor to prevent instantiation of utility class.
   */
  private UserSpecifications() {
  }

  /**
   * Creates a specification to find a user by ID.
   * 
   * @param id the user's ID
   * @return Specification for querying users by ID, or null if id is null
   */
  public static Specification<UserEntity> withId(Long id) {
    return id == null ? null : (root, query, builder) -> builder.equal(root.get("id"), id);
  }

  /**
   * Creates a specification to find a user by email.
   * 
   * @param email the user's email address
   * @return Specification for querying users by email, or null if email is null
   */
  public static Specification<UserEntity> withEmail(String email) {
    return email == null ? null : (root, query, builder) -> builder.equal(root.get("email"), email);
  }

  /**
   * Creates a specification to find a user by API key.
   * 
   * This is the most common query used to authenticate API requests.
   * 
   * @param key the user's unique API key
   * @return Specification for querying users by key, or null if key is null
   */
  public static Specification<UserEntity> withKey(String key) {
    return key == null ? null : (root, query, builder) -> builder.equal(root.get("key"), key);
  }
}
