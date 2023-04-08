package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.exception.FailAuthenticationException;
import com.ecommerce.ecommerceweb.model.AuthenticationToken;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationTokenService {
    @Autowired
    TokenRepository tokenRepository;
    public void saveTokenOfConfirmation(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepository.findByUser(user);
    }

    public User getUser(String token){
        AuthenticationToken token1 = tokenRepository.findByToken(token);
        if (Objects.isNull(token)){
            return null;
        }
        return token1.getUser();
    }

    public void authenticateToken (String token) throws FailAuthenticationException {
        if (Objects.isNull(token)){
            throw new FailAuthenticationException("Token does not exist!");
        }
        if (Objects.isNull(getUser(token))){
            throw new FailAuthenticationException("Token is not valid!");
        }
    }
}
