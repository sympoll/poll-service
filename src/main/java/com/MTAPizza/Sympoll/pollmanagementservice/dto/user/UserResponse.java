package com.MTAPizza.Sympoll.pollmanagementservice.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String username,
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime timeCreated
) {
}
