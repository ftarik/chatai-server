package fr.fgroup.chatai.entities.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 *
 * Copyright (C) FTarik, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 *
 * Created 17/06/2020 17:12
 */

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity extends AbstractBaseEntity {

    /**
     * Creation date of the Entity in the database
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * The Creator of the Entity in the database.
     * It will be the connected user or the System if the operation is launched by the system.
     */
    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    /**
     * Last Modification date of the Entity in the database
     */
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    /**
     * The profile responsable of the last modification of the Entity in the database
     */
    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

}
