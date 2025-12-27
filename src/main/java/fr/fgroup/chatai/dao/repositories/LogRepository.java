package fr.fgroup.chatai.dao.repositories;

import fr.fgroup.chatai.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long>,
        JpaSpecificationExecutor<LogEntity> {
}
