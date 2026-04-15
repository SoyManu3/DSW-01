package com.example.empleados.integration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class BasicAuthTestUtils {

    private BasicAuthTestUtils() {
    }

    public static String authHeader(String username, String password) {
        String value = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
