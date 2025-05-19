package com.codeping.server.coreserver.services.impl;

import com.codeping.server.coreserver.mappers.LeetCodeMapper;
import com.codeping.server.coreserver.models.Contest;
import com.codeping.server.coreserver.models.ContestHistory;
import com.codeping.server.coreserver.models.Submission;
import com.codeping.server.coreserver.models.UserProfile;
import com.codeping.server.coreserver.services.LeetCodeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeetCodeServiceImpl implements LeetCodeService {

    private final RestTemplate restTemplate;
    private final Bucket rateLimiter;
    private final LeetCodeMapper leetCodeMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private String executeGraphQLQuery(String query, Map<String, Object> variables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "query", query,
                "variables", variables);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                LEETCODE_BASE_URL,
                HttpMethod.POST,
                request,
                String.class);

        return response.getBody();
    }

    private String filterContestHistory(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.get("data");
            JsonNode history = data.get("userContestRankingHistory");

            if (!history.isArray()) {
                return response;
            }

            List<JsonNode> filteredHistory = new ArrayList<>();
            boolean foundFirstAttended = false;
            JsonNode previousContest = null;

            for (JsonNode contest : history) {
                if (!foundFirstAttended) {
                    if (contest.get("attended").asBoolean()) {
                        // Add the previous contest if it exists
                        if (previousContest != null) {
                            filteredHistory.add(previousContest);
                        }
                        filteredHistory.add(contest);
                        foundFirstAttended = true;
                    } else {
                        previousContest = contest;
                    }
                } else if (contest.get("attended").asBoolean()) {
                    filteredHistory.add(contest);
                }
            }

            Map<String, Object> newData = new HashMap<>();
            newData.put("userContestRankingHistory", filteredHistory);

            Map<String, Object> newRoot = new HashMap<>();
            newRoot.put("data", newData);

            return objectMapper.writeValueAsString(newRoot);
        } catch (Exception e) {
            log.error("Error filtering contest history: {}", e.getMessage());
            return response;
        }
    }
}