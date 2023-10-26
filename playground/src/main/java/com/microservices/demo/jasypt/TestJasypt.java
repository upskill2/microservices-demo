package com.microservices.demo.jasypt;

import org.jasypt.encryption.pbe.StandardPBEBigDecimalEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TestJasypt {

    public static void main (String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor ();
        encryptor.setPassword("sectret123");
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator (new RandomIvGenerator ());

        String encryptedText = encryptor.encrypt("123");
        String plainText = encryptor.decrypt(encryptedText);
        System.out.println (encryptedText);
        System.out.println (plainText);

    }
}
