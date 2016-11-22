package cn.signit.utils.timestamp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenInfo;
import org.bouncycastle.util.encoders.Base64;

import cn.signit.pkcs.digests.BouncyCastleDigest;
import cn.signit.pkcs.digests.DigestAlgorithms;
import cn.signit.utils.exception.MessageInit;

/**
*基于本地时间戳服务器实现的时间戳客户端
* @ClassName SignitTSAFactory
* @author Liwen
* @date 2016年8月17日-下午2:18:05
* @version (版本号)
* @see (参阅)
*/
public class SignitTSAClient implements TSAClient{
	
	protected String tsaURL;
    protected String tsaUsername;
    protected String tsaPassword;
	 /**hash算法默认大小*/
    public static final int DEFAULTTOKENSIZE = 4096;
    /**默认散列算法*/
    public static final String DEFAULTHASHALGORITHM = "SHA-256";
    public final static int DONT_BREAK_LINES = 8;
    protected int tokenSizeEstimate;
    
    protected String digestAlgorithm;
	
	public  SignitTSAClient(String url){
		this(url, null, null);
	}
	public  SignitTSAClient(String url,String username,String password){
		this(url,username,password,DEFAULTHASHALGORITHM);
	}
	public  SignitTSAClient(String url,String username,String password,String digestAlgorithm){
		this.tsaURL=url;
		this.tsaUsername=username;
		this.tsaPassword=password;
		this.digestAlgorithm=digestAlgorithm;
		this.tokenSizeEstimate=DEFAULTTOKENSIZE;
	}
	
	/**
	*@param digested
	*@return
	*@throws Exception
	*@see (参阅)
	*/
	@Override
	public byte[] getTimeStampToken(byte[] digested) throws Exception {
		byte[] respBytes = null;
        // Setup the time stamp request
        TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);
        // tsqGenerator.setReqPolicy("1.3.6.1.4.1.601.10.3.1");
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        TimeStampRequest request = tsqGenerator.generate(new ASN1ObjectIdentifier(DigestAlgorithms.getAllowedDigests(digestAlgorithm)), digested, nonce);
        byte[] requestBytes = request.getEncoded();
        respBytes = getTSAResponse(requestBytes);
        
        TimeStampResponse response = new TimeStampResponse(respBytes);
        response.validate(request);
        PKIFailureInfo failure = response.getFailInfo();
        int value = (failure == null) ? 0 : failure.intValue();
        if (value != 0) {
            throw new IOException(MessageInit.getComposedMessage("invalid.tsa.1.response.code.2", tsaURL, String.valueOf(value)));
        }
        TimeStampToken  tsToken = response.getTimeStampToken();
        if (tsToken == null) {
            throw new IOException(MessageInit.getComposedMessage("tsa.1.failed.to.return.time.stamp.token.2", tsaURL, response.getStatusString()));
        }
        TimeStampTokenInfo tsTokenInfo = tsToken.getTimeStampInfo(); // to view details
        System.out.println("Timestamp generated: " + tsTokenInfo.getGenTime());
        byte[] encoded = tsToken.getEncoded();
        this.tokenSizeEstimate = encoded.length + 32;
		return encoded;
	}

	/**
	*@return
	*@throws GeneralSecurityException
	*@see (参阅)
	*/
	@Override
	public MessageDigest getMessageDigest() throws GeneralSecurityException {
		return new BouncyCastleDigest().getMessageDigest(digestAlgorithm);
	}

	 protected byte[] getTSAResponse(byte[] requestBytes) throws IOException {
	        URL url = new URL(tsaURL);
	        URLConnection tsaConnection;
	        try {
	        	tsaConnection = (URLConnection) url.openConnection();
	        }
	        catch (IOException ioe) {
	            throw new IOException(MessageInit.getComposedMessage("failed.to.get.tsa.response.from.1", tsaURL));
	        }
	        tsaConnection.setDoInput(true);
	        tsaConnection.setDoOutput(true);
	        tsaConnection.setUseCaches(false);
	        tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
	        //tsaConnection.setRequestProperty("Content-Transfer-Encoding", "base64");
	        tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
	        
	        if ((tsaUsername != null) && !tsaUsername.equals("") ) {
	            String userPassword = tsaUsername + ":" + tsaPassword;
	            tsaConnection.setRequestProperty("Authorization", "Basic " +
	                Base64.encode(userPassword.getBytes()));
	        }
	        OutputStream out = tsaConnection.getOutputStream();
	        out.write(requestBytes);
	        out.close();
	        
	        // Get TSA response as a byte array
	        InputStream inp = tsaConnection.getInputStream();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
	            baos.write(buffer, 0, bytesRead);
	        }
	        byte[] respBytes = baos.toByteArray();
	        
	        String encoding = tsaConnection.getContentEncoding();
	        if (encoding != null && encoding.equalsIgnoreCase("base64")) {
	            respBytes = Base64.decode(new String(respBytes));
	        }
	       
	        return respBytes;
	    }
	/**
	*@return
	*@see (参阅)
	*/
	@Override
	public int getTokenSizeEstimate() {
		return DEFAULTTOKENSIZE;
	}    
}
