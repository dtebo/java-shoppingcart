package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user",
        produces = {"application/json"})
    public ResponseEntity<?> listCartItemsByUserId()
    {
//        User u = userService.findUserById(userid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(authentication,
            HttpStatus.OK);
    }

    @PutMapping(value = "/add/product/{productid}",
        produces = {"application/json"})
    public ResponseEntity<?> addToCart(
        @PathVariable
            long productid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User u = userService.findByName(userDetails.getUsername());

        CartItem addCartTtem = cartItemService.addToCart(u.getUserid(),
            productid,
            "I am not working");
        return new ResponseEntity<>(addCartTtem,
            HttpStatus.OK);
    }

    @DeleteMapping(value = "/remove/product/{productid}",
        produces = {"application/json"})
    public ResponseEntity<?> removeFromCart(
        @PathVariable
            long productid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User u = userService.findByName(userDetails.getUsername());

        CartItem removeCartItem = cartItemService.removeFromCart(u.getUserid(),
            productid,
            "I am still not working");
        return new ResponseEntity<>(removeCartItem,
            HttpStatus.OK);
    }
}
