package fr.fgroup.chatai.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileTypeForbiddenException extends RuntimeException {

  public FileTypeForbiddenException(MultipartFile multipartFile) {
    super("This file type is forbidden: " + multipartFile.getContentType());
  }
}
