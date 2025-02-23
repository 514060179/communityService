package com.newland.property.utils.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class RSAUtils {

    private static int getKeyLength(RSAPublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = publicKey.getAlgorithm();
        KeyFactory keyFact = KeyFactory.getInstance(algorithm);
        RSAPublicKeySpec keySpec = keyFact.getKeySpec(publicKey, RSAPublicKeySpec.class);
        BigInteger prime = keySpec.getModulus();
        int len = prime.toString(2).length();
        return len;
    }

    public static byte[] signWithSHA256(String source, Object privateKeyFilePath) throws Exception {
        RSAPrivateKey privateKey = loadPrivateKey(privateKeyFilePath);
        Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initSign(privateKey);
        sig.update(source.getBytes("utf-8"));
        return sig.sign();
    }

    public static byte[] signWithSHA1(String source, Object privateKeyFilePath) throws Exception {
        RSAPrivateKey privateKey = loadPrivateKey(privateKeyFilePath);
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(source.getBytes("utf-8"));
        return sig.sign();
    }

    public static boolean verifySignatureWithSHA256(String source, byte[] sign, String publicKeyFilePath)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        RSAPublicKey publicKey = loadPublicKey(publicKeyFilePath);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(publicKey);
        signature.update(source.getBytes("utf-8"));
        return signature.verify(sign);
    }

    public static boolean verifySignatureWithSHA1(String source, byte[] sign, String publicKeyFilePath)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        RSAPublicKey publicKey = loadPublicKey(publicKeyFilePath);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(source.getBytes("utf-8"));
        return signature.verify(sign);
    }

    public static RSAPrivateKey loadPrivateKey(Object privateKey)
            throws Exception {
        if (privateKey instanceof File) {
            return loadPrivateKeyByFile((File) privateKey);
        } else if (privateKey instanceof String) {
            return loadPrivateKeyByStr((String) privateKey);
        } else {
            throw new Exception("加载公钥参数类型异常");
        }
    }

    private static RSAPrivateKey loadPrivateKeyByFile(File privateKeyFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = FileUtils.readFileToString(privateKeyFile, "utf-8");
        return loadPrivateKeyByStr(key);
    }

    private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
        return privateKey;
    }

    public static RSAPublicKey loadPublicKey(Object publicKey)
            throws Exception {
        if (publicKey instanceof File) {
            if (((File) publicKey).getName().endsWith(".crt")) {
                return loadCerPublicKey((File) publicKey);
            } else {
                return loadPublicKey((File) publicKey);
            }
        } else if (publicKey instanceof String) {
            return loadPublicKey(publicKey);
        } else {
            throw new Exception("加载公钥参数类型异常");
        }
    }

    private static RSAPublicKey loadCerPublicKey(File publicKey)
            throws FileNotFoundException, CertificateException {
        CertificateFactory of = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream(publicKey);
        java.security.cert.Certificate ceof = of.generateCertificate(fis);
        return (RSAPublicKey) ceof.getPublicKey();
    }

    private static RSAPublicKey loadPublicKey(File publicKey)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String publicKeyStr = FileUtils.readFileToString(publicKey, "utf-8");
        publicKeyStr = publicKeyStr.replaceAll("-----BEGIN PUBLIC KEY-----", "");
        publicKeyStr = publicKeyStr.replaceAll("-----END PUBLIC KEY-----", "");
        return loadPublicKey(publicKeyStr);
    }

    public static RSAPublicKey loadPublicKey(String publicKeyStr)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }


    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }


    public static String decryptByPrivateKey(String privateKeyText, String text, String charsetName) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return new String(result, charsetName);
    }
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static String decrypt(String data, String privateKeyText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);

        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > 256) {
                cache = cipher.doFinal(dataBytes, offset, 256);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * 256;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    public static String getSignStr(Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map map = BeanUtils.describe(obj);

        if (map.containsKey("defaultUI")) {
            Object defaultUI = map.get("defaultUI");
            map.remove("defaultUI");
            map.put("DefaultUI", defaultUI);
        }

        final TreeMap<String, Object> sortedParas = new TreeMap<String, Object>(map);
        String signStr = "";
        Iterator iterator = sortedParas.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if ("class".equalsIgnoreCase(key)) {
                continue;
            }

            if ("serverSign".equalsIgnoreCase(key)) {
                continue;
            }

            if ("merchantSign".equalsIgnoreCase(key)) {
                continue;
            }

            Object objVal = sortedParas.get(key);
            if (null == objVal) {
                continue;
            }

            String strVal = String.valueOf(objVal);
            if (StringUtils.isEmpty(strVal)) {
                continue;
            }
            signStr += strVal;
        }
        return signStr;
    }

    public static boolean verifyResponseSignature(String signStr, String signValue, String publicKey){
//        String publicKey = getBocPublicKey();
//        String signStr = getSignStr(obj);
//        BocChannelRespone responeHead = (BocChannelRespone) obj;
//        String signValue = responeHead.getServerSign();
        try {
            return RSAUtils.verifySignatureWithSHA256(signStr, Base64Util.decode(signValue), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("boc 验证失败 signStr=%s，signValue=%s", signStr, signValue));
            return false;
        }
    }
}
