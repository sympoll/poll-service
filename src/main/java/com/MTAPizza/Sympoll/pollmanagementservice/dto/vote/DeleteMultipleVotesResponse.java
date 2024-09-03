package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.util.List;
import java.util.UUID;

public record DeleteMultipleVotesResponse(List<UUID> voteIds) {
}
