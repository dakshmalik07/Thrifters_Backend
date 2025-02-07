package com.thrifters.service;

import com.thrifters.exception.ProductException;
import com.thrifters.model.Cart;
import com.thrifters.model.CartItem;
import com.thrifters.model.Product;
import com.thrifters.model.User;
import com.thrifters.repository.CartRepository;
import com.thrifters.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    public Cart createCart(User user){
         Cart cart = new Cart();
         cart.setUser(user);

         return cartRepository.save(cart);
    }

    public String addCartItem(Long userId, AddItemRequest req)throws ProductException{
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(req.getProductId());

        CartItem isPresent = cartItemService.isCartItemExist(cart,product, req.getSize(),userId );

        if(isPresent==null){

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            int price = req.getQuantity()*product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item added to cart successfully";
    }

    public Cart findUserCart(Long userId){
        Cart cart =  cartRepository.findById(userId).get();

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem=0;

        for(CartItem cartItem : cart.getCartItems()){
             totalPrice+=cartItem.getPrice();
             totalDiscountedPrice+=cartItem.getDiscountedPrice();
             totalItem+=cartItem.getQuantity();
        }

        cart.setDiscount(totalPrice-totalDiscountedPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);
    }



}
