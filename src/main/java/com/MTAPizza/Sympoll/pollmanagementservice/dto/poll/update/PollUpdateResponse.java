package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.update;

import java.util.UUID;

public record PollUpdateResponse(
        UUID pollId,
        String title,
        String description
) {
}
