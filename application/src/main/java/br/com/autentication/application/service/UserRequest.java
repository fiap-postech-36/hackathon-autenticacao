package br.com.autentication.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String username;
    private String email;
    private String password;
}
