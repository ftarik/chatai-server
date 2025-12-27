package fr.fgroup.chatai.utils;

import fr.fgroup.chatai.enums.AuthorityEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tarik.famil (contact: tarikfamil@gmail.com)
 *
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 25 avr. 2019
 */
@Getter
@Setter
public class UserContext {

  private String email;
  private String key;
  private String token;
  private String tenantId;
  private String employeeId;
  private AuthorityEnum authority;
  private String role;
  private boolean isAdmin;

}
