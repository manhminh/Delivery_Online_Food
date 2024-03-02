package com.backend.service;

import com.backend.model.Cart;
import com.backend.model.CartItem;
import com.backend.request.CreateCartItemRequest;

public interface CartService {
    CartItem addItemToCart(CreateCartItemRequest request, String jwt);

    CartItem updateCartItemQuantity(Long cartItemId, int quantity);

    Cart removeItemFromCart(Long cartItemId, String jwt);

    Long calculateCartTotals(Cart cart);

    Cart findCartById(Long cartId);

    Cart findCartByUserId(Long userId);

    Cart clearCart(Long userId);
}
