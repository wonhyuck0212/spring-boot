package com.elice.agora.redis;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    /** 
     * 현재 {@code Redis}에 해당 {@code key}가 존재하는지 여부 확인
     * 
     * @param key 존재 여부를 확인할 {@code key} 값
     * @return 존재하는 경우 {@code true} 반환; 아니면 {@code false}
    */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /** 
     * 현재 {@code Redis}에 저장된 해당 {@code key}의 {@code value}를 {@code classType}의 클래스 형태로 반환
     * 
     * @param <T> {@code classType}의 타입
     * @param key {@code value}를 확인할 {@code key} 값
     * @param classType {@code value} 값을 매핑할 클래스 타입
     * @return 존재하는 경우 {@code true} 반환; 아니면 {@code false}
    */
    public <T> T get(String key, Class<T> classType) throws Exception {
        String jsonResult = (String) redisTemplate.opsForValue().get(key);

        if (StringUtils.isBlank(jsonResult)) {
            return null;
        } else {
            T obj = mapper.readValue(jsonResult, classType);
            return obj;
        }
    }

    /**   
     * 현재 {@code Redis}에 저장된 해당 {@code key}의 {@code value}를 JSON 문자열로부터 {@code List<T>} 형태로 변환하여 반환
     *   
     * @param <T> {@code List}에 포함될 객체의 타입  
     * @param key {@code value}를 확인할 {@code key} 값  
     * @param classType {@code value} 값을 매핑할 객체의 클래스 타입  
     * @return {@code key}에 해당하는 데이터가 존재하고 성공적으로 변환된 경우 {@code List<T>} 반환;   
     *         데이터가 존재하지 않거나 변환에 실패한 경우 {@code null}  
     * @throws Exception JSON 문자열의 역직렬화 과정에서 오류가 발생할 경우 예외를 던짐  
     */  
    public <T> List<T> getList(String key, Class<T> classType) throws Exception {
        String jsonResult = (String) redisTemplate.opsForValue().get(key);

        if (StringUtils.isBlank(jsonResult)) {
            return null;
        } else {
            List<T> list = mapper.readValue(jsonResult, new TypeReference<List<T>>() {});
            return list;
        }
    }

    /** 
     * 현재 {@code Redis}에 해당 {@code key}로 {@code value} 값을 저장
     * 
     * @param key 저장할 {@code key} 값
     * @param value 해당 {@code key}로 저장할 {@code value} 값
    */
    public void set(String key, Object value) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, mapper.writeValueAsString(value));
    }

    /**  
     * 현재 {@code Redis}에 저장된 모든 값을 삭제  
     */  
    public void deleteAll() {  
        Set<String> keys = redisTemplate.keys("*");  
        if (keys != null && !keys.isEmpty()) {  
            redisTemplate.delete(keys);  
        }  
    } 

    // 필요한 메소드를 추가하여 사용할 수 있습니다.

}