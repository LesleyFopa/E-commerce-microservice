package org.lesley.ecommerce.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.lesley.ecommerce.dtos.*;
import org.lesley.ecommerce.entity.Category;
import org.lesley.ecommerce.exception.ProductPurchaseException;
import org.lesley.ecommerce.mapper.CategoryMapper;
import org.lesley.ecommerce.mapper.ProductMapper;
import org.lesley.ecommerce.repository.CategoryRepository;
import org.lesley.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Integer createProduct( ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts( List<ProductPurchaseRequest> productPurchaseRequests) {
        var productIds = productPurchaseRequests
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products not found");
        }

        var storedRequest = productPurchaseRequests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for(int i=0 ; i<storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Product with id " + product.getId() + " does not have enough stock");
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse findById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public Integer createCategory(CategoryRequest request) {
        Category category = categoryMapper.toCategory(request);
        return categoryRepository.save(category).getId();
    }

    public CategoryResponse findCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::fromCategory)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    public List<CategoryResponse> findCategoryAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::fromCategory)
                .collect(Collectors.toList());
    }

    public void updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        category.setName(request.name());
        category.setDescription(request.description());
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }


}
