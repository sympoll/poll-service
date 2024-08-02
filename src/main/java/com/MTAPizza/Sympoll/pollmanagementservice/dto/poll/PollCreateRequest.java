package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll;

import java.util.List;

public record PollCreateRequest(
        String title,
        String description,
        int nofAnswersAllowed,
        int creatorId,
        int groupId,
        String deadline,
        List<String> votingItems) {
}
