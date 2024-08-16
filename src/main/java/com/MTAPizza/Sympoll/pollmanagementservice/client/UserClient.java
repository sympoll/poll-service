package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.UserIdExistsRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.UserIdExistsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.permission.UserHasPermissionRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.permission.UserHasPermissionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {
    @GetExchange("/api/user")
    ResponseEntity<UserIdExistsResponse> checkUserIdExists(@RequestBody UserIdExistsRequest userIdExistsRequest);

    @GetExchange("/api/user/permission")
    ResponseEntity<UserHasPermissionResponse> checkHasPermissionExists(@RequestBody UserHasPermissionRequest userHasPermissionRequest);
}
