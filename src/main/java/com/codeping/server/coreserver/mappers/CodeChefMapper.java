package com.codeping.server.coreserver.mappers;

import com.codeping.server.coreserver.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeChefMapper {
    private final ObjectMapper objectMapper;

    public UserProfile mapToUserProfile(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);

            return UserProfile.builder()
                    .username(root.get("handle").asText())
                    .realName(root.get("name").asText())
                    .profileImage(root.get("profileImage").asText())
                    .currentRating(root.get("currentRating").asInt())
                    .highestRating(root.get("highestRating").asInt())
                    .rank(root.get("stars").asText())
                    .globalRank(root.get("globalRank").asInt())
                    .countryRank(root.get("countryRank").asInt())
                    .countryName(root.get("countryName").asText())
                    .platform(Platform.CODECHEF)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping CodeChef profile response", e);
        }
    }

    public List<ContestHistory> mapToContestHistory(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode ratingData = root.get("ratingData");
            List<ContestHistory> result = new ArrayList<>();

            for (JsonNode contest : ratingData) {
                result.add(ContestHistory.builder()
                        .contestName(contest.get("name").asText())
                        .contestCode(contest.get("code").asText())
                        .startTime(parseDateTime(contest.get("getyear").asText(),
                                contest.get("getmonth").asText(),
                                contest.get("getday").asText()))
                        .newRating(Integer.parseInt(contest.get("rating").asText()))
                        .rank(Integer.parseInt(contest.get("rank").asText()))
                        .division(contest.get("name").asText().contains("Division")
                                ? contest.get("name").asText().split("Division")[1].trim()
                                : null)
                        .color(contest.get("color").asText())
                        .platform(Platform.CODECHEF)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping CodeChef contest history response", e);
        }
    }

    public List<Submission> mapToSubmissions(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode submissions = root.get("recentActivity");
            List<Submission> result = new ArrayList<>();

            for (JsonNode submission : submissions) {
                result.add(Submission.builder()
                        .problemName(submission.get("problem_name").asText())
                        .problemUrl(submission.get("problem_url").asText())
                        .language(submission.get("language").asText())
                        .platform(Platform.CODECHEF)
                        .build());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping CodeChef submissions response", e);
        }
    }

    public List<Contest> mapToContests(String response) {
        try {
            log.debug("Starting to map CodeChef contests response: {}", response);
            JsonNode root = objectMapper.readTree(response);
            log.debug("Parsed JSON root: {}", root);

            if (!root.get("success").asBoolean()) {
                log.error("CodeChef API returned unsuccessful response");
                throw new RuntimeException("CodeChef API returned unsuccessful response");
            }

            JsonNode contests = root.get("upcomingContests");
            log.debug("Found {} upcoming contests", contests.size());
            List<Contest> result = new ArrayList<>();

            for (JsonNode contest : contests) {
                log.debug("Processing contest: {}", contest);
                try {
                    String name = contest.get("name").asText();
                    String code = contest.get("code").asText();
                    String startTime = contest.get("startTime").asText();
                    log.debug("Contest details - name: {}, code: {}, startTime: {}", name, code, startTime);

                    Contest mappedContest = Contest.builder()
                            .title(name)
                            .titleSlug(code)
                            .startTime(startTime)
                            .platform(Platform.CODECHEF)
                            .build();
                    log.debug("Successfully mapped contest: {}", mappedContest);
                    result.add(mappedContest);
                } catch (Exception e) {
                    log.error("Error processing individual contest: {}", contest, e);
                    throw e;
                }
            }
            log.debug("Successfully mapped all contests, total: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error mapping CodeChef contests response: {}", e.getMessage(), e);
            throw new RuntimeException("Error mapping CodeChef contests response: " + e.getMessage(), e);
        }
    }

    private Long parseDateTime(String year, String month, String day) {
        // Convert to Unix timestamp
        return java.time.LocalDateTime.of(
                Integer.parseInt(year),
                Integer.parseInt(month),
                Integer.parseInt(day),
                0, 0, 0).toEpochSecond(java.time.ZoneOffset.UTC);
    }

    private Long parseDateTime(String dateTimeStr) {
        try {
            log.debug("Parsing date string: {}", dateTimeStr);
            // The date is already in a good format, just parse it directly
            java.time.ZonedDateTime zdt = java.time.ZonedDateTime.parse(dateTimeStr);
            Long timestamp = zdt.toEpochSecond();
            log.debug("Successfully parsed date to timestamp: {}", timestamp);
            return timestamp;
        } catch (Exception e) {
            log.error("Error parsing date: {} - Error: {}", dateTimeStr, e.getMessage());
            throw new RuntimeException("Error parsing date: " + dateTimeStr, e);
        }
    }

}