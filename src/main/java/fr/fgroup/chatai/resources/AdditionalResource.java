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
public class AdditionalResource {

  @JsonProperty("headers")
  private HeadersResource headers;

  @JsonProperty("status")
  private int status;

  @JsonProperty("statusText")
  private String statusText;

  @JsonProperty("url")
  private String url;

  @JsonProperty("ok")
  private boolean ok;

  @JsonProperty("name")
  private String name;

  @JsonProperty("message")
  private String message;

  @JsonProperty("error")
  private String error;
}
