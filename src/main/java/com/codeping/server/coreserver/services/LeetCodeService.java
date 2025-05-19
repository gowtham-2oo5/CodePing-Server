package com.codeping.server.coreserver.services;

import com.codeping.server.coreserver.models.*;
import java.util.List;

public interface LeetCodeService {
    // ==============================
    // LEETCODE (GraphQL)
    // ==============================

    String LEETCODE_BASE_URL = "https://leetcode.com/graphql";

    String LEETCODE_QUERY_USER_PROFILE = """
                query getUserProfile($username: String!) {
                    matchedUser(username: $username) {
                        username
                        profile {
                            realName
                            userAvatar
                            countryName
                            ranking
                        }
                    }
                }
            """;

    String LEETCODE_QUERY_CONTEST_RANKING = """
                query getUserContestRanking($username: String!) {
                    userContestRanking(username: $username) {
                        attendedContestsCount
                        rating
                        globalRanking
                        totalParticipants
                        topPercentage
                        badge {
                            name
                        }
                    }
                }
            """;

    String LEETCODE_QUERY_RECENT_SUBMISSIONS = """
                query getRecentSubmissions($username: String!) {
                    recentAcSubmissionList(username: $username, limit: 6) {
                        title
                        titleSlug
                        timestamp
                        lang
                    }
                }
            """;

    String LEETCODE_QUERY_CONTEST_HISTORY = """
                query getUserContestHistory($username: String!) {
                    userContestRankingHistory(username: $username) {
                        attended
                        contest {
                            title
                            startTime
                        }
                        rating
                        ranking
                    }
                }
            """;

    String LEETCODE_QUERY_UPCOMING_CONTESTS = """
                query getUpcomingContests {
                    upcomingContests {
                        title
                        titleSlug
                        startTime
                        duration
                        originStartTime
                        isVirtual
                    }
                }
            """;

    // LEETCODE API Methods
    UserProfile fetchLeetCodeUserProfile(String username);

    List<ContestHistory> fetchLeetCodeContestHistory(String username);

    List<Submission> fetchLeetCodeRecentSubmissions(String username);

    List<Contest> fetchLeetCodeUpcomingContests();

    // Cache eviction methods
    void evictUserProfileCache(String username);

    void evictContestHistoryCache(String username);

    void evictRecentSubmissionsCache(String username);

    void evictUpcomingContestsCache();
}