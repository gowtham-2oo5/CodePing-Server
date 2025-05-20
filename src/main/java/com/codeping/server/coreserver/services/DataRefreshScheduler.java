package com.codeping.server.coreserver.services;

import com.codeping.server.coreserver.repos.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataRefreshScheduler {

    private final LeetCodeService leetCodeService;
    private final CodeChefService codeChefService;
    private final CodeforcesService codeforcesService;
    private final PlatformUserRepository platformUserRepository;
    private final CacheManager cacheManager;

    // Refresh user profiles every 6 hours
    @Scheduled(fixedRate = 21600000) // 6 hours in milliseconds
    public void refreshUserProfiles() {
        log.info("Starting scheduled refresh of user profiles");
        try {
            List<String> usernames = platformUserRepository.findAllUsernames();
            for (String username : usernames) {
                try {
                    // Refresh LeetCode profile
                    leetCodeService.fetchLeetCodeUserProfile(username);
                    // Refresh CodeChef profile
                    codeChefService.getCodechefProfile(username);
                    // Refresh Codeforces profile
                    codeforcesService.getCodeforcesUserInfo(username);
                } catch (Exception e) {
                    log.error("Error refreshing profile for user {}: {}", username, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in user profiles refresh scheduler: {}", e.getMessage());
        }
    }

    // Refresh contest history every 2 hours
    @Scheduled(fixedRate = 7200000) // 2 hours in milliseconds
    public void refreshContestHistory() {
        log.info("Starting scheduled refresh of contest history");
        try {
            List<String> usernames = platformUserRepository.findAllUsernames();
            for (String username : usernames) {
                try {
                    // Refresh LeetCode contest history
                    leetCodeService.fetchLeetCodeContestHistory(username);
                    // Refresh CodeChef contest history
                    codeChefService.getCodechefRatingsGraph(username);
                    // Refresh Codeforces contest history
                    codeforcesService.getCodeforcesUserRating(username);
                } catch (Exception e) {
                    log.error("Error refreshing contest history for user {}: {}", username, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in contest history refresh scheduler: {}", e.getMessage());
        }
    }

    // Refresh recent submissions every 15 minutes
    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void refreshRecentSubmissions() {
        log.info("Starting scheduled refresh of recent submissions");
        try {
            List<String> usernames = platformUserRepository.findAllUsernames();
            for (String username : usernames) {
                try {
                    // Refresh LeetCode submissions
                    leetCodeService.fetchLeetCodeRecentSubmissions(username);
                    // Refresh CodeChef submissions
                    codeChefService.getCodechefRecentSubmissions(username);
                    // Refresh Codeforces submissions
                    codeforcesService.getCodeforcesRecentSubmissions(username);
                } catch (Exception e) {
                    log.error("Error refreshing recent submissions for user {}: {}", username, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in recent submissions refresh scheduler: {}", e.getMessage());
        }
    }

    // Refresh upcoming contests every hour
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void refreshUpcomingContests() {
        log.info("Starting scheduled refresh of upcoming contests");
        try {
            // Refresh LeetCode contests
            leetCodeService.fetchLeetCodeUpcomingContests();
            // Refresh Codeforces contests
            codeforcesService.getCodeforcesUpcomingContests();
        } catch (Exception e) {
            log.error("Error in upcoming contests refresh scheduler: {}", e.getMessage());
        }
    }

    // Clear all caches at midnight every Monday
    @Scheduled(cron = "0 0 0 * * MON")
    public void clearAllCaches() {
        log.info("Starting scheduled cache clear");
        try {
            cacheManager.getCacheNames().forEach(cacheName -> {
                cacheManager.getCache(cacheName).clear();
                log.info("Cleared cache: {}", cacheName);
            });
        } catch (Exception e) {
            log.error("Error clearing caches: {}", e.getMessage());
        }
    }
}