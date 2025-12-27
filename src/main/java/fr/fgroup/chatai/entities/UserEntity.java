package fr.fgroup.chatai.entities;

import fr.fgroup.chatai.entities.base.AbstractAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * UserEntity - JPA entity representing a ChatAI API user.
 * 
 * Each user has:
 * - A unique key for API authentication
 * - A quota of authorized tokens (set at user creation)
 * - Current token usage tracking
 * - Audit information (creation date/user, modification date/user)
 * 
 * Token tracking is used to enforce API usage limits and prevent abuse.
 * When a user makes API requests, tokens are consumed from their quota.
 * Once totalTokens exceeds totalTokensAuthorized, further requests are denied.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2021-06-24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_entity")
public class UserEntity extends AbstractAuditableEntity {

  /** Unique user key for API authentication (SHA-256 hash) */
  private String key;
  
  /** Current total tokens used by this user */
  @Column(nullable = false)
  private Long totalTokens;
  
  /** Maximum tokens authorized for this user */
  @Column(nullable = false)
  private Long totalTokensAuthorized;

}
