package com.thrifters.controller;

import com.thrifters.exception.ProductException;
import com.thrifters.model.Product;
import com.thrifters.request.CreateProductRequest;
import com.thrifters.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/find")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products/create")
    public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest req) {
        // Call the service method to create the product
        Product createdProduct = productService.createProduct(req);

        // Return the created product with 201 status
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }



     @GetMapping("/products")
    public ResponseEntity<Page<Product>>findProductByCategoryHandler( @RequestParam String category,
                                                                      @RequestParam List<String> color,
                                                                      @RequestParam List<String> size,
                                                                      @RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice,
                                                                      @RequestParam Integer minDiscount,
                                                                      @RequestParam String sort,
                                                                      @RequestParam String stock,
                                                                      @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer pageSize){
       Page<Product>res =  productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
       return new ResponseEntity<>(res,HttpStatus.ACCEPTED);

     }


     @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId)throws ProductException{
         Product product = productService.findProductById(productId);
         return new ResponseEntity<>(product,HttpStatus.ACCEPTED);
     }
}
