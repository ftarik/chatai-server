package fr.fgroup.chatai.controllers;

import fr.fgroup.chatai.resources.KeyResource;
import fr.fgroup.chatai.resources.MessageResource;
import fr.fgroup.chatai.resources.post.ContinueMessageResourcePost;
import fr.fgroup.chatai.resources.post.MessageResourcePost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */
@RequestMapping("/chatai/requests")
public interface CallerController {

  @GetMapping
  ResponseEntity<KeyResource> getKey();

  @PostMapping
  ResponseEntity<MessageResource> askRequest(@Valid @RequestBody MessageResourcePost message);

  @PostMapping("/continue")
  ResponseEntity<MessageResource> continueConversation(@Valid @RequestBody List<ContinueMessageResourcePost> messages);
}
