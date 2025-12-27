package fr.fgroup.chatai.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarik.famil@acensi.fr)
 * <p>
 * Copyright (C) ACENSI, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 11/03/2023
 */

@Data
@ToString
public class HeadersResource {

  @JsonProperty("normalizedNames")
  private Object normalizedNames;

  @JsonProperty("lazyUpdate")
  private Object lazyUpdate;
}
