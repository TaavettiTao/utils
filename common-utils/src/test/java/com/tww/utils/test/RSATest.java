package com.tww.utils.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import com.tww.utils.BytesUtil;
import com.tww.utils.RSAUtil;
/**
 * 
* @ClassName: RSATest 
* @Description: TODO(RSA私钥签名、公钥验签测试) 
* @author taoww
* @date 2017年6月14日 下午2:30:53 
*
 */
public class RSATest {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
		String respStr="{\"version\":1,\"commandId\":123456,\"tsmSequence\":\"11223\"}";
		String rsaPrivateModulus = "bcacfa861ac6abf81588eef02d69a0ceaa9872e358f9d7cea1094f4371c855e075a2bf41ef437297004a877d98801d94b231244c9836559ea270095585bca50fa94d51b023d9f489a4fbd6575643370c3770ef8bffb3a60c371f1c4eec726d108951f38a6f20bbf5c9c782642aaa4383bfe127a6b06d34a13893c83dd233666f";
		String privateKeyExponent ="4b4e8797eb2c0d0bb21cbbb1e58e597ee39c818eb8bed0224b285a18c35b80db5c589f1a0413d9b2e78cd7d38b80e74da869f0e11987e658b0e122f89dae93faa1a96e5116ff81b57905d726f7c806195826ea5a4af86e4dfde6b05c38cfd198f15dbd75e18fc79ffa1515533d3aa72e8c5495f96838260278326b5daf7e6061";
		byte[] privateSign = RSAUtil.sign(respStr, rsaPrivateModulus,
				privateKeyExponent);
		System.out.println(BytesUtil.bytesToHex(privateSign));
    	
    	String txnInfo="{\"version\":1,\"commandId\":123456,\"tsmSequence\":\"11223\"}";
    	String sign="3384B0283D5B064AAB696FBA62664AB373A17F041C9F7578E98162AAC40284E7322810B85F326B0177BB6FF8B395F2B1C7B1981DC0760FB0BA567560E817FEB9F1F03D8D6A9CBEC872FA351E53D176D70F609FDC6BE828DA0B5F4DFB8FB5A89DB0B675A62EACF763ED459D5987F44169EB17B476AFAA82B2B959044BCE0DD368";
    	String rsaModulus="bcacfa861ac6abf81588eef02d69a0ceaa9872e358f9d7cea1094f4371c855e075a2bf41ef437297004a877d98801d94b231244c9836559ea270095585bca50fa94d51b023d9f489a4fbd6575643370c3770ef8bffb3a60c371f1c4eec726d108951f38a6f20bbf5c9c782642aaa4383bfe127a6b06d34a13893c83dd233666f";
    	String publicKeyExponent="10001";
    	boolean flag=RSAUtil.verify(txnInfo,BytesUtil.hexToBytes(sign), rsaModulus, publicKeyExponent);
    	System.out.println(flag);
    }
}
