package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GroupClientStub {
    public static void initStubs(UUID userId){
        stubGroupIdExists();
        stubUserHasPermission(userId);
        stubGetGroupName();
    }

    private static void stubGroupIdExists(){
        stubFor(get(urlEqualTo("/api/group/id?groupId=123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                {
                  "isExists": true
                }
                """)));
    }

    private static void stubUserHasPermission(UUID userId){
        stubFor(get(urlEqualTo("/api/group/user-role/permission/delete?userId=" + userId.toString() + "&groupId=123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("Group Admin")));
    }

    private static void stubGetGroupName(){
        stubFor(get(urlEqualTo("/api/group/by-group-id?groupId=123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                {
                  "groupId": "123",
                  "groupName": "Group"
                }
                """)));
    }
}
