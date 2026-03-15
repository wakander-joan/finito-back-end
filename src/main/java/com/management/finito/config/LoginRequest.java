package com.management.finito.config;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {
    private String email;
    private String senha;
}
