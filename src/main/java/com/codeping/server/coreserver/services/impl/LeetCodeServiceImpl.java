package com.codeping.server.coreserver.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.codeping.server.coreserver.mappers.LeetCodeMapper;
import com.codeping.server.coreserver.models.Contest;
import com.codeping.server.coreserver.models.ContestHistory;
import com.codeping.server.coreserver.models.Submission;
import com.codeping.server.coreserver.models.UserProfile;
import com.codeping.server.coreserver.services.LeetCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeetCodeServiceImpl implements LeetCodeService {

    private final LeetCodeMapper leetCodeMapper;

    @Qualifier("leetCodeWebClient")
    private final WebClient leetCodeWebClient;

    @Override
    @Cacheable(value = "userProfiles", key = "#username", unless = "#result == null")
    public UserProfile fetchLeetCodeUserProfile(String username) {
        log.info("Fetching LeetCode profile for user: {}", username);
        Map<String, Object> variables = Map.of("username", username);
        return leetCodeWebClient.post()
                .bodyValue(Map.of("query", LEETCODE_QUERY_USER_PROFILE, "variables", variables))
                .retrieve()
                .bodyToMono(String.class)
                .map(leetCodeMapper::mapToUserProfile)
                .doOnError(e -> log.error("Error fetching LeetCode profile for user {}: {}", username, e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "contestHistory", key = "#username", unless = "#result == null")
    public List<ContestHistory> fetchLeetCodeContestHistory(String username) {
        log.info("Fetching LeetCode contest history for user: {}", username);
        Map<String, Object> variables = Map.of("username", username);
        return leetCodeWebClient.post()
                .bodyValue(Map.of("query", LEETCODE_QUERY_CONTEST_HISTORY, "variables", variables))
                .retrieve()
                .bodyToMono(String.class)
                .map(leetCodeMapper::mapToContestHistory)
                .doOnError(e -> log.error("Error fetching LeetCode contest history for user {}: {}", username,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "recentSubmissions", key = "#username", unless = "#result == null")
    public List<Submission> fetchLeetCodeRecentSubmissions(String username) {
        log.info("Fetching LeetCode recent submissions for user: {}", username);
        Map<String, Object> variables = Map.of("username", username);
        return leetCodeWebClient.post()
                .bodyValue(Map.of("query", LEETCODE_QUERY_RECENT_SUBMISSIONS, "variables", variables))
                .retrieve()
                .bodyToMono(String.class)
                .map(leetCodeMapper::mapToSubmissions)
                .doOnError(e -> log.error("Error fetching LeetCode recent submissions for user {}: {}", username,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "upcomingContests", key = "'leetcode'", unless = "#result == null")
    public List<Contest> fetchLeetCodeUpcomingContests() {
        log.info("Fetching LeetCode upcoming contests");
        return leetCodeWebClient.post()
                .bodyValue(Map.of("query", LEETCODE_QUERY_UPCOMING_CONTESTS))
                .retrieve()
                .bodyToMono(String.class)
                .map(leetCodeMapper::mapToContests)
                .doOnError(e -> log.error("Error fetching LeetCode upcoming contests: {}", e.getMessage()))
                .block();
    }

    // Cache eviction methods for manual refresh
    @CacheEvict(value = "userProfiles", key = "#username")
    public void evictUserProfileCache(String username) {
        log.info("Evicting LeetCode user profile cache for user: {}", username);
    }

    @CacheEvict(value = "contestHistory", key = "#username")
    public void evictContestHistoryCache(String username) {
        log.info("Evicting LeetCode contest history cache for user: {}", username);
    }

    @CacheEvict(value = "recentSubmissions", key = "#username")
    public void evictRecentSubmissionsCache(String username) {
        log.info("Evicting LeetCode recent submissions cache for user: {}", username);
    }

    @CacheEvict(value = "upcomingContests", key = "'leetcode'")
    public void evictUpcomingContestsCache() {
        log.info("Evicting LeetCode upcoming contests cache");
    }
}