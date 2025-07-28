package com.dasith.crud_app.service;

import com.dasith.crud_app.model.Business;
import com.dasith.crud_app.model.Product;
import com.dasith.crud_app.repository.BusinessRepository;
import com.dasith.crud_app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByBusinessId(Long businessId){
        return productRepository.findByBusinessId(businessId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProductForBusiness(Long businessId,Product product) {
        // Find the business by their id
        Business business=businessRepository.findById(businessId)
                .orElseThrow(()->new RuntimeException("Business not Found with id"+businessId));

        //Associate the product with the found business
        product.setBusiness(business);

        // The repository handles saving and updating the relationship.
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}