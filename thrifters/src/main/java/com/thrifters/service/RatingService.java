package com.thrifters.service;


import com.thrifters.exception.ProductException;
import com.thrifters.model.Product;
import com.thrifters.model.Rating;
import com.thrifters.model.User;
import com.thrifters.repository.RatingRepository;
import com.thrifters.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductService productService;

    public Rating createRating(RatingRequest req, User user) throws ProductException{
        Product product = productService.findProductById(req.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setCreatedAt(LocalDateTime.now());
        rating.setRating(req.getRating());
        return ratingRepository.save(rating);
    }

    public List<Rating>getProductsRating(Long productId){
        return ratingRepository.getAllProductsRating(productId);
    }


}
