package com.codeping.server.coreserver.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recent_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CPUsers user;

    @Column(nullable = false)
    private String problem;

    @Column(name = "problem_link", nullable = false)
    private String problemLink;
}