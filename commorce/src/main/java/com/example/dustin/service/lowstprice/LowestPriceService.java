package com.example.dustin.service.lowstprice;

import com.example.dustin.vo.Keyword;
import com.example.dustin.vo.Product;
import com.example.dustin.vo.ProductGrp;

import java.util.Set;

// 최저가 관련 서비스를 제공하는 인터페이스
public interface LowestPriceService {

    // 특정 키에 대해 정렬된 집합의 값을 가져옴
    Set GetZsetValue(String key);

    // 특정 키에 대해 상태와 함께 정렬된 집합의 값을 가져옴
    // 이 메서드는 예외 발생 시 이를 호출자에게 전달
    Set GetZsetValueWithStatus(String key) throws Exception;

    // 특정 키에 대해 특정 예외와 함께 정렬된 집합의 값을 가져옴
    // 메서드 호출 시 특정 예외를 발생시킬 수 있음
    Set GetZsetValueWithSpecificException(String key) throws Exception;

    // 새로운 제품을 추가하고, 성공적으로 추가된 경우 정수 값을 반환
    // 예를 들어, 성공적으로 추가되면 1을 반환, 실패하면 0을 반환할 수 있음
    int SetNewProduct(Product newProduct);

    // 새로운 제품 그룹을 추가하고, 성공적으로 추가된 경우 정수 값을 반환
    // 예를 들어, 성공적으로 추가되면 1을 반환, 실패하면 0을 반환할 수 있음
    int SetNewProductGrp(ProductGrp newProductGrp);

    // 특정 키워드와 관련된 새로운 제품 그룹을 추가하고, 성공적으로 추가된 경우 정수 값을 반환
    // 예를 들어, 성공적으로 추가되면 1을 반환, 실패하면 0을 반환할 수 있음
    int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score);

    // 특정 키워드에 따라 최저가 제품 정보를 가져옴
    // 키워드에 해당하는 최저가 제품을 포함하는 Keyword 객체를 반환
    Keyword GetLowestPriceProductByKeyword(String keyword);
}
