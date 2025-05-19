package com.codeping.server.coreserver.controllers;

import com.codeping.server.coreserver.models.*;
import com.codeping.server.coreserver.services.LeetCodeService;
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
@RequestMapping("/api/leetcode")
@RequiredArgsConstructor
@Tag(name = "LeetCode Integration", description = "APIs for LeetCode platform integration")
public class LeetCodeController {

        private final LeetCodeService leetCodeService;

        @GetMapping("/profile/{username}")
        @Operation(summary = "Get LeetCode profile", description = "Retrieves a user's LeetCode profile")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Profile not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
                log.info("Fetching LeetCode profile for user: {}", username);
                return ResponseEntity.ok(leetCodeService.fetchLeetCodeUserProfile(username));
        }

        @GetMapping("/contest-history/{username}")
        @Operation(summary = "Get contest history", description = "Retrieves a user's LeetCode contest history")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contest history retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Contest history not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<ContestHistory>> getContestHistory(@PathVariable String username) {
                log.info("Fetching LeetCode contest history for user: {}", username);
                return ResponseEntity.ok(leetCodeService.fetchLeetCodeContestHistory(username));
        }

        @GetMapping("/recent-submissions/{username}")
        @Operation(summary = "Get recent submissions", description = "Retrieves a user's recent LeetCode submissions")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Submissions not found"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Submission>> getRecentSubmissions(@PathVariable String username) {
                log.info("Fetching LeetCode recent submissions for user: {}", username);
                return ResponseEntity.ok(leetCodeService.fetchLeetCodeRecentSubmissions(username));
        }

        @GetMapping("/upcoming-contests")
        @Operation(summary = "Get upcoming contests", description = "Retrieves upcoming LeetCode contests")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Upcoming contests retrieved successfully"),
                        @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
        })
        public ResponseEntity<List<Contest>> getUpcomingContests() {
                log.info("Fetching LeetCode upcoming contests");
                return ResponseEntity.ok(leetCodeService.fetchLeetCodeUpcomingContests());
        }

        // Cache refresh endpoints
        @PostMapping("/refresh/profile/{username}")
        public ResponseEntity<Void> refreshUserProfile(@PathVariable String username) {
                log.info("Manually refreshing LeetCode profile for user: {}", username);
                leetCodeService.evictUserProfileCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/contest-history/{username}")
        public ResponseEntity<Void> refreshContestHistory(@PathVariable String username) {
                log.info("Manually refreshing LeetCode contest history for user: {}", username);
                leetCodeService.evictContestHistoryCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/recent-submissions/{username}")
        public ResponseEntity<Void> refreshRecentSubmissions(@PathVariable String username) {
                log.info("Manually refreshing LeetCode recent submissions for user: {}", username);
                leetCodeService.evictRecentSubmissionsCache(username);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/refresh/upcoming-contests")
        public ResponseEntity<Void> refreshUpcomingContests() {
                log.info("Manually refreshing LeetCode upcoming contests");
                leetCodeService.evictUpcomingContestsCache();
                return ResponseEntity.ok().build();
        }
}