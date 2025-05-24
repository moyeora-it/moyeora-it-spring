package com.moyeorait.moyeoraitspring.commons;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigurationTest {
    private final String password = "password";

    String jasyptEncrypt(String input){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(password);
        return encryptor.encrypt(input);
    }

    String jasyptDecrypt(String input) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(password);
        return encryptor.decrypt(input);
    }
    @Test
    @DisplayName("문자열 암호화, 복호화 후 동일한 문자열 반환")
    void encryptTest() {
        String target = "target";

        String encryptResult = this.jasyptEncrypt(target);
        System.out.println("encryptResult : " + encryptResult);
        String decryptResult = this.jasyptDecrypt(encryptResult);
        assertThat(decryptResult).isEqualTo(target);

    }
}