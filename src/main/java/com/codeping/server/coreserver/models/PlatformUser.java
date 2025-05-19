package com.codeping.server.coreserver.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "platform_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"platform_id", "user_id"}),
        @UniqueConstraint(columnNames = {"platform_id", "username"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CPUsers user;

    @Column(nullable = false)
    private String username;
}