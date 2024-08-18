package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserClientStub {
    public static void initStubs(UUID userId){
        stubUserIdExists(userId);
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
}
