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

//import com.rsa.pkg.RSA;

//import com.clark.order.pkg.RSA;


/**
 * 
 * @类名：RSA .
 * @描述: *****  .
 * @作者: yakunMeng .
 * @创建时间: 2017年9月6日 上午10:38:30 .
 * @版本号: V1.0 .
 */
public class RSA11 {

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
    
//    public static String sign2(String signing2, String privateKey, String charset){
//        try {
//            return sign2(signing2.getBytes(charset), privateKey);
//        } catch (UnsupportedEncodingException e) {
//            throw new SecurityException(e);
//        }
//    }

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
//		Map<String, String> parameters = new TreeMap<String, String>();
//		parameters.put("partnerNo", "test");
//		parameters.put("goodsNum", "11");
//		parameters.put("goodsNo", "1");
//		parameters.put("contractType", "1");
//		parameters.put("comment", "中文");
//		parameters.put("notifyUrl", "http://www.baidu.com");
//		parameters.put("format", "jsonp");
//		parameters.put("cb", "aa");
//		StringBuilder paramStingBuilder = new StringBuilder();
//		for (String key : parameters.keySet()) {
//			paramStingBuilder.append(key).append("=").append(parameters.get(key)).append("&");
//		}
//		paramStingBuilder=new StringBuilder("comment=体育包商品&contractType=1&goodsNo=AB9596358360947&goodsNum=1&notifyUrl=http://10.200.218.184:8080/ddp/feedback&partnerNo=OTT");
//测网公钥	String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqOwiMfQ5hQ96raQgH5iZi2tJOfRncTBHd7Xv4UEF3z10fQ741EzZXzMlsBtygtenWzKQ6prfhgk3bLm3sTxzHyTWO6Afj/yLiTXW9L0GXHHnWFJUwl5wvz3948yxrcrSdJa1RFtvMU0CMOR1qsCmCTxpxtv0bGNHZla+U4UlKSQIDAQAB";
		
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqOwiMfQ5hQ96raQgH5iZi2tJOfRncTBHd7Xv4UEF3z10fQ741EzZXzMlsBtygtenWzKQ6prfhgk3bLm3sTxzHyTWO6Afj/yLiTXW9L0GXHHnWFJUwl5wvz3948yxrcrSdJa1RFtvMU0CMOR1qsCmCTxpxtv0bGNHZla+U4UlKSQIDAQAB";
//压测网公钥	
		String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKo7CIx9DmFD3qtpCAfmJmLa0k59GdxMEd3te/hQQXfPXR9DvjUTNlfMyWwG3KC16dbMpDqmt+GCTdsubexPHMfJNY7oB+P/IuJNdb0vQZccedYUlTCXnC/Pf3jzLGtytJ0lrVEW28xTQIw5HWqwKYJPGnG2/RsY0dmVr5ThSUpJAgMBAAECgYAOmF8kRHieKEaIJco1eyshYKeVaS51b+Q8RedSI/dpAVN3ocRGIq6PGpTOeUwjT1WMQCF4keDEKfro5vMvxpyt9IY/0w95ysQlbHgenArkmN79LCsIo5iLmNJz6FaANn/cYKeWqyzkK66TcbJcnn+SSLSpqQ4dEhvNf/qj/l0oTQJBAORpFaoP+hK2Eeuay8qL9B4JNmnX8neWeC+2iRZ6zVwiLuJEDJeTfY7BtVfGkPKjhDbyh794xwbbgXXS8wGxqSMCQQC+yuifFolfGgX6XUkiTl6HRkv3c88olEPLbcjICWLQom2CyUrujy7N2MS8K9+UDi4xWlT0M4uY6niSqnjqPxOjAkADbCes8qxWl2/RTfJ7yHkR8iVklJ3HePO4E04ejhyBoul87nQZ2VXqS7DB7fHCEP0sLZonHlItIiDgeRa3EchtAkEArqZfuwGOTpgr0LuELvs+XOZks+g3QhY22QluOm8dHY8SYB6TkT1Qwu4jaX1f9M/mSRc/nStyJlC8fsW+We/TsQJBAJj/vWEoLO9hOdWUiUL2c871Ykxp196lAZX6ADUYo9oFNW29ZHocV8h+eZfA8NIM6SLh2cNlpsne9R5SA3eaDPc=";
//张君尧卡密加密公私钥，先用RSA加密后用Base64加密，所以解密先用Base64解密后用RSA解密
		String cdkey_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWO25gI0s4+Dw/ePVBnPd+IBVCzgcZgGwp7t6mFWLgAcRtMZtZ6H4fXp7aGoBUkfJCcrcmM1mvchDlDI3Sn8UfjKHieaOTati13Su3BzZaIZmWhiBT98oBmLe3Q9Jk8WGdaahDatOoCYeNFiClIKBjBskuHYk8aqOo5te4MM3K9wIDAQAB";
		String cdkey_private_key="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJY7bmAjSzj4PD949UGc934gFULOBxmAbCnu3qYVYuABxG0xm1nofh9entoagFSR8kJytyYzWa9yEOUMjdKfxR+MoeJ5o5Nq2LXdK7cHNlohmZaGIFP3ygGYt7dD0mTxYZ1pqENq06gJh40WIKUgoGMGyS4diTxqo6jm17gwzcr3AgMBAAECgYAhKuvac9BWuo9qoATdwI7vnJu1DRGKMs0ukNs9JbmXpSw9w1b+CZNWz6gYlQNCKJqoyvTejTW+w1sxho0FNFrrnd4mktftb14gGhAHOpC3Xvk5sN16FkE+iONAOBHZ5x9AZM/V0FLBOR6C8snGPRpMDmzzTijJ+Z5gQbx1h1OmmQJBAOAYsMtivgZUXVjKY4Q1Sgkcy70r2r68ejPKVMaz7cOHGb8LQjI/sqMhxaau2fEqh9JB89NWY5lpgENtSZC1fh0CQQCrnrf4e/VpaHQLSXiM5UAnw3/RcdRLIRVXxHoDx3g0riqiWLvusxs2UA0EP15AdDC4aWbtZa0yZBNej0OnIjEjAkEAmciX2priaJmQjZQmAt5G9kXqkuD7dhUFj3D0CIAFVD7sLixduvn3bveIGcC4E0XqeyGDX65THz5FJQBkkYfbCQJAR0kYNZiXqX7/pKiR8ERRT7L+5qqXedV2Hgy4edVi8s7dWq3s8gtnZDoADiGcLr2UdyUg4b74IBQ9j2uGuGeVbQJANmcW3ud4pvVD57uyfKiF2i9Qxg4hZHmeCQ9Ee3F1OtnTOHJDvX5+PkLqTKk1Cqe98iw6n/GUfk0mfxMfUy1huA==";
		
