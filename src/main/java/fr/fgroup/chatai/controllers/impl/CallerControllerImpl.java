package fr.fgroup.chatai.controllers.impl;

import fr.fgroup.chatai.controllers.CallerController;
import fr.fgroup.chatai.resources.KeyResource;
import fr.fgroup.chatai.resources.MessageResource;
import fr.fgroup.chatai.resources.post.ContinueMessageResourcePost;
import fr.fgroup.chatai.resources.post.MessageResourcePost;
import fr.fgroup.chatai.services.CallerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class CallerControllerImpl implements CallerController {

  private final CallerService callerService;

  @Override
  public ResponseEntity<KeyResource> getKey() {
    return callerService.generateKey();
  }

  @Override
  public ResponseEntity<MessageResource> askRequest(MessageResourcePost message) {
    return callerService.askRequest(message);
  }

  @Override
  public ResponseEntity<MessageResource> continueConversation(List<ContinueMessageResourcePost> messages) {
    return callerService.continueConversation(messages);
  }
}
