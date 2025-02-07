package com.thrifters.service;


import com.thrifters.exception.ProductException;
import com.thrifters.model.Product;
import com.thrifters.model.Review;
import com.thrifters.model.User;
import com.thrifters.repository.ProductRepository;
import com.thrifters.repository.ReviewRepository;
import com.thrifters.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository  reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    public Review createReview(ReviewRequest req, User user)throws ProductException {
        Product product = productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }


    public List<Review> getAllReview(Long productId){
        return reviewRepository.getAllProductsReview(productId);
    }

}
