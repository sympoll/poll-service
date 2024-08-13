package com.MTAPizza.Sympoll.pollmanagementservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserClientStub {
    public static void stabUserIdExists(){
        stubFor(get(urlEqualTo("/api/user"))
                .withRequestBody(matchingJsonPath("$.userId"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));
    }
}
