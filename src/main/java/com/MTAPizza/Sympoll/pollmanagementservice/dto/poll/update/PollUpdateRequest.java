package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.update;

import java.util.UUID;

public record PollUpdateRequest(
        UUID pollId,
        UUID userId,
        String groupId,
        String title,
        String description
) {
}
