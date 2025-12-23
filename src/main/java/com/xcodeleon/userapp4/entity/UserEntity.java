package com.xcodeleon.userapp4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;




@Data
@Entity(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private String login;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "is_deleted")
    private Boolean deleted;
}
