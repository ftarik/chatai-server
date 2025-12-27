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
 * Created 05/03/2023
 */
@Data
@ToString
public class UsageResource {
  @JsonProperty("prompt_tokens")
  private int promptTokens;
  @JsonProperty("completion_tokens")
  private int completionTokens;
  @JsonProperty("total_tokens")
  private int totalTokens;

}
