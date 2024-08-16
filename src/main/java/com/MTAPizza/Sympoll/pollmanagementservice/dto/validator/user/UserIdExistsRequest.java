package com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user;

import java.util.UUID;

public record UserIdExistsRequest(
        UUID userId
) {
}
