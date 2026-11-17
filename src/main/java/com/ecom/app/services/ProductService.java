package com.ecom.app.services;

import com.ecom.app.dtos.ProductRequest;
import com.ecom.app.dtos.ProductResponse;
import com.ecom.app.models.Product;
import com.ecom.app.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::mapToProductResponse);
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Optional<ProductResponse> updatedProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    updateProductFromRequest(product, productRequest);
                    Product savedProduct = productRepository.save(product);
                    return mapToProductResponse(savedProduct);
                });
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return  true;
                })
                .orElse(false);
    }

    public List<ProductResponse> searchProducts(String keywork) {
        return productRepository.searchProduct(keywork).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse response = new ProductResponse();
        product.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setStockQuantity(product.getStockQuantity());
        response.setActive(product.getActive());
        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest){
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }
}
