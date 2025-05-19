package com.codeping.server.coreserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String username;
    private String realName;
    private String profileImage;
    private String countryName;
    private Integer currentRating;
    private Integer highestRating;
    private String rank; // e.g., "newbie", "2★", etc.
    private Integer globalRank;
    private Integer countryRank;
    private Platform platform;
}