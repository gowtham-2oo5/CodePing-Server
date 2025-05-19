package com.codeping.server.coreserver.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersDTO {

    private String id;
    private String firebaseId;
    private String mail;
    private String name;
    private String profilePicUrl;
    private String role;
    private String githubUrl;
    private String linkedInUrl;
    private String resumeUrl;

}
