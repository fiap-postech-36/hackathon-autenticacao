package br.com.autentication.application.infra;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = KeycloakConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties") // Especificando o arquivo de propriedades de teste
public class KeycloakConfigTest {

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.realm-auth}")
    private String realmAuth;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-auth-id}")
    private String clientAuthId;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Test
    public void testKeycloakConfigValues() {
        assertNotNull("serverUrl should not be null", keycloakConfig.getServerUrl());
        assertNotNull("realm should not be null", keycloakConfig.getRealm());
        assertNotNull("realmAuth should not be null", keycloakConfig.getRealmAuth());
        assertNotNull("clientId should not be null", keycloakConfig.getClientId());
        assertNotNull("clientAuthId should not be null", keycloakConfig.getClientAuthId());
        assertNotNull("username should not be null", keycloakConfig.getUsername());
        assertNotNull("password should not be null", keycloakConfig.getPassword());

        assertEquals("http://localhost:8080", keycloakConfig.getServerUrl());
        assertEquals("test-realm", keycloakConfig.getRealm());
        assertEquals("test-realm-auth", keycloakConfig.getRealmAuth());
        assertEquals("test-client-id", keycloakConfig.getClientId());
        assertEquals("test-client-auth-id", keycloakConfig.getClientAuthId());
        assertEquals("test-user", keycloakConfig.getUsername());
        assertEquals("test-password", keycloakConfig.getPassword());
    }

    @Test
    public void testKeycloakBean() {
        assertNotNull("Keycloak bean should not be null", keycloakConfig.keycloak());
    }
}