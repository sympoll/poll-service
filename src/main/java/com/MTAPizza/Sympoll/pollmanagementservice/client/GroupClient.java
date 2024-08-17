package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.group.GroupIdExistsRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.group.GroupIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface GroupClient {
    @GetExchange("/api/group/id")
    ResponseEntity<GroupIdExistsResponse> checkGroupIdExists(@RequestParam GroupIdExistsRequest groupIdExistsRequest);
}
