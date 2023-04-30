package com.ecommerce.ecommerceweb.service;

import com.ecommerce.ecommerceweb.datatransferobject.ProductDTO;
import com.ecommerce.ecommerceweb.exception.CategoryNotExistException;
import com.ecommerce.ecommerceweb.exception.ProductNotExistException;
import com.ecommerce.ecommerceweb.model.Category;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.repository.CategoryRepository;
import com.ecommerce.ecommerceweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

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
        productDTO.setDiscount_price(product.getDiscountPrice());
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

    public ProductDTO findProductDTOById(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return getProductDTO(product);
    }

    public List<ProductDTO> sortProducts(String sort_by, String order) {
        List<Product> products = new ArrayList<>();
        if (order.equals("ASC"))
            products = productRepository.findAll(Sort.by(Sort.Direction.ASC, sort_by));
        else if (order.equals("DESC"))
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, sort_by));

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product: products){
            productDTOS.add(getProductDTO(product));
        }
        return productDTOS;
    }

    public List<ProductDTO> searchProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName.toLowerCase());
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> products = productRepository.searchProductByCategory(category, pageable);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product: products){
            productDTOS.add(getProductDTO(product));
        }
        return productDTOS;
    }

    public List<ProductDTO> searchProducts(String keyword) {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> products = productRepository.searchProducts(keyword.toLowerCase(), pageable);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product: products){
            productDTOS.add(getProductDTO(product));
        }
        return productDTOS;
    }

    public boolean delete(int productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            return false;
        }
        productRepository.delete(product.get());
        return true;
    }

}
