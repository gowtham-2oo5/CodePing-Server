package com.codeping.server.coreserver.services.impl;

import com.codeping.server.coreserver.mappers.CodeChefMapper;
import com.codeping.server.coreserver.models.*;
import com.codeping.server.coreserver.services.CodeChefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeChefServiceImpl implements CodeChefService {

    @Qualifier("codeChefWebClient")
    private final WebClient codeChefWebClient;
    private final CodeChefMapper codeChefMapper;

    @Override
    @Cacheable(value = "userProfiles", key = "#username", unless = "#result == null")
    public UserProfile getCodechefProfile(String username) {
        log.info("Fetching CodeChef profile for user: {}", username);
        return codeChefWebClient.get()
                .uri("/users/" + username)
                .retrieve()
                .bodyToMono(String.class)
                .map(codeChefMapper::mapToUserProfile)
                .doOnError(e -> log.error("Error fetching CodeChef profile for user {}: {}", username, e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "contestHistory", key = "#username", unless = "#result == null")
    public List<ContestHistory> getCodechefRatingsGraph(String username) {
        log.info("Fetching CodeChef ratings graph for user: {}", username);
        return codeChefWebClient.get()
                .uri("/users/" + username + "/ratings")
                .retrieve()
                .bodyToMono(String.class)
                .map(codeChefMapper::mapToContestHistory)
                .doOnError(e -> log.error("Error fetching CodeChef ratings graph for user {}: {}", username,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "recentSubmissions", key = "#username", unless = "#result == null")
    public List<Submission> getCodechefRecentSubmissions(String username) {
        log.info("Fetching CodeChef recent submissions for user: {}", username);
        return codeChefWebClient.get()
                .uri("/users/" + username + "/recent-submissions")
                .retrieve()
                .bodyToMono(String.class)
                .map(codeChefMapper::mapToSubmissions)
                .doOnError(e -> log.error("Error fetching CodeChef recent submissions for user {}: {}", username,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "contestHistory", key = "#username + '_whole'", unless = "#result == null")
    public List<ContestHistory> getCodechefWholeData(String username) {
        log.info("Fetching CodeChef whole data for user: {}", username);
        return codeChefWebClient.get()
                .uri("/users/" + username + "/whole-data")
                .retrieve()
                .bodyToMono(String.class)
                .map(codeChefMapper::mapToContestHistory)
                .doOnError(
                        e -> log.error("Error fetching CodeChef whole data for user {}: {}", username, e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "upcomingContests", key = "'codechef'", unless = "#result == null")
    public List<Contest> getCodechefUpcomingContests() {
        log.info("Fetching CodeChef upcoming contests");
        return codeChefWebClient.get()
                .uri("/api/upcoming")
                .retrieve()
                .bodyToMono(String.class)
                .map(codeChefMapper::mapToContests)
                .doOnError(e -> log.error("Error fetching CodeChef upcoming contests: {}", e.getMessage()))
                .block();
    }

    // Cache eviction methods for manual refresh
    @CacheEvict(value = "userProfiles", key = "#username")
    public void evictUserProfileCache(String username) {
        log.info("Evicting CodeChef user profile cache for user: {}", username);
    }

    @CacheEvict(value = "contestHistory", key = "#username")
    public void evictContestHistoryCache(String username) {
        log.info("Evicting CodeChef contest history cache for user: {}", username);
    }

    @CacheEvict(value = "recentSubmissions", key = "#username")
    public void evictRecentSubmissionsCache(String username) {
        log.info("Evicting CodeChef recent submissions cache for user: {}", username);
    }

    @CacheEvict(value = "contestHistory", key = "#username + '_whole'")
    public void evictWholeDataCache(String username) {
        log.info("Evicting CodeChef whole data cache for user: {}", username);
    }

    @CacheEvict(value = "upcomingContests", key = "'codechef'")
    public void evictUpcomingContestsCache() {
        log.info("Evicting CodeChef upcoming contests cache");
    }
}