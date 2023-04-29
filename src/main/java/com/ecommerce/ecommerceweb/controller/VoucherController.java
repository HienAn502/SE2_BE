package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.aGeneral.ApiResponse;
import com.ecommerce.ecommerceweb.model.Voucher;
import com.ecommerce.ecommerceweb.service.ProductService;
import com.ecommerce.ecommerceweb.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public List<Voucher> getVoucher() {
        return voucherService.voucherList();
    }
    @GetMapping("/applicable-vouchers")
    public List<Voucher> getApplicableVoucher() {
        return voucherService.applicableVoucherList();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createVoucher(@RequestBody Voucher voucher) {
        voucherService.createVoucher(voucher);
        return new ResponseEntity<>(new ApiResponse(true, "A new voucher is created!"), HttpStatus.OK);
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<ApiResponse> deleteVoucher(@PathVariable("voucherId") int voucherId) {
        if (!voucherService.findById(voucherId)){
            return new ResponseEntity<>(new ApiResponse(false, "Voucher does not exist!"), HttpStatus.NOT_FOUND);
        }
        voucherService.deleteVoucher(voucherId);
        return new ResponseEntity<>(new ApiResponse(true, "Voucher deleted!"), HttpStatus.OK);
    }

    @PostMapping("/edit/{voucherId}")
    public ResponseEntity<ApiResponse> updateVoucher(@PathVariable("voucherId") int voucherId, @RequestBody Voucher updateVoucher) {
        if (!voucherService.findById(voucherId)){
            return new ResponseEntity<>(new ApiResponse(false, "Voucher does not exist!"), HttpStatus.NOT_FOUND);
        }
        voucherService.editVoucher(voucherId, updateVoucher);
        return new ResponseEntity<>(new ApiResponse(true, "Voucher updated!"), HttpStatus.OK);
    }

    @GetMapping("/apply-voucher/")
    public ResponseEntity<ApiResponse> applyVoucher(@RequestParam("voucher_id") int voucherId, @RequestParam("product_id") int productId) {
        if (!voucherService.findById(voucherId)){
            return new ResponseEntity<>(new ApiResponse(false, "Voucher does not exist!"), HttpStatus.NOT_FOUND);
        }
        if (productService.findById(productId) == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Product does not exist!"), HttpStatus.NOT_FOUND);
        }
        voucherService.applyVoucher(productId, voucherId);
        return new ResponseEntity<>(new ApiResponse(true, "Apply succeed!"), HttpStatus.OK);
    }
}
