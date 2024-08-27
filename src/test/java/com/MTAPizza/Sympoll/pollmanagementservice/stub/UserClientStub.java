package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserClientStub {
    public static void initStubs(UUID userId){
        stubUserIdExists(userId);
        stubGetUserById(userId);
    }

    private static void stubUserIdExists(UUID userId){
        stubFor(get(urlEqualTo("/api/user/id?userId=" + userId.toString()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                {
                  "isExists": true
                }
                """)));
    }

    private static void stubGetUserById(UUID userId){
        stubFor(get(urlEqualTo("/api/user/by-user-id?userId=" + userId.toString()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(String.format("""
                {
                  "userId": "%s",
                  "username": "Roy",
                  "passwordHash": "324324",
                  "email": "Roy@gmail.com",
                  "timeCreated": "2024-08-14T10:00:00"
                }
                """, userId))));
    }
}
