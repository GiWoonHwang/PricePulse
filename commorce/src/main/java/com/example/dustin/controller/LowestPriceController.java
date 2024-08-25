package com.example.dustin.controller;

import com.example.dustin.service.lowstprice.LowestPriceService;
import com.example.dustin.vo.Keyword;
import com.example.dustin.vo.NotFoundException;
import com.example.dustin.vo.Product;
import com.example.dustin.vo.ProductGrp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@RestController  // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequestMapping("/")  // 이 컨트롤러의 요청 경로를 지정합니다.
public class LowestPriceController {

    @Autowired  // LowestPriceService 빈을 자동으로 주입 받습니다.
    private LowestPriceService mlps;

    // Redis에서 특정 키로 저장된 값을 가져오는 GET 요청을 처리합니다.
    @GetMapping("/product")
    public Set GetZsetValue(String key){
        return mlps.GetZsetValue(key);  // 주어진 키로 Redis에서 ZSet 값을 가져옵니다.
    }

    // Redis에서 특정 키로 저장된 값을 가져오고, 결과가 없으면 404 상태 코드를 반환합니다.
    @GetMapping("/product1")
    public Set GetZsetValueWithStatus(String key){
        try {
            return mlps.GetZsetValueWithStatus(key);
        } catch (Exception ex) {
            // 값이 없는 경우, 404 NOT FOUND 상태와 함께 예외 메시지를 반환합니다.
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    // Redis에서 특정 키로 저장된 값을 가져오고, 예외 발생 시 Exception을 발생시킵니다.
    @GetMapping("/product2")
    public Set GetZsetValueUsingExController(String key) throws Exception {
        try {
            return mlps.GetZsetValueWithStatus(key);
        } catch (Exception ex) {
            // 예외가 발생하면 Exception을 그대로 던집니다.
            throw new Exception(ex);
        }
    }

    // Redis에서 특정 키로 저장된 값을 가져오고, NotFoundException 발생 시 Exception을 발생시킵니다.
    @GetMapping("/product3")
    public ResponseEntity<Set> GetZsetValueUsingExControllerWithSpecificException(String key) throws Exception {
        Set<String> mySet = new HashSet<>();
        try {
            mySet = mlps.GetZsetValueWithSpecificException(key);
        } catch (NotFoundException ex) {
            // NotFoundException이 발생하면 Exception을 던집니다.
            throw new Exception(ex);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        // 성공적으로 값을 가져온 경우, HTTP 200 OK 상태와 함께 값을 반환합니다.
        return new ResponseEntity<Set>(mySet, responseHeaders, HttpStatus.OK);
    }

    // 새로운 Product를 Redis에 저장하는 PUT 요청을 처리합니다.
    @PutMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct) {
        return mlps.SetNewProduct(newProduct);  // 새로운 Product를 저장하고 결과를 반환합니다.
    }

    // 새로운 ProductGrp를 Redis에 저장하는 PUT 요청을 처리합니다.
    @PutMapping("/productGroup")
    public int SetNewProductGroup(@RequestBody ProductGrp newProductGrp) {
        return mlps.SetNewProductGrp(newProductGrp);  // 새로운 ProductGrp를 저장하고 결과를 반환합니다.
    }

    // 특정 키워드에 새로운 ProductGrp를 추가하는 PUT 요청을 처리합니다.
    @PutMapping("/productGroupToKeyword")
    public int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        return mlps.SetNewProductGrpToKeyword(keyword, prodGrpId, score);  // ProductGrp를 키워드에 추가하고 결과를 반환합니다.
    }

    // 키워드에 따른 가장 낮은 가격의 Product를 가져오는 GET 요청을 처리합니다.
    @GetMapping("/productPrice/lowest")
    public Keyword GetLowestPriceProductByKeyword(String keyword) {
        return mlps.GetLowestPriceProductByKeyword(keyword);  // 키워드에 따른 가장 낮은 가격의 Product를 반환합니다.
    }
}
