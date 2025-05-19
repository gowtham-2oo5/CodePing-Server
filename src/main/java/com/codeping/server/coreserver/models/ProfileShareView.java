package com.codeping.server.coreserver.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile_share_views")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileShareView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private CPUsers user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_share_view_id")
    @Builder.Default
    private List<PlatformUserPerformanceData> platformUserGraphs = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ai_review_id", referencedColumnName = "id")
    private AIReview aiReview;
}