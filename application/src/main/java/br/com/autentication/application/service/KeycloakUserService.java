package br.com.autentication.application.service;

import br.com.autentication.application.infra.KeycloakConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class KeycloakUserService {

    @Autowired
    private Keycloak keycloak;
    @Autowired
    private KeycloakConfig keycloakConfig;

    public String createUser(UserRequest userRequest) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealmAuth());
            UsersResource usersResource = realmResource.users();
            UserRepresentation user = createUserRepresentation(userRequest);
            usersResource.create(user);
            setUserPassword(userRequest, usersResource);
            return "Usuário criado com sucesso!";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    private static void setUserPassword(UserRequest userRequest, UsersResource usersResource) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userRequest.getPassword());
        credential.setTemporary(false);
        String userId = usersResource.search(userRequest.getUsername()).get(0).getId();
        usersResource.get(userId).resetPassword(credential);
    }

    private static UserRepresentation createUserRepresentation(UserRequest userRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setEnabled(true);
        return user;
    }

    public Map<String, Object> validateUser(UserRequest userRequest) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(keycloakConfig.getServerUrl())
                .path("/realms/" + keycloakConfig.getRealmAuth() + "/protocol/openid-connect/token");
        Form form = new Form();
        form.param("client_id", keycloakConfig.getClientAuthId());
        form.param("username", userRequest.getUsername());
        form.param("password", userRequest.getPassword());
        form.param("grant_type", "password");
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.form(form));
        if (response.getStatus() == 200) {
            return response.readEntity(Map.class);
        } else {
            throw new RuntimeException("Erro ao autenticar no Keycloak: " + response.getStatus());
        }
    }
}