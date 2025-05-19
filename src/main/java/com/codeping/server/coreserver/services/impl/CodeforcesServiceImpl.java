package com.codeping.server.coreserver.services.impl;

import com.codeping.server.coreserver.mappers.CodeforcesMapper;
import com.codeping.server.coreserver.models.Contest;
import com.codeping.server.coreserver.models.ContestHistory;
import com.codeping.server.coreserver.models.Submission;
import com.codeping.server.coreserver.models.UserProfile;
import com.codeping.server.coreserver.services.CodeforcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeforcesServiceImpl implements CodeforcesService {

    @Qualifier("codeforcesWebClient")
    private final WebClient codeforcesWebClient;
    private final CodeforcesMapper codeforcesMapper;

    @Override
    @Cacheable(value = "userProfiles", key = "#handle", unless = "#result == null")
    public UserProfile getCodeforcesUserInfo(String handle) {
        log.info("Fetching Codeforces user info for handle: {}", handle);
        return codeforcesWebClient.get()
                .uri("/user.info?handles=" + handle)
                .retrieve()
                .bodyToMono(String.class)
                .map(codeforcesMapper::mapToUserProfile)
                .doOnError(
                        e -> log.error("Error fetching Codeforces user info for handle {}: {}", handle, e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "contestHistory", key = "#handle", unless = "#result == null")
    public List<ContestHistory> getCodeforcesUserRating(String handle) {
        log.info("Fetching Codeforces user rating for handle: {}", handle);
        return codeforcesWebClient.get()
                .uri("/user.rating?handle=" + handle)
                .retrieve()
                .bodyToMono(String.class)
                .map(codeforcesMapper::mapToContestHistory)
                .doOnError(e -> log.error("Error fetching Codeforces user rating for handle {}: {}", handle,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "recentSubmissions", key = "#handle", unless = "#result == null")
    public List<Submission> getCodeforcesRecentSubmissions(String handle) {
        log.info("Fetching Codeforces recent submissions for handle: {}", handle);
        return codeforcesWebClient.get()
                .uri("/user.status?handle=" + handle)
                .retrieve()
                .bodyToMono(String.class)
                .map(codeforcesMapper::mapToSubmissions)
                .doOnError(e -> log.error("Error fetching Codeforces recent submissions for handle {}: {}", handle,
                        e.getMessage()))
                .block();
    }

    @Override
    @Cacheable(value = "upcomingContests", key = "'codeforces'", unless = "#result == null")
    public List<Contest> getCodeforcesUpcomingContests() {
        log.info("Fetching Codeforces upcoming contests");
        return codeforcesWebClient.get()
                .uri("/contest.list?gym=false")
                .retrieve()
                .bodyToMono(String.class)
                .map(codeforcesMapper::mapToContests)
                .doOnError(e -> log.error("Error fetching Codeforces upcoming contests: {}", e.getMessage()))
                .block();
    }

    // Cache eviction methods for manual refresh
    @CacheEvict(value = "userProfiles", key = "#handle")
    public void evictUserProfileCache(String handle) {
        log.info("Evicting Codeforces user profile cache for handle: {}", handle);
    }

    @CacheEvict(value = "contestHistory", key = "#handle")
    public void evictContestHistoryCache(String handle) {
        log.info("Evicting Codeforces contest history cache for handle: {}", handle);
    }

    @CacheEvict(value = "recentSubmissions", key = "#handle")
    public void evictRecentSubmissionsCache(String handle) {
        log.info("Evicting Codeforces recent submissions cache for handle: {}", handle);
    }

    @CacheEvict(value = "upcomingContests", key = "'codeforces'")
    public void evictUpcomingContestsCache() {
        log.info("Evicting Codeforces upcoming contests cache");
    }
}