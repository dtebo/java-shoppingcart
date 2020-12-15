package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.models.UserMinimum;
import com.lambdaschool.shoppingcart.models.UserRoles;
import com.lambdaschool.shoppingcart.services.RoleService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class CreateNewUser {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping(value = "/createnewuser", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addSelf(HttpServletRequest request, @Valid @RequestBody UserMinimum userMinimum) throws URISyntaxException{
        User newuser = new User();
        newuser.setUsername(userMinimum.getUsername());
        newuser.setPassword(userMinimum.getPassword());
        newuser.setPrimaryemail(userMinimum.getPrimaryemail());

        Set<UserRoles> newRoles = new HashSet<>();
        newRoles.add(new UserRoles(newuser, roleService.findByName("user")));
        newuser.setRoles(newRoles);
        newuser = userService.save(newuser);

        RestTemplate restTemplate = new RestTemplate();
        String requestURI = "http://localhost" + ":" + request.getLocalPort() + "/login";
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(acceptableMediaTypes);
        headers.setBasicAuth(System.getenv("OAUTHCLIENTID"), System.getenv("OAUTHCLIENTSECRET"));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type",
                "password");
        map.add("scope",
                "read write trust");
        map.add("username",
                userMinimum.getUsername());
        map.add("password",
                userMinimum.getPassword());
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(map, headers);
        String token = restTemplate.postForObject(requestURI, tokenRequest, String.class);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
