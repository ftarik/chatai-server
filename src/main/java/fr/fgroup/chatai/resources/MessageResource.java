package fr.fgroup.chatai.resources;

import fr.fgroup.chatai.enums.AIRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageResource {
  private AIRoleEnum role;
  private String content;
}
