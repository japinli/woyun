package cn.signit.merkle;

/**
*Enumeration used to define what hash type the merkle tree is using
* @ClassName HashType
* @author Liwen
* @date 2016年10月27日-上午10:11:33
* @version (版本号)
* @see (参阅)
*/
public enum HashType {
	SHA256,     // Used for all merkle's outside of bitcoin 
	DOUBLE_SHA256   // Used for merkle's inside of bitcoin 
}
