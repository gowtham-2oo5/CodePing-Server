package com.codeping.server.coreserver.services;

import com.codeping.server.coreserver.models.*;
import java.util.List;

public interface CodeChefService {
    // ==============================
    // CODECHEF (Local Microservice)
    // ==============================

    String CODECHEF_BASE_URL = "http://localhost:8800";
    String CODECHEF_PROFILE_ENDPOINT = CODECHEF_BASE_URL + "/api/profile/";
    String CODECHEF_RATINGS_GRAPH_ENDPOINT = CODECHEF_BASE_URL + "/api/ratings/";
    String CODECHEF_RECENT_SUBMISSIONS_ENDPOINT = CODECHEF_BASE_URL + "/api/recent/";
    String CODECHEF_WHOLE_ENDPOINT = CODECHEF_BASE_URL + "/api/whole/";
    String CODECHEF_UPCOMING_CONTESTS_ENDPOINT = CODECHEF_BASE_URL + "/api/upcoming";

    // CODECHEF API Methods
    UserProfile getCodechefProfile(String username);

    List<ContestHistory> getCodechefRatingsGraph(String username);

    List<Submission> getCodechefRecentSubmissions(String username);

    List<ContestHistory> getCodechefWholeData(String username);

    List<Contest> getCodechefUpcomingContests();

    // Cache eviction methods
    void evictUserProfileCache(String username);

    void evictContestHistoryCache(String username);

    void evictRecentSubmissionsCache(String username);

    void evictWholeDataCache(String username);

    void evictUpcomingContestsCache();
}