package com.thrifters.controller;



import com.thrifters.config.JwtProvider;
import com.thrifters.exception.UserException;
import com.thrifters.model.User;
import com.thrifters.repository.UserRepository;
import com.thrifters.request.LoginRequest;
import com.thrifters.response.AuthResponse;
import com.thrifters.service.CustomUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService userService;

    @PostMapping("/signup")
     public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException {
         String email = user.getEmail();
         String password = user.getPassword();
         String firstName= user.getFirstName();
         String lastName = user.getLastName();

         User isEmailExist = userRepository.findByEmail(email);
         if(isEmailExist!=null){
             throw new UserException("email already exist with another account");
         }

         // Hash the user's password
         String hashedPassword = passwordEncoder.encode(password);

         User createdUser = new User();
         createdUser.setEmail(email);
         createdUser.setPassword(hashedPassword);
         createdUser.setFirstName(firstName);
         createdUser.setLastName(lastName);

         User savedUser = userRepository.save(createdUser);
         Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
         SecurityContextHolder.getContext().setAuthentication(authentication);

         String token = jwtProvider.generateToken(authentication);
         AuthResponse authResponse = new AuthResponse(token,"Signup Success");
         return new  ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
     }

     @PostMapping("/signin")
     public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

         Authentication authentication = authenticate(username,password);
         SecurityContextHolder.getContext().setAuthentication(authentication);

         String token = jwtProvider.generateToken(authentication);
         AuthResponse authResponse = new AuthResponse(token,"Signin Success");
         return new  ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid Username");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
