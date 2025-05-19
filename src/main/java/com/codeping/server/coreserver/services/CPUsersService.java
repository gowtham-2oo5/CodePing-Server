package com.codeping.server.coreserver.services;

import com.codeping.server.coreserver.dto.CPUserDTO;
import java.util.List;
import java.util.UUID;

public interface CPUsersService {
    CPUserDTO createUser(CPUserDTO userDTO);

    CPUserDTO updateUser(UUID id, CPUserDTO userDTO);

    void deleteUser(UUID id);

    CPUserDTO getUserById(UUID id);

    List<CPUserDTO> getAllUsers();

    CPUserDTO getUserByEmail(String email);

    CPUserDTO getUserByFirebaseUid(String firebaseUid);
}
