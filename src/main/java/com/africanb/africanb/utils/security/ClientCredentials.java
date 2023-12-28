package com.africanb.africanb.utils.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientCredentials {
    @Value("${server.id}")
    private String serverId;
    @Value("${client.id}")
    private String clientId;
}
