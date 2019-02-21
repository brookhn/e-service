package com.pp.server.thread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;


/**
 * 
 * @类名：RSA .
 * @描述: *****  .
 * @作者: yakunMeng .
 * @创建时间: 2017年9月6日 上午10:38:30 .
 * @版本号: V1.0 .
 */
public class RSA {

    /**
     * encrypt algorithm
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * signature algorithm
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * public key map key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * private key map key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * public/private key size
     */
    private static final Integer KEY_SIZE = 1024;

    /**
     * init a public/private key pair
     */
    public static Map<String, Object> initKey(){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // get public key
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // get private key
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }
    }
    /**
     * generate signed data with private key
     * @param signing original data
     * @param privateKey selves' private key
     * @param charset 字符编码
     * @return signed data
     */
    public static String sign(String signing, String privateKey, String charset){
        try {
            return sign(signing.getBytes(charset), privateKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * generate signed data with private key
     * @param data original data
     * @param privateKey selves' private key
     * @return signed data
     */
    public static String sign(byte[] data, String privateKey){
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(privateKey);

            // construct PKCS8EncodedKeySpec obejct
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

            // get key factory of KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            // generate PrivateKey object
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // sign data with private key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data);

            // encode signed data with Base64
            return base64Encode(signature.sign());

        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    /**
     * verify signed data with public key
     * @param signing original data
     * @param signed signed data
     * @param publicKey public key
     * @param charset charset
     * @return true if verify successfully, or false
     */
    public static boolean verify(String signing, String signed, String publicKey, String charset) {
        try {
            return verify(signing.getBytes(charset), signed, publicKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * verify signed data with public key
     * @param data original data
     * @param signed signed data
     * @param publicKey others' public key
     * @return true if verify successfully, or false
     */
    public static boolean verify(byte[] data, String signed, String publicKey) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(publicKey);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            // KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            // get key factory of KEY_ALGORITHM
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            // sign data with public key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);

            // 验证签名是否正常
            return signature.verify(base64Decode(signed));
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * decrypt data with private key
     * @param data encrypted data
     * @param key private key
     * @return decrypted data
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) {
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // get PKCS8EncodedKeySpec object
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // decrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * decrypt data with public key
     * @param data encrypted data
     * @param key public key
     * @return decrypted data
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // decrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * encrypt data with public key
     * @param data original data
     * @param key public key
     * @return encrypted data
     */
    public static byte[] encryptByPublicKey(byte[] data, String key){
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec data
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // encrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * encrypt data with private key
     * @param data original data
     * @param key private key
     * @return encrypted data
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key){
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct PKCS8EncodedKeySpec data
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // encrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * get encoded private key
     * @param keyMap key pair
     * @return encoded private key
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return base64Encode(key.getEncoded());
    }

    /**
     * get encoded public key
     * @param keyMap key pair
     * @return encoded public key
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return base64Encode(key.getEncoded());
    }

    /**
     * encode with Base64
     * @param key key
     * @return encoded key
     */
    public static String base64Encode(byte[] key){
        return Base64.encode(key);
    }

    /**
     * decode with Base64
     * @param key key
     * @return decoded key
     */
    public static byte[] base64Decode(String key) {
        return Base64.decode(key);
    }
    
    /**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static String loadPrivateKeyByFile(String path) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/private_key.key"));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static String loadPublicKeyByFile(String path) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/public_key.key"));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}
	public static void main(String[] args) {
		
		System.out.println("Hello");
		
	}
		
		public static String getRSA(String token) {
		//线上的公私钥
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUjU67OKwnON57U4c/OaZ8ajXTnFAQ0+wUXl+2yUiIXmzc1DoyMJJdza1Mip76+v1412/dc5h9ngxQn89hfqAoD0l+mkxYnmAFShSmLIRoRngLa0yW9nOw4B+RnhE2dxrJkyhF+RD1h0z7oNVy4PSSn26RDlSd8e7bNB2wShvZ7QIDAQAB";
		String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKo7CIx9DmFD3qtpCAfmJmLa0k59GdxMEd3te/hQQXfPXR9DvjUTNlfMyWwG3KC16dbMpDqmt+GCTdsubexPHMfJNY7oB+P/IuJNdb0vQZccedYUlTCXnC/Pf3jzLGtytJ0lrVEW28xTQIw5HWqwKYJPGnG2/RsY0dmVr5ThSUpJAgMBAAECgYAOmF8kRHieKEaIJco1eyshYKeVaS51b+Q8RedSI/dpAVN3ocRGIq6PGpTOeUwjT1WMQCF4keDEKfro5vMvxpyt9IY/0w95ysQlbHgenArkmN79LCsIo5iLmNJz6FaANn/cYKeWqyzkK66TcbJcnn+SSLSpqQ4dEhvNf/qj/l0oTQJBAORpFaoP+hK2Eeuay8qL9B4JNmnX8neWeC+2iRZ6zVwiLuJEDJeTfY7BtVfGkPKjhDbyh794xwbbgXXS8wGxqSMCQQC+yuifFolfGgX6XUkiTl6HRkv3c88olEPLbcjICWLQom2CyUrujy7N2MS8K9+UDi4xWlT0M4uY6niSqnjqPxOjAkADbCes8qxWl2/RTfJ7yHkR8iVklJ3HePO4E04ejhyBoul87nQZ2VXqS7DB7fHCEP0sLZonHlItIiDgeRa3EchtAkEArqZfuwGOTpgr0LuELvs+XOZks+g3QhY22QluOm8dHY8SYB6TkT1Qwu4jaX1f9M/mSRc/nStyJlC8fsW+We/TsQJBAJj/vWEoLO9hOdWUiUL2c871Ykxp196lAZX6ADUYo9oFNW29ZHocV8h+eZfA8NIM6SLh2cNlpsne9R5SA3eaDPc=";
		String sign = "";
		boolean flag = false;
		try {
			byte[] decrypt=RSA.decryptByPublicKey(RSA.base64Decode(token), publicKey);
			System.out.println(new String(decrypt));
		    
			String encrypt2=RSA.base64Encode(RSA.encryptByPublicKey(new String(decrypt).getBytes(), publicKey));
			sign=new String(encrypt2);
			System.out.println(encrypt2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
		
	}
}
