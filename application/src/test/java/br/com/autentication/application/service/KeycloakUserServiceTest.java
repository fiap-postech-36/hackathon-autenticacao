package br.com.autentication.application.service;

import br.com.autentication.application.infra.KeycloakConfig;
import jakarta.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakUserServiceTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private KeycloakConfig keycloakConfig;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private KeycloakUserService keycloakUserService;

    @Before
    public void setUp() {
        when(keycloakConfig.getRealmAuth()).thenReturn("test-realm");
        when(keycloak.realm("test-realm")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
    }

    @Test
    public void testCreateUserSuccess() {
        UserRequest userRequest = new UserRequest("ibraim", "leandroibraimads@gmail.com", "password");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        when(usersResource.search(userRequest.getUsername())).thenReturn(Collections.singletonList(userRepresentation));

        Response responseMock = Response.ok().build();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(responseMock);

        UserResource userResource = mock(UserResource.class);
        doNothing().when(userResource).resetPassword(any(CredentialRepresentation.class));
        when(usersResource.get("1")).thenReturn(userResource);

        String result = keycloakUserService.createUser(userRequest);
        assertEquals("Usuário criado com sucesso!", result);
    }

    @Test
    public void testCreateUserFailure() {
        UserRequest userRequest = new UserRequest("Ibraim", "leandroibraimads@gmail.com", "password");
        when(usersResource.create(any(UserRepresentation.class)))
                .thenThrow(new RuntimeException("Erro ao criar usuário"));

        try {
            keycloakUserService.createUser(userRequest);
            fail("Esperava uma exceção RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Erro ao criar usuário"));
        }
    }

    @Test
    public void testCreateUserWhenUserAlreadyExists() {
        UserRequest userRequest = new UserRequest("existinguser", "existing@example.com", "password");

        UserRepresentation existingUser = new UserRepresentation();
        existingUser.setId("1");
        when(usersResource.search(userRequest.getUsername())).thenReturn(Collections.singletonList(existingUser));

        UserResource userResource = mock(UserResource.class);
        when(usersResource.get("1")).thenReturn(userResource);

        doNothing().when(userResource).resetPassword(any(CredentialRepresentation.class));

        String result = keycloakUserService.createUser(userRequest);

        assertEquals("Usuário criado com sucesso!", result);
    }


    @Test
    public void testCreateUserWhenCreateFailsWithResponseError() {
        UserRequest userRequest = new UserRequest("Ibraim", "leandroibraimads@gmail.com", "password");

        when(usersResource.create(any(UserRepresentation.class)))
                .thenReturn(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());

        try {
            keycloakUserService.createUser(userRequest);
            fail("Esperava uma exceção RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Erro ao criar usuário"));
        }
    }

    @Test
    public void testCreateUserWhenResetPasswordFails() {
        UserRequest userRequest = new UserRequest("Ibraim", "leandroibraimads@gmail.com", "password");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        when(usersResource.search(userRequest.getUsername())).thenReturn(Collections.singletonList(userRepresentation));

        Response responseMock = Response.ok().build();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(responseMock);

        UserResource userResource = mock(UserResource.class);
        doThrow(new RuntimeException("Erro ao resetar senha")).when(userResource).resetPassword(any(CredentialRepresentation.class));
        when(usersResource.get("1")).thenReturn(userResource);

        try {
            keycloakUserService.createUser(userRequest);
            fail("Esperava uma exceção RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Erro ao resetar senha"));
        }
    }
}