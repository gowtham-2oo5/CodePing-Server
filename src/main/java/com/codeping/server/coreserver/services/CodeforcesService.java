package com.codeping.server.coreserver.services;

import com.codeping.server.coreserver.models.*;
import java.util.List;

public interface CodeforcesService {
    // ==============================
    // CODEFORCES (Public REST API)
    // ==============================

    String CODEFORCES_BASE_URL = "https://codeforces.com/api";
    String CODEFORCES_USER_INFO_ENDPOINT = CODEFORCES_BASE_URL + "/user.info?handles=";
    String CODEFORCES_USER_RATING_ENDPOINT = CODEFORCES_BASE_URL + "/user.rating?handle=";
    String CODEFORCES_USER_STATUS_ENDPOINT = CODEFORCES_BASE_URL + "/user.status?handle=%s&from=1&count=6";
    String CODEFORCES_CONTEST_LIST_ENDPOINT = CODEFORCES_BASE_URL + "/contest.list";

    // CODEFORCES API Methods
    UserProfile getCodeforcesUserInfo(String handle);

    List<ContestHistory> getCodeforcesUserRating(String handle);

    List<Submission> getCodeforcesRecentSubmissions(String handle);

    List<Contest> getCodeforcesUpcomingContests();

    // Cache eviction methods
    void evictUserProfileCache(String handle);

    void evictContestHistoryCache(String handle);

    void evictRecentSubmissionsCache(String handle);

    void evictUpcomingContestsCache();
}
