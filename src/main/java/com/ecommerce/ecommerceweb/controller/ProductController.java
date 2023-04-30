package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.aGeneral.ApiResponse;
import com.ecommerce.ecommerceweb.datatransferobject.ProductDTO;
import com.ecommerce.ecommerceweb.model.Category;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.repository.CategoryRepository;
import com.ecommerce.ecommerceweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepository categoryRepository;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDTO productDTO){
        Optional<Category> optionalCategory = categoryRepository.findById(productDTO.getCategoryId());
        if (!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "Category not exist!"), HttpStatus.BAD_REQUEST);
        }
        productService.createProduct(productDTO, optionalCategory.get());
        return new ResponseEntity<>(new ApiResponse(true, "Product added!"), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> getProducts(){
        List<ProductDTO> listAllProducts = productService.getAllProducts();
        return new ResponseEntity<>(listAllProducts, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") int productId) {
        return new ResponseEntity<>(productService.findProductDTOById(productId), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> sortProducts(@RequestParam("sort_by") String sort_by, @RequestParam("order") String order) {
        if (!sort_by.matches("(name|price)") || !order.matches("(ASC|DESC)"))  {
            return new ResponseEntity<List<ProductDTO>>(new ArrayList<ProductDTO>(), HttpStatus.BAD_REQUEST);
        }
        List<ProductDTO> productDTOS = productService.sortProducts(sort_by, order);
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDTO productDTO) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(productDTO.getCategoryId());
        if (!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "Category not exist!"), HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(new ApiResponse(true, "Product updated!"), HttpStatus.OK);
    }

    @GetMapping("/search/")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(value = "keyword") String keyword) {
        List<ProductDTO> products = productService.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") int productId) {
        if (productService.delete(productId)) {
            return new ResponseEntity<>(new ApiResponse(true, "Deleted succeed!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Product id does not exist!"), HttpStatus.NOT_FOUND);
        }
    }
}
