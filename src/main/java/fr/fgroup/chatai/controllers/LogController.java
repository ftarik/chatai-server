package fr.fgroup.chatai.controllers;

import fr.fgroup.chatai.resources.LogResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 09/03/2023
 */

@RequestMapping("/chatai")
public interface LogController {

  @PostMapping("/system")
  ResponseEntity<Void> log(@RequestBody String data);

  @PostMapping("/log")
  ResponseEntity<Void> logs(@RequestBody LogResource resource);

}
