package com.codeping.server.coreserver.controllers;

import com.codeping.server.coreserver.models.*;
import com.codeping.server.coreserver.services.CodeChefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/codechef")
@RequiredArgsConstructor
@Tag(name = "CodeChef Integration", description = "APIs for CodeChef platform integration")
public class CodeChefController {

        private final CodeChefService codeChefService;

        @GetMapping("/profile/{username}")
        @Operation(summary = "Get CodeChef profile", description = "Retrieves a user's CodeChef profile")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Profile not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
                log.info("Fetching CodeChef profile for user: {}", username);
                return ResponseEntity.ok(codeChefService.getCodechefProfile(username));
        }

        @GetMapping("/ratings/{username}")
        @Operation(summary = "Get CodeChef ratings graph", description = "Retrieves a user's CodeChef ratings graph")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Ratings graph retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Ratings not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<ContestHistory>> getRatingsGraph(@PathVariable String username) {
                log.info("Fetching CodeChef ratings graph for user: {}", username);
                return ResponseEntity.ok(codeChefService.getCodechefRatingsGraph(username));
        }

        @GetMapping("/recent-submissions/{username}")
        @Operation(summary = "Get recent submissions", description = "Retrieves a user's recent CodeChef submissions")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Submissions not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Submission>> getRecentSubmissions(@PathVariable String username) {
                log.info("Fetching CodeChef recent submissions for user: {}", username);
                return ResponseEntity.ok(codeChefService.getCodechefRecentSubmissions(username));
        }

        @GetMapping("/whole-data/{username}")
        @Operation(summary = "Get complete user data", description = "Retrieves all CodeChef data for a user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Data retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Data not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<ContestHistory>> getWholeData(@PathVariable String username) {
                log.info("Fetching CodeChef whole data for user: {}", username);
                return ResponseEntity.ok(codeChefService.getCodechefWholeData(username));
        }

        @GetMapping("/upcoming-contests")
        @Operation(summary = "Get upcoming contests", description = "Retrieves upcoming CodeChef contests")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Upcoming contests retrieved successfully"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Contest>> getUpcomingContests() {
                log.info("Fetching CodeChef upcoming contests");
                return ResponseEntity.ok(codeChefService.getCodechefUpcomingContests());
        }

        // Cache refresh endpoints
        @PostMapping("/refresh/profile/{username}")
        public ResponseEntity<Void> refreshUserProfile(@PathVariable String username) {
                log.info("Manually refreshing CodeChef profile for user: {}", username);
                codeChefService.evictUserProfileCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/ratings/{username}")
        public ResponseEntity<Void> refreshRatingsGraph(@PathVariable String username) {
                log.info("Manually refreshing CodeChef ratings graph for user: {}", username);
                codeChefService.evictContestHistoryCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/recent-submissions/{username}")
        public ResponseEntity<Void> refreshRecentSubmissions(@PathVariable String username) {
                log.info("Manually refreshing CodeChef recent submissions for user: {}", username);
                codeChefService.evictRecentSubmissionsCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/whole-data/{username}")
        public ResponseEntity<Void> refreshWholeData(@PathVariable String username) {
                log.info("Manually refreshing CodeChef whole data for user: {}", username);
                codeChefService.evictWholeDataCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/upcoming-contests")
        public ResponseEntity<Void> refreshUpcomingContests() {
                log.info("Manually refreshing CodeChef upcoming contests");
                codeChefService.evictUpcomingContestsCache();
                return ResponseEntity.ok().build();
        }
}