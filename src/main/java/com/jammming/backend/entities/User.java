package com.jammming.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCESS_TOKEN", length = 512)
    private String accessToken;

    @Column(name = "EXPIRES_IN")
    private Integer expiresIn;

    @Column(name = "REFRESH_TOKEN", length = 512)
    private String refreshToken;

    // the time in seconds when token expires
    @Column(name = "EXPIRE_TIME")
    private Long expireTime;

}
