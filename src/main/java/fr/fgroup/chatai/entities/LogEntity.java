package fr.fgroup.chatai.entities;

import fr.fgroup.chatai.entities.base.AbstractBaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * LogEntity - JPA entity for storing audit logs of user actions.
 * 
 * This entity tracks all significant user actions in the system:
 * - What action was performed (action type)
 * - Who triggered the action (user)
 * - When it occurred (action date)
 * - The result of the action
 * - Detailed description of what happened
 * - Category of the action (e.g., screen, API call)
 * 
 * Logs are automatically persisted for compliance and debugging purposes.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2023-03-05
 */
@Data
@ToString
@Entity(name = "log_entity")
public class LogEntity extends AbstractBaseEntity {

  /** Category of the logged action (e.g., "screen", "api", "system") */
  private String category;

  /** User or system account that triggered the action (e.g., user email or API key) */
  private String userActionTrigger;

  /** Date and time when the action occurred */
  private LocalDateTime actionDate;

  /** Type of action performed (e.g., "CREATE", "UPDATE", "DELETE", "READ") */
  private String actionType;

  /** Result or outcome of the action (e.g., "Success", "Failed", "contact shared") */
  private String actionResult;

  /** Detailed description of what the action entailed */
  private String actionDesc;
}
