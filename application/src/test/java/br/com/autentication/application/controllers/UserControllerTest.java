package br.com.autentication.application.controllers;

import br.com.autentication.application.service.KeycloakUserService;
import br.com.autentication.application.service.UserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private KeycloakUserService keycloakUserService;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;

    @Test
    public void createUser_Success() {
        when(keycloakUserService.createUser(userRequest)).thenReturn("User created successfully");

        ResponseEntity<String> response = userController.createUser(userRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User created successfully", response.getBody());
    }

    @Test
    public void createUser_Failure() {
        when(keycloakUserService.createUser(userRequest)).thenThrow(new RuntimeException("User creation failed"));

        ResponseEntity<String> response = userController.createUser(userRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User creation failed", response.getBody());
    }

    @Test
    public void autenticateUser_Success() {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "dummy_token");
        when(keycloakUserService.validateUser(userRequest)).thenReturn(mockResponse);

        ResponseEntity<Map<String, Object>> response = userController.autenticateUser(userRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("dummy_token", response.getBody().get("access_token"));
    }
}