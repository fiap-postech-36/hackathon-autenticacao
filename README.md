# Keycloak: Gerenciamento de Identidades e Acessos

O Keycloak é uma ferramenta de gerenciamento de identidade e acesso que permite a autenticação, autorização e gerenciamento de usuários de forma simples e segura.
---

## Autenticação no Keycloak

### Como funciona a autenticação no Keycloak?

O Keycloak oferece um fluxo de autenticação robusto baseado em protocolos de segurança como OAuth 2.0 e OpenID Connect. Ele fornece suporte nativo para:

- **Autenticação com Single Sign-On (SSO):** Permite que os usuários façam login uma vez e acessem várias aplicações sem necessidade de se autenticar novamente.
- **Tokens de Acesso, Atualização e ID:** Após a autenticação, o Keycloak emite tokens que podem ser usados para autorização e acesso a APIs protegidas.
- **Configuração de Provedores de Identidade:** Integração com provedores externos como Google, Microsoft, GitHub, etc.
- **Autenticação Multifator (MFA):** Adiciona uma camada extra de segurança ao exigir um segundo fator de autenticação.

### Benefícios da Autenticação com Keycloak

1. **Segurança Aprimorada:** Uso de padrões de mercado para proteger credenciais e acessos.
2. **Centralização de Gerenciamento:** Administra múltiplas aplicações a partir de uma interface única.
3. **Escalabilidade:** Suporte a um grande número de usuários e sistemas.
4. **Flexibilidade:** Permite customizações nos fluxos de autenticação e autorização.
5. **Economia de Tempo:** Reduz a complexidade de implementar autenticação em aplicações individuais.

---

## Rodando o Keycloak via Docker e importando um Realm

### Instruções para executar o Keycloak via Docker

1. Execute o comando abaixo para iniciar um container do Keycloak:

```bash
docker docker-compose up -d
```

2. Acesse o console administrativo do Keycloak no seguinte link:

[http://localhost:8080](http://localhost:8080)

Use as credenciais definidas no arquivo .env para as variáveis de ambiente `KEYCLOAK_ADMIN` e `KEYCLOAK_ADMIN_PASSWORD` para fazer login.

---

## Criando um usuário via API do Keycloak

Você pode criar usuários programaticamente utilizando a API REST do Keycloak.

### Passo a passo

1. **Obtenha um token de administração**:

   Execute o seguinte comando para obter um token:

   ```bash
   curl --request POST \
     --url http://localhost:8080/realms/master/protocol/openid-connect/token \
     --header 'Content-Type: application/x-www-form-urlencoded' \
     --data 'grant_type=password' \
     --data 'client_id=admin-cli' \
     --data 'username=admin' \
     --data 'password=admin'
   ```

   O token será retornado no campo `access_token` da resposta.

2. **Envie a requisição para criar o usuário**:

   Com o token obtido, execute o seguinte comando para criar um usuário:

   ```bash
   curl --request POST \
     --url http://localhost:8080/admin/realms/<realm-name>/users \
     --header 'Authorization: Bearer <access_token>' \
     --header 'Content-Type: application/json' \
     --data '{
       "username": "novo-usuario",
       "enabled": true,
       "credentials": [{
         "type": "password",
         "value": "senha-segura",
         "temporary": false
       }]
     }'
   ```

   - Substitua `<realm-name>` pelo nome do realm.
   - Substitua `<access_token>` pelo token obtido anteriormente.
   - Altere os valores de `username` e `password` conforme necessário.

3. **Confirme o sucesso**:

   - Verifique no Keycloak (interface web ou API) se o usuário foi criado corretamente.

---

## 🔗 Links

- Collection: https://acesse.one/puMqh
- https://www.keycloak.org/

---

## Time de desenvolvedores

- [@ulysses903](https://github.com/ulysses903)
- [@samuelmteixeira](https://www.github.com/samuelmteixeira)
- [@kaiquesantos98](https://www.github.com/KaiqueSantos98)
- [@jjbazagajr](https://www.github.com/jjbazagajr)
- [@leandroibraim](https://www.github.com/leandroibraim)