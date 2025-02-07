package com.thrifters.repository;

import com.thrifters.model.Rating;
import com.thrifters.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("SELECT r From Review r Where r.product.id=:productId")
    public List<Review> getAllProductsReview(@Param("productId")Long productId);

}
