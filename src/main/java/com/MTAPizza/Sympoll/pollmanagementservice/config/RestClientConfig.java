package com.MTAPizza.Sympoll.pollmanagementservice.config;

import com.MTAPizza.Sympoll.pollmanagementservice.client.GroupClient;
import com.MTAPizza.Sympoll.pollmanagementservice.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${group.service.url}")
    private String groupServiceUrl;

    @Bean
    public UserClient userClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(UserClient.class);
    }

    @Bean
    public GroupClient groupClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(groupServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(GroupClient.class);
    }
}
