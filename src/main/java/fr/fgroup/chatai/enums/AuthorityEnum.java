package fr.fgroup.chatai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 20/08/2021
 */

@Getter
@AllArgsConstructor
public enum AuthorityEnum {
  NONE("NONE", "None");

  /**
   * code of the status
   */
  private final String code;

  /**
   * Display label of the status
   */
  private final String name;

}
