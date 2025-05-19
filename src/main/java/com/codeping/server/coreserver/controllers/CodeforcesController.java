package com.codeping.server.coreserver.controllers;

import com.codeping.server.coreserver.models.*;
import com.codeping.server.coreserver.services.CodeforcesService;
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
@RequestMapping("/api/codeforces")
@RequiredArgsConstructor
@Tag(name = "Codeforces Integration", description = "APIs for Codeforces platform integration")
public class CodeforcesController {

        private final CodeforcesService codeforcesService;

        @GetMapping("/profile/{handle}")
        @Operation(summary = "Get user profile", description = "Retrieves a user's Codeforces profile")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<UserProfile> getUserProfile(@PathVariable String handle) {
                log.info("Fetching Codeforces profile for handle: {}", handle);
                return ResponseEntity.ok(codeforcesService.getCodeforcesUserInfo(handle));
        }

        @GetMapping("/rating/{handle}")
        @Operation(summary = "Get user rating", description = "Retrieves a user's Codeforces rating history")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User rating retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "User rating not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<ContestHistory>> getUserRating(@PathVariable String handle) {
                log.info("Fetching Codeforces rating for handle: {}", handle);
                return ResponseEntity.ok(codeforcesService.getCodeforcesUserRating(handle));
        }

        @GetMapping("/recent-submissions/{handle}")
        @Operation(summary = "Get recent submissions", description = "Retrieves a user's recent Codeforces submissions")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Submissions not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Submission>> getRecentSubmissions(@PathVariable String handle) {
                log.info("Fetching Codeforces recent submissions for handle: {}", handle);
                return ResponseEntity.ok(codeforcesService.getCodeforcesRecentSubmissions(handle));
        }

        @GetMapping("/upcoming-contests")
        @Operation(summary = "Get upcoming contests", description = "Retrieves upcoming Codeforces contests")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Upcoming contests retrieved successfully"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Contest>> getUpcomingContests() {
                log.info("Fetching Codeforces upcoming contests");
                return ResponseEntity.ok(codeforcesService.getCodeforcesUpcomingContests());
        }

        // Cache refresh endpoints
        @PostMapping("/refresh/profile/{handle}")
        public ResponseEntity<Void> refreshUserProfile(@PathVariable String handle) {
                log.info("Manually refreshing Codeforces profile for handle: {}", handle);
                codeforcesService.evictUserProfileCache(handle);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/rating/{handle}")
        public ResponseEntity<Void> refreshUserRating(@PathVariable String handle) {
                log.info("Manually refreshing Codeforces rating for handle: {}", handle);
                codeforcesService.evictContestHistoryCache(handle);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/recent-submissions/{handle}")
        public ResponseEntity<Void> refreshRecentSubmissions(@PathVariable String handle) {
                log.info("Manually refreshing Codeforces recent submissions for handle: {}", handle);
                codeforcesService.evictRecentSubmissionsCache(handle);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/upcoming-contests")
        public ResponseEntity<Void> refreshUpcomingContests() {
                log.info("Manually refreshing Codeforces upcoming contests");
                codeforcesService.evictUpcomingContestsCache();
                return ResponseEntity.ok().build();
        }
}