package br.com.fiap.dining_organizer.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class LoginDuplicadoException extends RuntimeException {

    public LoginDuplicadoException() {
        super("Já existe um usuário com esse login.");
    }

    public LoginDuplicadoException(String message) {
        super(message);
    }

    public LoginDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}

