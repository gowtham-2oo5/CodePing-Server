package com.codeping.server.coreserver.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating_graphs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingGraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CPUsers user;

    @Column(nullable = false)
    private String contest;

    @Column(name = "rating_before", nullable = false)
    private Integer ratingBefore;

    @Column(name = "rating_after", nullable = false)
    private Integer ratingAfter;

    @Column(length = 1000)
    private String msg;
}