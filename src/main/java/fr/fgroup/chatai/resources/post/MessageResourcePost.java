package fr.fgroup.chatai.resources.post;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */

@Data
public class MessageResourcePost {
  @NotNull private String content;
}
