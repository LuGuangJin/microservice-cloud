package tech.jabari.gateway.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class JwtKeyGenerator {

    public static void main(String[] args) throws Exception {
        // 1. 获取 RSA 密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 2. 初始化密钥长度
        keyPairGenerator.initialize(2048);
        // 3. 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 4. 获取私钥和公钥
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 5. 转换为 Base64 编码的字符串（便于打印和配置）
        String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // 6. 打印输出
        System.out.println("=== Private Key (给 auth-service) ===");
        // 通常需要转换为 PKCS#8 格式的PEM字符串
        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println(encodedPrivateKey);
        System.out.println("-----END PRIVATE KEY-----");

        System.out.println("\n=== Public Key (给 api-gateway) ===");
        // 转换为 X.509 格式的PEM字符串
        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(encodedPublicKey);
        System.out.println("-----END PUBLIC KEY-----");
    }
}