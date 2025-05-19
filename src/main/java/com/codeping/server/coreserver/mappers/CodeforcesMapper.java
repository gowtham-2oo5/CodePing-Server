package com.codeping.server.coreserver.mappers;

import com.codeping.server.coreserver.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class CodeforcesMapper {
    private final ObjectMapper objectMapper;

    public UserProfile mapToUserProfile(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode user = root.get("result").get(0);

            return UserProfile.builder()
                    .username(user.get("handle").asText())
                    .profileImage(user.get("avatar").asText())
                    .currentRating(user.get("rating").asInt())
                    .highestRating(user.get("maxRating").asInt())
                    .rank(user.get("rank").asText())
                    .platform(Platform.CODEFORCES)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Codeforces profile response", e);
        }
    }

    public List<ContestHistory> mapToContestHistory(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode contests = root.get("result");
            List<ContestHistory> result = new ArrayList<>();

            for (JsonNode contest : contests) {
                result.add(ContestHistory.builder()
                        .contestName(contest.get("contestName").asText())
                        .startTime(contest.get("ratingUpdateTimeSeconds").asLong())
                        .oldRating(contest.get("oldRating").asInt())
                        .newRating(contest.get("newRating").asInt())
                        .rank(contest.get("rank").asInt())
                        .platform(Platform.CODEFORCES)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Codeforces contest history response", e);
        }
    }

    public List<Submission> mapToSubmissions(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode submissions = root.get("result");
            List<Submission> result = new ArrayList<>();

            for (JsonNode submission : submissions) {
                JsonNode problem = submission.get("problem");
                result.add(Submission.builder()
                        .problemName(problem.get("name").asText())
                        .problemUrl("https://codeforces.com/problemset/problem/" +
                                problem.get("contestId").asText() + "/" +
                                problem.get("index").asText())
                        .language(submission.get("programmingLanguage").asText())
                        .timestamp(submission.get("creationTimeSeconds").asLong())
                        .verdict(submission.get("verdict").asText())
                        .rating(problem.has("rating") ? problem.get("rating").asInt() : null)
                        .tags(problem.has("tags") ? StreamSupport.stream(problem.get("tags").spliterator(), false)
                                .map(JsonNode::asText)
                                .collect(Collectors.toList()) : null)
                        .platform(Platform.CODEFORCES)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Codeforces submissions response", e);
        }
    }

    public List<Contest> mapToContests(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode contests = root.get("result");
            List<Contest> result = new ArrayList<>();

            for (JsonNode contest : contests) {
                if ("BEFORE".equals(contest.get("phase").asText())) {
                    Long startTimeSeconds = contest.get("startTimeSeconds").asLong();
                    String formattedStartTime = formatStartTime(startTimeSeconds);

                    result.add(Contest.builder()
                            .title(contest.get("name").asText())
                            .startTime(formattedStartTime)
                            .duration(contest.get("durationSeconds").asInt())
                            .type(contest.get("type").asText())
                            .phase(contest.get("phase").asText())
                            .platform(Platform.CODEFORCES)
                            .build());
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Codeforces contests response", e);
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