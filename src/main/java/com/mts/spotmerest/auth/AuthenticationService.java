package com.mts.spotmerest.auth;

import com.mts.spotmerest.configs.JwtService;
import com.mts.spotmerest.mappers.UserDAO;
import com.mts.spotmerest.models.Role;
import com.mts.spotmerest.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserDAO userDAO;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        String jwtToken=null;
        AuthenticationResponse authenticationResponse= new AuthenticationResponse();
        int userExists;
        if(userDAO.findByEmail(request.getEmail()).isPresent()){
            userExists=1;
        }else{
            userExists=0;
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                //.createdAt(getCurrentTime())
                .build();

        if(userExists==0){
            userDAO.save(user);
            Map<String,Object> map = new HashMap<>();
            map.put("sub",request.getEmail());
            map.put("first_name",request.getFirstname());
            map.put("email",request.getEmail());
            jwtToken = jwtService.generateToken(map,user);
//            return AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
            authenticationResponse =AuthenticationResponse.builder()
                    .access_token(jwtToken)
                    .build();
            System.out.println("User Created "+ request.getEmail());
            authenticationResponse.setCode(200L);
        }else if(userExists==1){

            authenticationResponse.setMessage("user exists");
            authenticationResponse.setCode(408L);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not match request");
        }else{
            authenticationResponse.setMessage("User does not match request");
            authenticationResponse.setCode(407L);
        }

        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        System.out.println("auth service authenticate: "+ request.getEmail());
        AuthenticationResponse authenticationResponse; //means username and password are correct
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        System.out.println("Successful new token: "+ authenticationToken.getName());//means username and password are correct
            authenticationManager.authenticate(authenticationToken);
        var user = userDAO.findByEmail(request.getEmail())
                .orElseThrow();
        System.out.println("Found Email: "+user.getFirstname() );
        Map<String,Object> map = new HashMap<>();
        map.put("sub",user.getEmail());
        map.put("first_name",user.getFirstname());
        map.put("email",user.getEmail());
        var jwtToken = jwtService.generateToken(map,user);
        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .build();
    }

    public Date getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return date;
    }
}
