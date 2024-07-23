package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll;

import java.util.List;

public record PollCreateRequest(
        String title,
        String description,
        int numAnswersAllowed,
        int creatorId,
        int groupId,
        String timeCreated,
        String deadline,
        List<String> answers) {
}
