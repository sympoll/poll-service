package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GroupClientStub {
    public static void initStubs(){
        stubGroupIdExists();
    }

    private static void stubGroupIdExists(){
        stubFor(get(urlEqualTo("/api/group"))
                .withRequestBody(matchingJsonPath("$.groupId"))
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
