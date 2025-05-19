package com.codeping.server.coreserver.dto;

import com.codeping.server.coreserver.models.CPUsers;
import com.codeping.server.coreserver.models.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CPUserDTO {
    private UUID id;
    private String firebaseUid;
    private String email;
    private String name;
    private String shortBio;
    private String linkedinUrl;
    private String githubUrl;
    private String resumeUrl;
    private String profilePicUrl;
    private UserRoles role;

    public static CPUserDTO fromEntity(CPUsers user) {
        return CPUserDTO.builder()
                .id(user.getId())
                .firebaseUid(user.getFirebaseUid())
                .email(user.getEmail())
                .name(user.getName())
                .shortBio(user.getShortBio())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .resumeUrl(user.getResumeUrl())
                .profilePicUrl(user.getProfilePicUrl())
                .role(user.getRole())
                .build();
    }

    public CPUsers toEntity() {
        return CPUsers.builder()
                .id(this.id)
                .firebaseUid(this.firebaseUid)
                .email(this.email)
                .name(this.name)
                .shortBio(this.shortBio)
                .linkedinUrl(this.linkedinUrl)
                .githubUrl(this.githubUrl)
                .resumeUrl(this.resumeUrl)
                .profilePicUrl(this.profilePicUrl)
                .role(this.role)
                .build();
    }
}