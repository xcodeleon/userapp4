package com.xcodeleon.userapp4.repository;

import com.xcodeleon.userapp4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Modifying
    @Query("UPDATE users u SET u.deleted = true WHERE u.id = :uuid")
    void setDeleted(UUID uuid);

}