		String sign = "";
		boolean flag = false;
		try {
			sign = RSA11.sign("partner="+"ftv"+"&thirdUid="+"3CE22EB8E0894624908B33DD9340DA75"+"&type="+0+"&orderId="+"2018021315154347"+"&payOrderId="+"8769820132342123"+"&payTime="+"20171227"+"&payAmount="+"234.00"+"&goodsId="+"CC2288778620413"+"&goodsName="+"体育高级会员包3月"+"&goodsNum="+1+"&goodsPrice="+"234.00"+"&appid=" + "pptv.atv.sports" + "&appplt=" + "atv"+ "&channel=" + "2277", priKey, "utf-8");
//			sign = RSA11.sign("partner="+"ftv"+"&thirdUid="+"fffe9a47301e4891911e2c4b64395dc5"+"&type="+0+"&orderId="+"2018020615053912"+"&payOrderId="+"8769820132342123"+"&payTime="+"20171227"+"&payAmount="+"1.00"+"&goodsId="+"B77765723185694"+"&goodsName="+"体育单片付费测试"+"&goodsNum="+1+"&goodsPrice="+"1.00"+"&appid=" + "pptv.atv.sports" + "&appplt=" + "atv"+ "&channel=" + "2277", priKey, "utf-8");

//			sign="{\"msg_id\": \"6b949cfcc54046488bdbca3ecbfb571a\","
//					+ "\"xiaomi_id\": 131477867, "
//					+ "\"pay_order_id\": 909700225,"
//					+ "\"cust_order_id\":201712201439153,"
//					+ "\"order_fee\": \"10000\", "
//					+ "\"private_data\": \"\", "
//					+ "\"pay_time\": 1369193066}";
//			sign=org.apache.commons.codec.binary.Base64.encodeBase64String(sign.getBytes());
//			System.out.println(sign);
//			String signi = RSA.sign(sign,priKey, "utf-8");
//			System.out.println(signi);
//			String signing = "partner="+"youku"+"&thirdUid="+123+"&type="+0+"&orderId="+"201710141157105"+"&payOrderId="+123+"&payTime="+23+"&payAmount="+100+"&goodsId="+"B76963026914587"+"&goodsName="+"体育包商品2"+"&goodsNum="+1+"&goodsPrice="+100;
			String signing ="partner="+"ftv"+"&thirdUid="+"3CE22EB8E0894624908B33DD9340DA75"+"&type="+0+"&orderId="+"2018021315154347"+"&payOrderId="+"8769820132342123"+"&payTime="+"20171227"+"&payAmount="+"234.00"+"&goodsId="+"CC2288778620413"+"&goodsName="+"体育高级会员包3月"+"&goodsNum="+1+"&goodsPrice="+"234.00"+"&appid=" + "pptv.atv.sports" + "&appplt=" + "atv"+ "&channel=" + "2277";
			System.out.println("sing="+sign);
			flag = RSA11.verify(signing, sign, publicKey, "UTF-8");
			System.out.println("flag="+flag);
			
//			sign2 = RSA11.sign("777777421aa", priKey, "utf-8");
//			String signing2 ="777777421aa";
//			System.out.println(sign2);
//			flag = RSA11.verify(signing2, sign, publicKey, "UTF-8");
//			System.out.println(flag2);
			
			
//			sign(paramStingBuilder.toString(), RSAUtil.getPrivateKey(priKey));
//			sign = RSA.sign("00:25:92:BC:AB:F3", priKey, "utf-8");
//			flag= RSA.verify("00:25:92:BC:AB:F3", sign, publicKey, "utf-8");
//			String encode=RSA.base64Encode(RSA.encryptByPrivateKey("aaa".getBytes(), priKey));
//			System.out.println(encode);
//			byte[] decode=RSA.decryptByPublicKey(RSA.base64Decode(encode), publicKey);
//			System.out.println(new String(decode));
//			 flag=RSA.verify(params, sign, publicKey, "utf-8");
			
//			String encrypt=RSA.base64Encode(RSA.encryptByPrivateKey("dd9c7975bef4e4c611673cfa4b1ab344".getBytes(), priKey));
//			System.out.println(encrypt);
			byte[] decrypt=RSA11.decryptByPublicKey(RSA11.base64Decode("WHzYX/E2NhD9r6ahYbShD5D3Nq0oktklCga+oGkQiU9ybh6AwNApNZXMGdxmQItT+8rbrzWC/S+kBS0w+7QetrqeYokFH6F9A21EhkMUbQSyulGTrXNQSp5HOJ3MFOdtUMRfmAIcWoJ8Roo+gy6CSyM5advQSrctzPD4W91SU04="), publicKey);
			System.out.println("=================== "+new String(decrypt));//这两行是会员捆绑领取的线下mac地址
			String encrypt2=RSA11.base64Encode(RSA11.encryptByPublicKey(new String(decrypt).getBytes(), publicKey));
			System.out.println("----- "+encrypt2);
			
	//		byte[] decode=RSA11.decryptByPublicKey(RSA11.base64Decode("G9d5CkUljsCF9nJpJTvDWx7v3zhSw7WCOUt/b8npBJE9zHVo4cFflbLb47JjQ7iZh8I3AtpqomlB9cMrtbtAjtnFGRFWkzNkLEIDWoJPgq+o6YuPldwECy/VnhoY9h9NL2VCfd4x9JnIP2mGcZtaVOECYvmSKkLmHiaQw79RIZE="),cdkey_public_key);
	//		System.out.println(new String(decode));		
			// 上两行是卡密兑换的加解密
//			byte[] decrypt2=RSA11.decryptByPrivateKey(RSA11.base64Decode("eyJleHRfdXNlcl9pZCI6MTIzNDU2NzgsImV4dHJhX2RhdGEiOnsidXNlck5hbWUiOiJOaHJ2WHlIRU16ZUpEVEhGNnhRMk8weTMreEtBcGZrbFBsc1o4d2xwMkFxMEszOHFuMUplNFQrVmdMV0NVZGw4UGd1K0IvZTRickhjTXRmM2hpUWd6NDk5VTBnb0lSNXZiZm1aK2phZy8vRys4d1hyME9LNmE2YTRXTGt5RkxhYnhSSTY2VG5hMm5IVGlvRnlFM2syNmNxbmxKOXo5MmFLa0Uxc01QcEhrNG89In0sIm9yZGVyX2ZlZSI6IjAuMDEiLCJ4aWFvbWlfaWQiOjEzMTQ3Nzg2NywibXNnX2lkIjoiNmI5NDljZmNjNTQwNDY0ODhiZGJjYTNlY2JmYjU3MWEiLCJwYXlfb3JkZXJfaWQiOjI4ODg5MTYwMTQwODEsImN1c3Rfb3JkZXJfaWQiOjI4ODg5MTYwMTQwOCwicGF5X3RpbWUiOjE1MjYwMjAzNDd9"), priKey);
//			System.out.println(new String(decrypt2));
		} catch (Exception e) {
			e.printStackTrace();
		}
								
	}
}
