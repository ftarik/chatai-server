package fr.fgroup.chatai.dao.services.impl;

import fr.fgroup.chatai.dao.repositories.UserRepository;
import fr.fgroup.chatai.dao.services.UserDaoService;
import fr.fgroup.chatai.entities.UserEntity;
import fr.fgroup.chatai.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarikfamil@gmail.com)
 * <p>
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 05/03/2023
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDaoServiceImpl implements UserDaoService {

  private final UserRepository repository;

  @Override
  public UserEntity save(UserEntity entity) {
    return repository.save(entity);
  }

  @Override
  public void delete(UserEntity entity) {
    repository.delete(entity);
  }

  @Override
  public UserEntity findOne(Specification<UserEntity> specification) {
    return repository.findOne(specification).orElseThrow(() -> {
      log.debug("Couldn't find any user with the specified criteria");
      return new ResourceNotFoundException("No configuration found with the specified criteria");
    });
  }

  @Override
  public Page<UserEntity> findAll(Specification<UserEntity> specification, Pageable pageable) {
    return repository.findAll(specification, pageable);
  }
}
