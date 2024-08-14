package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserClientStub {
    public static void initStubs(){
        stubUserIdExists();
        stubUserHasPermission();
    }

    private static void stubUserIdExists(){
        stubFor(get(urlEqualTo("/api/user"))
                .withRequestBody(matchingJsonPath("$.userId"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                {
                  "isExists": true
                }
                """)));
    }

    private static void stubUserHasPermission(){
        stubFor(get(urlEqualTo("/api/user/permission"))
                .withRequestBody(matchingJsonPath("$.userId"))
                .withRequestBody(matchingJsonPath("$.groupId"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                {
                  "hasPermission": true
                }
                """)));
    }
}
