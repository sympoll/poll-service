package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.user.UserResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.UserIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import java.util.List;
import java.util.UUID;

public interface UserClient {
    @GetExchange("/api/user/id")
    ResponseEntity<UserIdExistsResponse> checkUserIdExists(@RequestParam UUID userId);

    @GetExchange("/api/user/by-user-id")
    ResponseEntity<UserResponse> getUserById(@RequestParam UUID userId);

    @PostExchange("/api/user/list-by-user-ids")
    ResponseEntity<List<UserResponse>> getUsersListByIds(@RequestBody List<UUID> userIds);
}
