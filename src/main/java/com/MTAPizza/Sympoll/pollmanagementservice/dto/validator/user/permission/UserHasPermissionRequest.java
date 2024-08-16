package com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.permission;

import java.util.UUID;

public record UserHasPermissionRequest(
        UUID userId,
        String groupId
) {
}
