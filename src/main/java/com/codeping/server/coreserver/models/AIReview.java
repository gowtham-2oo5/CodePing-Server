package com.codeping.server.coreserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CPUsers user;

    @Column(nullable = false, length = 5000)
    private String msg;
}