package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete;

import java.util.UUID;

public record PollDeleteRequest(
        UUID pollId,
        UUID userId,
        String groupId
) {
}
