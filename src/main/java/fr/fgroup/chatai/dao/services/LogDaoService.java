package fr.fgroup.chatai.dao.services;

import fr.fgroup.chatai.entities.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface LogDaoService {

  LogEntity findOne(Specification<LogEntity> specification);

  Page<LogEntity> findAll(Specification<LogEntity> specification, Pageable pageable);

  LogEntity save(LogEntity entity);

  void delete(LogEntity entity);
}
