package com.elice.agora.token;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.user.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TIME =  7 * 24 * 60 * 60 * 1000L;

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String AUTHORIZATION = "Authorization";

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Autowired
    private UserRepository userRepository;

    // bean으로 등록 되면서 딱 한번 실행이 됩니다.
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }

    // 토큰 생성
    public TokenDto createAllToken(UserEntity userEntity) {
        return new TokenDto(createToken(userEntity, "Access"), createToken(userEntity, "Refresh"));
    }

    public String createToken(UserEntity userEntity, String type) {
        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;
        long currTime = date.getTime();
        Date expireDate = new Date(currTime + time);

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("auth", userEntity.getRole());
        payload.put("sub",userEntity.getEmail());
        // payload.put("iat", currTime);
        // payload.put("exp", expireTime);

        return Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setIssuedAt(new Date(currTime))
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // 토큰 검증
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    // refreshToken 토큰 검증
    // db에 저장되어 있는 token과 비교
    // db에 저장한다는 것이 jwt token을 사용한다는 강점을 상쇄시킨다.
    // db 보다는 redis를 사용하는 것이 더욱 좋다. (in-memory db기 때문에 조회속도가 빠르고 주기적으로 삭제하는 기능이 기본적으로 존재합니다.)
    public Boolean refreshTokenValidation(String token) throws Exception {

        // 1차 토큰 검증
        if(!tokenValidation(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(getEmailFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    // 인증 객체 생성
    // public Authentication createAuthentication(String email) {
    //     UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    //     // spring security 내에서 가지고 있는 객체입니다. (UsernamePasswordAuthenticationToken)
    //     return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    // }

    // 토큰에서 email 가져오는 기능
    public String getEmailFromToken(String token) throws Exception {
        // System.out.println("+++++" + Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody());
        
        System.out.println("********************token : " + token + "*****************");
        String result =  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.substring(7)).getBody().getSubject();
        System.out.println("********************result : " + result + "*****************");
        
        if (result == null || result == "") {
            throw new Exception("토큰 해독 중 null");
        }
        return result;
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }

    public UserEntity getUserFromToken(HttpServletRequest request) throws Exception {
        // 토큰이 Valid 하고, 항상 존재하는 유저를 가져온다고 가정
        String token = request.getHeader(AUTHORIZATION);

        System.out.println("=====" + token);

        if (token == null) {
            throw new Exception("토큰이 유효하지 않습니다.");
        }

        String email = getEmailFromToken(token);
        System.out.println("=====" + email);
        UserEntity userEntity = userRepository.findByEmail(email);

        return userEntity;
    }
}

// if(accessToken != null) {
//     // 어세스 토큰값이 유효하다면 setAuthentication를 통해
//     // security context에 인증 정보저장
//     if(jwtUtil.tokenValidation(accessToken)){
//         setAuthentication(jwtUtil.getEmailFromToken(accessToken));
//     }
//     // 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
//     else if (refreshToken != null) {
//         // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
//         boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
//         // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
//         if (isRefreshToken) {
//             // 리프레시 토큰으로 아이디 정보 가져오기
//             String loginId = jwtUtil.getEmailFromToken(refreshToken);
//             // 새로운 어세스 토큰 발급
//             String newAccessToken = jwtUtil.createToken(loginId, "Access");
//             // 헤더에 어세스 토큰 추가
//             jwtUtil.setHeaderAccessToken(response, newAccessToken);
//             // Security context에 인증 정보 넣기
//             setAuthentication(jwtUtil.getEmailFromToken(newAccessToken));
//         }
//         // 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
//         else {
//             jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
//             return;
//         }
//     }
// }
