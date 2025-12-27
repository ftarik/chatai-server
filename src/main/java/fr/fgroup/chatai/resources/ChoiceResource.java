package fr.fgroup.chatai.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 04/03/2023
 */

@Data
@ToString
public class ChoiceResource {
  private MessageResource message;
  private int index;
  @JsonProperty("finish_reason")
  private String finishReason;
}
