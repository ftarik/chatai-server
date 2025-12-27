package fr.fgroup.chatai.dao.services.impl;
import fr.fgroup.chatai.dao.repositories.LogRepository;
import fr.fgroup.chatai.dao.services.LogDaoService;
import fr.fgroup.chatai.entities.LogEntity;
import fr.fgroup.chatai.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogDaoServiceImpl implements LogDaoService {

  private final LogRepository logRepository;

  @Override
  public LogEntity findOne(Specification<LogEntity> specification) {
    return logRepository.findOne(specification).orElseThrow(() -> {
      log.debug("Couldn't find any Log with the specified criteria");
      return new ResourceNotFoundException("No configuration found with the specified criteria");
    });
  }

  @Override
  public Page<LogEntity> findAll(Specification<LogEntity> specification, Pageable pageable) {
    return logRepository.findAll(specification, pageable);
  }

  @Override
  public LogEntity save(LogEntity entity) {
    return logRepository.save(entity);
  }

  @Override
  public void delete(LogEntity entity) {
    logRepository.delete(entity);
  }
}
