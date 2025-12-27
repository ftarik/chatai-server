package fr.fgroup.chatai.services;

import fr.fgroup.chatai.resources.LogResource;
import org.springframework.http.ResponseEntity;


public interface LogService {

  ResponseEntity<Void> create(String data);
  ResponseEntity<Void> logs(LogResource resource);
}
