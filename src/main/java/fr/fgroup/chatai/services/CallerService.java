package fr.fgroup.chatai.services;

import fr.fgroup.chatai.resources.KeyResource;
import fr.fgroup.chatai.resources.MessageResource;
import fr.fgroup.chatai.resources.post.ContinueMessageResourcePost;
import fr.fgroup.chatai.resources.post.MessageResourcePost;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */
public interface CallerService {
  ResponseEntity<KeyResource> generateKey();

  ResponseEntity<MessageResource> askRequest(MessageResourcePost message);

  ResponseEntity<MessageResource> continueConversation(List<ContinueMessageResourcePost> messages);
}
