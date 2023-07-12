package com.africanb.africanb.utils.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdentificationClient {

    @Value("${server.id}")
    private String serverId;
    @Value("${client.id}")
    private String clientId;
}
