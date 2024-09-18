package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.create;

import java.util.List;
import java.util.UUID;

public record PollCreateRequest(
        String title,
        String description,
        int nofAnswersAllowed,
        UUID creatorId,
        String groupId,
        String deadline,
        List<String> votingItems) {
}
