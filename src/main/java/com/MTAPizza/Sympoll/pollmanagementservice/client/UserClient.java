package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.UserIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface UserClient {
    @GetExchange("/api/user/id")
    ResponseEntity<UserIdExistsResponse> checkUserIdExists(@RequestParam UUID userId);
}
