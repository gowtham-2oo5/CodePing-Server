package com.codeping.server.coreserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "platform_user_performance_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformUserPerformanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private CPUsers user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rating_graph_id", referencedColumnName = "id")
    private RatingGraph ratingGraph;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recent_subs_id", referencedColumnName = "id")
    private RecentSubmission recentSubmission;
}
