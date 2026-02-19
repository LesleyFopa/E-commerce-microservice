package org.lesley.ecommerce.controller;

import jakarta.validation.Valid;
import org.lesley.ecommerce.dtos.*;
import org.lesley.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> createProduct(
            @RequestBody  @Valid ProductRequest productRequest
    ) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody @Valid List<ProductPurchaseRequest> productPurchaseRequests
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(productPurchaseRequests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable Integer id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping("/category")
    public ResponseEntity<Integer> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createCategory(request));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findCategoryById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponse>> findCategoryAll() {
        return ResponseEntity.ok(productService.findCategoryAll());
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoryRequest request) {
        productService.updateCategory(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        productService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
