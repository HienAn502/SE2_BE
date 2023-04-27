package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.ResponseDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SigninDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SigninRespDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SignupDTO;
import com.ecommerce.ecommerceweb.exception.CommonException;
import com.ecommerce.ecommerceweb.exception.FailAuthenticationException;
import com.ecommerce.ecommerceweb.model.AuthenticationToken;
import com.ecommerce.ecommerceweb.model.Cart;
import com.ecommerce.ecommerceweb.model.User;
import com.ecommerce.ecommerceweb.repository.CartRepository;
import com.ecommerce.ecommerceweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationTokenService authenticationTokenService;
    @Autowired
    CartRepository cartRepository;
    @Transactional
        public ResponseDTO signup(SignupDTO signupDTO) {
        // if user already exist, cant sign up
        if (Objects.nonNull(userRepository.findByEmail(signupDTO.getEmail()))){
            throw new CommonException("This user already exist!");
        }
        // hash password -> save user to db -> generate token
        // hash password
        String encryptedPassword = signupDTO.getPassword();
        try {
            encryptedPassword = hashPassword(signupDTO.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(e.getMessage());
        }
        Cart cart = new Cart();
        cart.setCartItemList(new ArrayList<>());
        cart.setCreatedDate(new Date());

        // save user to db
        User user = new User(signupDTO.getFirstName(), signupDTO.getLastName(), signupDTO.getPhoneNumber(), signupDTO.getEmail(), encryptedPassword,cart,  new ArrayList<>());
        cart.setUser(user);
        cartRepository.save(cart);
        userRepository.save(user);

        // generate token
        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationTokenService.saveTokenOfConfirmation(authenticationToken);

        ResponseDTO responseDTO = new ResponseDTO("success", "Sign up successful!");
        return responseDTO;
    }

    // hash password by md5 algorithm
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SigninRespDTO signin(SigninDTO signinDTO) {
        // find if user email exist in db
        User user = userRepository.findByEmail(signinDTO.getEmail());
        if (Objects.isNull(user)){
            throw new FailAuthenticationException("User not valid!");
        }

        // compare hash-ed password
        try {
            if (!user.getPassword().equals(hashPassword(signinDTO.getPassword()))){
                throw new FailAuthenticationException("Password is incorrect!");
            }
        } catch (NoSuchAlgorithmException e) {

        }

        // if password match then get the token
        AuthenticationToken token = authenticationTokenService.getToken(user);
        if (Objects.isNull(token)){
            throw new CommonException("Token does not exist!");
        }
        return new SigninRespDTO("success", token.getToken());
    }
}
