package fr.fgroup.chatai.controllers.impl;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 09/03/2023
 */

import fr.fgroup.chatai.controllers.LogController;
import fr.fgroup.chatai.resources.LogResource;
import fr.fgroup.chatai.services.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LogControllerImpl implements LogController {

  private final LogService logService;

  @Override
  public ResponseEntity<Void> log(String data) {
    return logService.create(data);
  }

  @Override
  public ResponseEntity<Void> logs(LogResource resource) {
    return logService.logs(resource);
  }
}
