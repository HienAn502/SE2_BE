package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.aGeneral.ApiResponse;
import com.ecommerce.ecommerceweb.datatransferobject.ProductDTO;
import com.ecommerce.ecommerceweb.model.Category;
import com.ecommerce.ecommerceweb.model.Product;
import com.ecommerce.ecommerceweb.repository.ProductRepository;
import com.ecommerce.ecommerceweb.service.CategoryService;
import com.ecommerce.ecommerceweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>(new ApiResponse(true, "A new category is created!"), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<Category> listCategory(){
        return categoryService.listCategory();
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category){
        System.out.println("category id" + categoryId);
        if (!categoryService.findById(categoryId)){
            return new ResponseEntity<>(new ApiResponse(false, "Category does not exist!"), HttpStatus.NOT_FOUND);
        }
        categoryService.editCategory(categoryId, category);
        return new ResponseEntity<>(new ApiResponse(true, "Category updated!"), HttpStatus.OK);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable("categoryName") String categoryName) {
        List<ProductDTO> products = productService.searchProductsByCategory(categoryName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}