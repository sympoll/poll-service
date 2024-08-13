package com.MTAPizza.Sympoll.pollmanagementservice.dto.user;

import java.util.UUID;

public record UserIdExistsRequest(
        UUID userId
) {
}
