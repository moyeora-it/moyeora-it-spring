package com.moyeorait.moyeoraitspring.domain.user.repository;

import com.moyeorait.moyeoraitspring.commons.enumdata.Position;
import com.moyeorait.moyeoraitspring.commons.enumdata.Provider;
import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User저장 및 조회 테스트")
    void saveAndFindUser(){
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user.setNickname("테스트유저");
        user.setProfileImage("https://example.com/image.jpg");
        user.setProvider(Provider.KAKAO);
        user.setProviderId(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsDeleted(false);
        user.setPosition(Position.BE);
        user.setSkills(List.of("Java", "Spring"));
        user.setEmailAuthentication(1);

        User saved = userRepository.save(user);
        User found = userRepository.findById(saved.getId()).orElseThrow();

        Assertions.assertThat(found.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(found.getSkills()).isEqualTo(user.getSkills());
        Assertions.assertThat(found.getPosition()).isEqualTo(Position.BE);
    }
}