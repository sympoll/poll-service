package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.group.GroupIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface GroupClient {
    @GetExchange("/api/group/id")
    ResponseEntity<GroupIdExistsResponse> checkGroupIdExists(@RequestParam String groupId);

    @GetExchange("/api/group/user-role/name")
    ResponseEntity<String> getUserRollName(@RequestParam UUID userId, @RequestParam String groupId);
}
