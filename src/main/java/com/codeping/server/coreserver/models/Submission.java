package com.codeping.server.coreserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private String problemName;
    private String problemUrl;
    private String language;
    private Long timestamp;
    private String verdict; // For CodeForces
    private Integer rating; // For CodeForces
    private List<String> tags; // For CodeForces
    private Platform platform;
}