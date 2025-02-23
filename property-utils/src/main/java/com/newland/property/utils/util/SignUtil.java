package com.newland.property.utils.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.SortedMap;

public class SignUtil {
    private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * @param params 所有的请求参数都会在这里进行排序加密md5
     * @return 验证签名结果
     */
    public static boolean verifySign(SortedMap<String, String> params, String sign) {

        String urlSign = params.get("sign");
        if(StringUtils.isEmpty(urlSign)){
            urlSign = sign;
        }
        logger.info("params={}",params);
        if (params == null || StringUtils.isEmpty(urlSign)) {
            return false;
        }
        //判断是md5还是rsa
        String paramsSign = getParamsSign(params);
        return !StringUtils.isEmpty(paramsSign) && urlSign.equals(paramsSign);

    }

    /**
     * @param params 所有的请求参数都verify会在这里进行排序加密rsa
     * @return 验证签名结
     */
    public static boolean verifySign(String publicKey, SortedMap<String, String> params, String sign) {
        String urlSign = params.get("sign");
        if(StringUtils.isEmpty(urlSign)){
            urlSign = sign;
        }
        if (params == null || StringUtils.isEmpty(urlSign)) {
            return false;
        }
        String plain_text = getParamsStr(params);
        return verify(publicKey, plain_text, sign);
    }


    /**
     * @param params 所有的请求参数都会在这里进行排序
     * @return 得到待签名字符
     */
    public static String getParamsStr(SortedMap<String, String> params) {
        //要先去掉 Url 里的 Sign
        params.remove("sign");
        //String paramsJsonStr = JSONObject.toJSONString(params);
        String str = "";
        for (Map.Entry entry : params.entrySet()) {
            str = str + (String) entry.getKey() + "=" + (String) entry.getValue() + "&";
        }
        if(str.length() > 0){
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    /**
     * @param params 所有的请求参数都会在这里进行排序加密
     * @return 得到签名
     */
    public static String getParamsSign(SortedMap<String, String> params) {
        //要先去掉 Url 里的 Sign
        params.remove("sign");
        //String paramsJsonStr = JSONObject.toJSONString(params);
        String str = "";
        for (Map.Entry entry : params.entrySet()) {
            str = str + (String) entry.getKey() + "=" + (String) entry.getValue() + "&";
        }
        if(str.length() > 0){
            str = str.substring(0, str.length() - 1);
        }
//        str += Constant.SIGN_SALT;
        return DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
    }

    public static String signater(String privateKey, String plain_text) {
        byte[] signed = null;
        try {
            Signature Sign = Signature.getInstance("SHA256WithRSA");
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Sign.initSign(priKey);
            Sign.update(plain_text.getBytes("UTF-8"));
            signed = Sign.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Base64.encodeBase64String(signed);
    }


    public static boolean verify(String publicKey, String plain_text, String signed) {
        logger.info(publicKey);
        logger.info(plain_text);
        logger.info(signed);
        //MessageDigest messageDigest;
        boolean SignedSuccess=false;
        try {
            Signature verifySign = Signature.getInstance("SHA256WithRSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            verifySign.initVerify(pubKey);
            verifySign.update(plain_text.getBytes("UTF-8"));
            SignedSuccess = verifySign.verify(Base64.decodeBase64(signed));
            logger.info("sign result" + SignedSuccess);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SignedSuccess;
    }
}
