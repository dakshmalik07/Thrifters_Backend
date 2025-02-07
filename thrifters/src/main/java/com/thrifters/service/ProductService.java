package com.thrifters.service;

import com.thrifters.exception.ProductException;
import com.thrifters.model.Category;
import com.thrifters.model.Product;
import com.thrifters.repository.CategoryRepository;
import com.thrifters.repository.ProductRepository;
import com.thrifters.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public Product createProduct(CreateProductRequest req){

        Category topLevel  = categoryRepository.findByName(req.getTopLevelCategory());
        if(topLevel==null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel  = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(),topLevel.getName());
        if(secondLevel==null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel  = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(),secondLevel.getName());
        if(thirdLevel==null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPresent(req.getDiscountPresent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public String deleteProduct(Long productId)throws ProductException{
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    public Product updateProduct(Long productId,Product req)throws ProductException{
        Product product = findProductById(productId);
        if(req.getQuantity()!=0){
            product.setQuantity(req.getQuantity());
        }

        return productRepository.save(product);
    }

    public Product findProductById(Long id)throws ProductException{
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product not found with id: "+id);
    }

    public List<Product> findproductByCategory(String category){
       return null;
    }

    public Page<Product> getAllProduct(String category,List<String>colors,List<String>sizes, Integer minPrice,Integer maxPrice,Integer minDiscount,
                                       String sort,String stock,Integer pageNumber,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        List<Product> products = productRepository.filterProducts(category,minPrice,maxPrice,minDiscount,sort);
         if(!colors.isEmpty()){
             products = products.stream().filter(p->colors.stream().anyMatch(c->c.equalsIgnoreCase(p.getColor())))
                     .collect(Collectors.toList());
         }

         if(stock!=null){
             if (stock.equals("in_stock")){
                 products = products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
             } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
             }
         }

         int startIndex = (int)pageable.getOffset();
         int endIndex = Math.min(startIndex+pageable.getPageSize(),products.size());

         List<Product> pageContent = products.subList(startIndex,endIndex);
         Page<Product>filteredProducts = new PageImpl<>(pageContent,pageable,products.size());
         return filteredProducts;
    }

}
