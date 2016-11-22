package cn.signit.utils.timestamp;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
*时间戳
* @ClassName TSAClient
* @author Liwen
* @date 2016年11月18日-上午9:49:42
* @version (版本号)
* @see (参阅)
*/
public interface TSAClient {
    /**
     * Get the time stamp token size estimate.
     * Implementation must return value large enough to accomodate the entire token
     * returned by getTimeStampToken() _prior_ to actual getTimeStampToken() call.
     * @return	an estimate of the token size
     */
    public int getTokenSizeEstimate();
    
    /**
     * Gets the MessageDigest to digest the data imprint
     * @return the digest algorithm name
     */
    public MessageDigest getMessageDigest() throws GeneralSecurityException;
    
    /**
     * Get RFC 3161 timeStampToken.
     * Method may return null indicating that timestamp should be skipped.
     * @param imprint byte[] - data imprint to be time-stamped
     * @return byte[] - encoded, TSA signed data of the timeStampToken
     * @throws Exception - TSA request failed
     */
    public byte[] getTimeStampToken(byte[] imprint) throws Exception;
    
}