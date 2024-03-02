package com.backend.service;

import com.backend.exception.CartException;
import com.backend.model.Cart;
import com.backend.model.CartItem;
import com.backend.model.Food;
import com.backend.model.User;
import com.backend.repository.CartItemRepository;
import com.backend.repository.CartRepository;
import com.backend.request.CartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImp implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Override
    public CartItem addItemToCart(CartItemRequest request, String jwt) {
        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.getFoodById(request.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem : cart.getItems()){
            if(cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setFood(food);
        newItem.setQuantity(request.getQuantity());
        newItem.setPrice(food.getPrice() * request.getQuantity());
        newItem.setIngredients(request.getIngredients());

        CartItem savedItem = cartItemRepository.save(newItem);

        cart.getItems().add(savedItem);

        return savedItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isEmpty()){
            throw new CartException("Cart item not found");
        }

        CartItem item = cartItem.get();
        item.setQuantity(quantity);
        item.setPrice(item.getFood().getPrice() * quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isEmpty()){
            throw new CartException("Cart item not found");
        }

        CartItem item = cartItem.get();
        cart.getItems().remove(item);

        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) {
        Long total = 0L;

        for(CartItem item : cart.getItems()){
            total += item.getFood().getPrice() * item.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if(cart.isEmpty()){
            throw new CartException("Cart not found");
        }

        return cart.get();
    }

    @Override
    public Cart findCartByUserId(String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByCustomerId(user.getId());
        if(cart == null){
            throw new CartException("Cart not found");
        }

        return cart;
    }

    @Override
    public Cart clearCart(String jwt) {
        Cart cart = findCartByUserId(jwt);
        cart.getItems().clear();

        return cartRepository.save(cart);
    }
}
