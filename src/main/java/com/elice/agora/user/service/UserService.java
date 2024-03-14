package com.elice.agora.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elice.agora.token.JwtUtil;
import com.elice.agora.token.TokenDto;
import com.elice.agora.user.dto.LoginDTO;
import com.elice.agora.user.dto.MeDTO;
import com.elice.agora.user.dto.SignUpDTO;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[#?!@$ %^&*-])[A-Za-z0-9#?!@$ %^&*-]{15,30}$";
    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9]{4,8}$";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenDto login(LoginDTO loginDto) throws Exception {
        String email = loginDto.email;
        String password = loginDto.password;

        // 조건 달성 여부 확인
        checkForImproperEmail(loginDto.email);
        checkForExistingEmail(loginDto.email, true); // should exist
        checkForPasswordLogin(password);

        UserEntity userEntity = userRepository.findByEmail(email);
        
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new Exception("유저 정보가 일치하지 않습니다.");
        }

        // JWT Token 발행해서 보내주기
        TokenDto token = jwtUtil.createAllToken(userEntity);

        return token;
    }

    public UserEntity findUserByEmail(String email) throws Exception {
      return userRepository.findByEmail(email);
    }

    public void signUp(SignUpDTO signUpDto) throws Exception {
        // 조건 달성 여부 확인
        checkForImproperEmail(signUpDto.email);
        checkForExistingEmail(signUpDto.email, false); // should not exist
        checkForPasswordSignup(signUpDto.password, signUpDto.password2);
        checkForNickname(signUpDto.nickname);
        
        // Password Encoding
        String encodedPassword = passwordEncoder.encode(signUpDto.password);
        signUpDto.password = encodedPassword;

        // DB에 저장
        UserEntity userEntity = signUpDto.toEntity();
        userRepository.save(userEntity);
    }

    public MeDTO findMe(Long id) throws Exception {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
        MeDTO meDTO = new MeDTO();
            meDTO.email = user.getEmail();
            meDTO.nickname = user.getNickname();
            meDTO.userId = user.getUserId();
            return meDTO;
        } else {
            // Optional이 비어 있을 때의 처리
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        
    }

    private void checkForImproperEmail(String email) throws Exception {
        boolean isNull = email == null || email == "";
        if (isNull) {
            throw new Exception("이메일이 공백입니다.");
        }

        boolean isFormatCorrect = Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
        if (!isFormatCorrect) {
            throw new Exception("이메일이 형식을 지키지 않았습니다.");
        }
    }

    private void checkForExistingEmail(String email, Boolean shouldExist) throws Exception {
        boolean isExistingEmail = userRepository.existsByEmail(email);
        
        if (shouldExist) {
            if (!isExistingEmail) {
                throw new Exception("이메일에 해당하는 유저가 없습니다."); // 404
            }
        } else {
            if (isExistingEmail) {
                throw new Exception("이메일이 중복되었습니다.");
            }
        }
    }

    private void checkForPasswordSignup(String password, String password2) throws Exception {
        boolean isPasswordNull = password == null || password == "";
        if (isPasswordNull) {
            throw new Exception("비밀번호가 공백입니다.");
        }

        boolean isPassword2Null = password2 == null;
        if (isPassword2Null) {
            throw new Exception("비밀번호 확인값이 공백입니다.");
        }

        boolean isPasswordMatching = password.equals(password2);
        if (!isPasswordMatching) {
            throw new Exception("비밀번호와 비밀번호 확인값이 다릅니다.");
        }

        boolean isFormatCorrect = Pattern.compile(PASSWORD_REGEX)
                .matcher(password)
                .matches();
        if (!isFormatCorrect) {
            throw new Exception("비밀번호 조건을 지키지 않았습니다.");
        }
    }

    private void checkForNickname(String nickname) throws Exception {
        boolean isNull = nickname == null || nickname == "";
        if (isNull) {
            throw new Exception("닉네임이 공백입니다.");
        }

        boolean isFormatCorrect = Pattern.compile(NICKNAME_REGEX)
                .matcher(nickname)
                .matches();
        if (!isFormatCorrect) {
            throw new Exception("닉네임이 형식을 지키지 않았습니다.");
        }

        boolean isExistingNickname = userRepository.existsByNickname(nickname);
        if (isExistingNickname) {
            throw new Exception("닉네임이 중복되었습니다.");
        }
    }

    private void checkForPasswordLogin(String password) throws Exception {
        boolean isNull = password == null || password == "";
        if (isNull) {
            throw new Exception("비밀번호가 공백입니다.");
        }
    }
}
