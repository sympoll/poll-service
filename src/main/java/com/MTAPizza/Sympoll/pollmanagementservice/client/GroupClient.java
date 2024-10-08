package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.GroupResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.UserGroupsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.group.GroupIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.UUID;

public interface GroupClient {
    @GetExchange("/api/group/id")
    ResponseEntity<GroupIdExistsResponse> checkGroupIdExists(@RequestParam String groupId);

    @GetExchange("/api/group/user-role/permission/delete")
    ResponseEntity<Boolean> checkUserPermissionToDeletePoll(@RequestParam UUID userId, @RequestParam String groupId);

    @GetExchange("/api/group/name/by-group-id")
    ResponseEntity<GroupResponse> getGroupNameById(@RequestParam String groupId);

    @GetExchange("/api/group/all-user-groups")
    ResponseEntity<UserGroupsResponse> getAllUserGroups(@RequestParam UUID userId);

    @PostExchange("/api/group/groups-list")
    ResponseEntity<List<GroupResponse>> getGroupDataList(@RequestBody List<String> groupIds);
}
