package com.main.springsecurityjwt.controller;


import com.main.springsecurityjwt.util.JwtUtil;
import com.main.springsecurityjwt.models.AuthenticationRequest;
import com.main.springsecurityjwt.models.AuthenticationResponse;
import com.main.springsecurityjwt.services.MyUserDetailsService;
import com.main.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class helloEndPoint {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;


    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello World";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws  Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()) );
        }
        catch (BadCredentialsException e){
            throw new Exception("incorrect username or password",e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.genertateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}