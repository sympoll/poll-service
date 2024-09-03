package com.MTAPizza.Sympoll.pollmanagementservice.dto.group;

import java.util.List;
import java.util.UUID;

public record DeleteGroupPollsResponse(List<UUID> pollsIds) {
}
