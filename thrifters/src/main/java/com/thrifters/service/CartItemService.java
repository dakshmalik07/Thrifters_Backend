package com.thrifters.service;


import com.thrifters.exception.CartItemException;
import com.thrifters.exception.UserException;
import com.thrifters.model.Cart;
import com.thrifters.model.CartItem;
import com.thrifters.model.Product;
import com.thrifters.model.User;
import com.thrifters.repository.CartItemRepository;
import com.thrifters.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    public CartItem createCartItem(CartItem cartItem){
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getDiscountedPrice()*cartItem.getQuantity());
        CartItem createdCartItem  = cartItemRepository.save(cartItem);
        return createdCartItem;
    }


    public CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        if(user.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getQuantity()*cartItem.getProduct().getPrice());
            item.setDiscountedPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice());
        }
        return cartItemRepository.save(item);
    }

    public CartItem isCartItemExist(Cart cart,Product product,String size,Long userId){
        CartItem cartItem = cartItemRepository.isCartItemExist(cart,product,size,userId);

        return cartItem;
    }

    public void removeCartItem(Long userId,Long cartItemId)throws CartItemException,UserException{
        CartItem cartItem = findCartItemById(cartItemId);
        User user = userService.findUserById(cartItem.getUserId());

        User reqUser = userService.findUserById(userId);

        if(user.getId().equals(reqUser.getId())){
           cartItemRepository.deleteById(cartItemId);
        }
        else {
            throw new UserException("User id is wrong");
        }

    }

    public CartItem findCartItemById(Long cartItemId)throws CartItemException {

     Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
        if (opt.isPresent()){
            return opt.get();
        }
        else {
            throw new CartItemException("No item of this id exist" + cartItemId);
        }
    }

}
