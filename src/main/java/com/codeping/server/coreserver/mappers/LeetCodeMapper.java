package com.codeping.server.coreserver.mappers;

import com.codeping.server.coreserver.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeetCodeMapper {
    private final ObjectMapper objectMapper;

    public UserProfile mapToUserProfile(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode matchedUser = root.get("data").get("matchedUser");
            JsonNode profile = matchedUser.get("profile");

            return UserProfile.builder()
                    .username(matchedUser.get("username").asText())
                    .realName(profile.get("realName").asText())
                    .profileImage(profile.get("userAvatar").asText())
                    .countryName(profile.get("countryName").asText())
                    .globalRank(profile.get("ranking").asInt())
                    .platform(Platform.LEETCODE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping LeetCode profile response", e);
        }
    }

    public List<ContestHistory> mapToContestHistory(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode history = root.get("data").get("userContestRankingHistory");
            List<ContestHistory> result = new ArrayList<>();

            for (JsonNode contest : history) {
                if (contest.get("attended").asBoolean()) {
                    result.add(ContestHistory.builder()
                            .contestName(contest.get("contest").get("title").asText())
                            .startTime(contest.get("contest").get("startTime").asLong())
                            .attended(true)
                            .newRating(contest.get("rating").asInt())
                            .rank(contest.get("ranking").asInt())
                            .platform(Platform.LEETCODE)
                            .build());
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping LeetCode contest history response", e);
        }
    }

    public List<Submission> mapToSubmissions(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode submissions = root.get("data").get("recentAcSubmissionList");
            List<Submission> result = new ArrayList<>();

            for (JsonNode submission : submissions) {
                result.add(Submission.builder()
                        .problemName(submission.get("title").asText())
                        .problemUrl("https://leetcode.com/problems/" + submission.get("titleSlug").asText())
                        .language(submission.get("lang").asText())
                        .timestamp(Long.parseLong(submission.get("timestamp").asText()))
                        .platform(Platform.LEETCODE)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping LeetCode submissions response", e);
        }
    }

    public List<Contest> mapToContests(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode contests = root.get("data").get("upcomingContests");
            List<Contest> result = new ArrayList<>();

            for (JsonNode contest : contests) {
                Long startTimeSeconds = contest.get("startTime").asLong();
                String formattedStartTime = formatStartTime(startTimeSeconds);

                result.add(Contest.builder()
                        .title(contest.get("title").asText())
                        .titleSlug(contest.get("titleSlug").asText())
                        .startTime(formattedStartTime)
                        .duration(contest.get("duration").asInt())
                        .isVirtual(contest.get("isVirtual").asBoolean())
                        .platform(Platform.LEETCODE)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping LeetCode contests response", e);
        }
    }

    private String formatStartTime(Long startTimeSeconds) {
        long now = System.currentTimeMillis() / 1000;
        long diff = startTimeSeconds - now;

        long days = diff / (24 * 3600);
        long hours = (diff % (24 * 3600)) / 3600;

        return String.format("%d Days %d Hrs", days, hours);
    }
}