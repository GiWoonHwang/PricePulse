package com.example.dustin.service.lowstprice;

import com.example.dustin.vo.Keyword;
import com.example.dustin.vo.NotFoundException;
import com.example.dustin.vo.Product;
import com.example.dustin.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
public class LowestPriceServiceImpl implements LowestPriceService {

    private RedisTemplate myProdPriceRedis; // RedisTemplate을 사용하여 Redis와의 데이터 작업을 수행

    // 주어진 키에 대해 Redis의 정렬된 집합(ZSet)에서 10개의 요소를 가져오는 메서드
    public Set GetZsetValue(String key) {
        Set myTempSet = new HashSet(); // 결과를 저장할 임시 집합
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9); // Redis에서 범위 내의 값을 가져옴
        return myTempSet; // 가져온 값 반환
    }

    // 주어진 키에 대해 상태와 함께 Redis의 정렬된 집합(ZSet)에서 10개의 요소를 가져오는 메서드
    // 결과가 없으면 예외를 발생시킴
    public Set GetZsetValueWithStatus(String key) throws Exception {
        Set myTempSet = new HashSet(); // 결과를 저장할 임시 집합
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9); // Redis에서 범위 내의 값을 가져옴
        if (myTempSet.size() < 1) {
            throw new Exception("The Key doesn't have any member"); // 결과가 없으면 예외 발생
        }
        return myTempSet; // 가져온 값 반환
    }

    // 주어진 키에 대해 특정 예외와 함께 Redis의 정렬된 집합(ZSet)에서 10개의 요소를 가져오는 메서드
    // 결과가 없으면 NotFoundException을 발생시킴
    public Set GetZsetValueWithSpecificException(String key) throws Exception {
        Set myTempSet = new HashSet(); // 결과를 저장할 임시 집합
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9); // Redis에서 범위 내의 값을 가져옴
        if (myTempSet.size() < 1) {
            throw new NotFoundException("The Key doesn't exist in redis", HttpStatus.NOT_FOUND); // 결과가 없으면 NotFoundException 발생
        }
        return myTempSet; // 가져온 값 반환
    }

    // 새로운 제품을 Redis의 정렬된 집합(ZSet)에 추가하는 메서드
    // 추가된 제품의 랭크를 반환
    public int SetNewProduct(Product newProduct) {
        int rank = 0;
        // Redis의 정렬된 집합에 새로운 제품을 추가 (제품 그룹 ID, 제품 ID, 가격 사용)
        myProdPriceRedis.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        // 추가된 제품의 랭크를 가져옴
        rank = myProdPriceRedis.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();
        return rank; // 제품의 랭크 반환
    }

    // 새로운 제품 그룹을 Redis의 정렬된 집합(ZSet)에 추가하는 메서드
    // 추가된 제품의 수를 반환
    public int SetNewProductGrp(ProductGrp newProductGrp) {
        List<Product> product = newProductGrp.getProductList(); // 제품 그룹의 제품 리스트를 가져옴
        String productId = product.get(0).getProductId(); // 첫 번째 제품의 ID를 가져옴
        double price = product.get(0).getPrice(); // 첫 번째 제품의 가격을 가져옴
        // Redis의 정렬된 집합에 새로운 제품 그룹을 추가 (제품 그룹 ID, 제품 ID, 가격 사용)
        myProdPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price);
        // 추가된 제품 그룹의 총 제품 수를 가져옴
        int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();
        return productCnt; // 제품 수 반환
    }

    // 주어진 키를 Redis에서 삭제하는 메서드
    public void DeleteKey(String key) {
        myProdPriceRedis.delete(key); // Redis에서 주어진 키 삭제
    }

    // 특정 키워드와 관련된 새로운 제품 그룹을 Redis의 정렬된 집합(ZSet)에 추가하는 메서드
    // 추가된 제품 그룹의 랭크를 반환
    public int SetNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        // Redis의 정렬된 집합에 새로운 제품 그룹을 추가 (키워드, 제품 그룹 ID, 점수 사용)
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        // 추가된 제품 그룹의 랭크를 가져옴
        return myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue(); // 제품 그룹의 랭크 반환
    }

    // 주어진 키워드에 따라 최저가 제품 정보를 가져오는 메서드
    public Keyword GetLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo = new Keyword(); // 반환할 키워드 객체 생성
        List<ProductGrp> tempProdGrp = new ArrayList<>(); // 임시 제품 그룹 리스트 생성
        // 키워드를 통해 제품 그룹을 10개 가져옴
        tempProdGrp = GetProdGrpUsingKeyword(keyword);

        // 가져온 정보들을 반환할 키워드 객체에 설정
        returnInfo.setKeyword(keyword); // 키워드 설정
        returnInfo.setProductGrpList(tempProdGrp); // 제품 그룹 리스트 설정
        return returnInfo; // 키워드 객체 반환
    }

    // 주어진 키워드를 사용하여 제품 그룹을 가져오는 메서드
    public List<ProductGrp> GetProdGrpUsingKeyword(String keyword) {
        List<ProductGrp> returnInfo = new ArrayList<>(); // 반환할 제품 그룹 리스트 생성

        // 주어진 키워드로 제품 그룹 ID를 조회하여 리스트에 저장 (최대 10개)
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));

        List<Product> tempProdList = new ArrayList<>(); // 임시 제품 리스트 생성

        // 제품 그룹 ID 리스트
