package com.example.empleados.integration;

public final class DepartamentosAuthTestUtils {

    private DepartamentosAuthTestUtils() {
    }

    public static String authHeader(String username, String password) {
        return BasicAuthTestUtils.authHeader(username, password);
    }
}
