package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.ProductDTO;
import com.ecommerce.ecommerceweb.exception.ProductNotExistException;
import com.ecommerce.ecommerceweb.model.Category;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public void createProduct(ProductDTO productDTO, Category category) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        productRepository.save(product);
    }

    public ProductDTO getProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setId(product.getId());
        return productDTO;
    }

    public List<ProductDTO> getAllProducts(){
        List<Product> allProducts = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product: allProducts){
            productDTOS.add(getProductDTO(product));
        }
        return productDTOS;
    }

    public void updateProduct(ProductDTO productDTO, Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()){
            throw new Exception("product not exist");
        }
        Product product = optionalProduct.get();
        product.setName(productDTO.getName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        productRepository.save(product);
    }

    public Product findById(Integer productId) throws ProductNotExistException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new ProductNotExistException("product does not exists:" + productId);
        }
        return product.get();
    }
}
