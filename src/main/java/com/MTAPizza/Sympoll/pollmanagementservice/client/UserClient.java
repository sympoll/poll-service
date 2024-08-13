package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.user.UserIdExistsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/user")
    ResponseEntity<String> checkUserIdExists(@RequestBody UserIdExistsRequest userIdExistsRequest);
}
