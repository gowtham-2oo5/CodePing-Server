package com.codeping.server.coreserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    private String title;
    private String titleSlug; // For LeetCode
    private String startTime; // Changed from Long to String to support formats like "1 Days 20 Hrs"
    private Integer duration;
    private String type; // For CodeForces
    private String phase; // For CodeForces
    private Boolean isVirtual; // For LeetCode
    private Platform platform;
}