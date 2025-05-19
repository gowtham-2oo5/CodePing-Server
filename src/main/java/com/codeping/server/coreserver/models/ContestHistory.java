package com.codeping.server.coreserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestHistory {
    private String contestName;
    private String contestCode; // For CodeChef
    private Long startTime;
    private Integer duration;
    private Boolean attended;
    private Integer oldRating;
    private Integer newRating;
    private Integer rank;
    private String division; // For CodeChef
    private String color; // For CodeChef
    private Platform platform;
}