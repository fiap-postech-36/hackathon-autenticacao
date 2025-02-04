package br.com.autentication.application.controllers;

import br.com.autentication.application.service.KeycloakUserService;
import br.com.autentication.application.service.UserRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private KeycloakUserService keycloakUserService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            String response = keycloakUserService.createUser(userRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> autenticateUser(@RequestBody UserRequest userRequest) {
        Map<String, Object> accessTokenResponse = keycloakUserService.validateUser(userRequest);
        return ResponseEntity.ok(accessTokenResponse);
    }
}
