package com.dasith.crud_app.controller;

import com.dasith.crud_app.model.Product;
import com.dasith.crud_app.service.BusinessService;
import com.dasith.crud_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesse/{businessId}/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductsForBusiness(@PathVariable Long businessId) {
        // Note: Assuming your service returns a List<Product>
        List<Product> products = productService.getProductsByBusinessId(businessId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@PathVariable Long businessId,@RequestBody Product product) {
        Product createProduct=productService.createProductForBusiness(businessId, product);
        return ResponseEntity.ok(createProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}