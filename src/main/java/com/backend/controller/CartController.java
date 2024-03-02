package com.backend.controller;

import com.backend.model.Cart;
import com.backend.model.CartItem;
import com.backend.request.CartItemRequest;
import com.backend.request.UpdateCartItemRequest;
import com.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody CartItemRequest request,
                                                  @RequestHeader("Authorization") String jwt) {
        CartItem createdItem = cartService.addItemToCart(request, jwt);
        return new ResponseEntity<>(createdItem, HttpStatus.OK);
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest request,
            @RequestHeader("Authorization") String jwt) {

        CartItem createdItem = cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());
        return new ResponseEntity<>(createdItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{itemId}/remove")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable("itemId") Long itemId,
            @RequestHeader("Authorization") String jwt) {
        Cart cart = cartService.removeItemFromCart(itemId, jwt);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader("Authorization") String jwt) {
        Cart cart = cartService.clearCart(jwt);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String jwt) {
        Cart cart = cartService.findCartByUserId(jwt);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
