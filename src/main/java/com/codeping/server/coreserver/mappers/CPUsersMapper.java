package com.codeping.server.coreserver.mappers;

import com.codeping.server.coreserver.dto.UsersDTO;
import com.codeping.server.coreserver.models.CPUsers;
import com.codeping.server.coreserver.models.UserRoles;

public class CPUsersMapper {

    public static UsersDTO toDTO(CPUsers user) {
        if (user == null) return null;

        return UsersDTO.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .firebaseId(user.getFirebaseUid())
                .mail(user.getEmail())
                .name(user.getName())
                .profilePicUrl(user.getProfilePicUrl())
                .role(user.getRole().name())
                .githubUrl(user.getGithubUrl())
                .linkedInUrl(user.getLinkedinUrl())
                .resumeUrl(user.getResumeUrl())
                .build();
    }

    public static CPUsers toEntity(UsersDTO dto) {
        if (dto == null) return null;

        return CPUsers.builder()
                .firebaseUid(dto.getFirebaseId())
                .email(dto.getMail())
                .name(dto.getName())
                .profilePicUrl(dto.getProfilePicUrl())
                .role(UserRoles.valueOf(dto.getRole()))
                .githubUrl(dto.getGithubUrl())
                .linkedinUrl(dto.getLinkedInUrl())
                .resumeUrl(dto.getResumeUrl())
                .build();
    }
}
