package fr.fgroup.chatai.resources;

import lombok.Data;
import lombok.ToString;

import java.util.List;

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
public class RequestResponse {

  private String id;
  private String object;
  private Long created;
  private String model;
  private List<ChoiceResource> choices;
  private UsageResource usage;

}
