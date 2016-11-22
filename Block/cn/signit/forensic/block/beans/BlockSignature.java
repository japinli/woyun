package cn.signit.forensic.block.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.operator.ContentSigner;

import cn.signit.pkcs.digests.DigestAlgorithms;
import cn.signit.pkcs.x509.tools.SignVerify;

/**
*区块签名基本对象及其初始化方法
* @ClassName BlockSignature
* @author Liwen
* @date 2016年11月14日-下午3:09:18
* @version (版本号)
* @see (参阅)
*/
public class BlockSignature extends ASN1Object{
	private DERBitString merkleroot;//待签名的默克尔根
	private AlgorithmIdentifier   signAlgorithm;//签名算法
	private Certificate singer;//签名者证书
	private DERBitString signature;//签名值

	private BlockSignature(){
		
	}
	
	/**
	 * 签名校验
	 */
	public boolean verify(){
		try{
			Signature sig=Signature.getInstance(DigestAlgorithms.getDigest(signAlgorithm.toString()));
			X509Certificate certificate=(X509Certificate) CertificateFactory.getInstance("X509")
					.generateCertificate(new ByteArrayInputStream(singer.getEncoded()));
			sig.initVerify(certificate.getPublicKey());
			sig.update(signature.getBytes());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据指定信息生成新的区块签名
	 * @param merkleroot 待签名的默克尔根
	 * @param signer 签名者
	 * @param cert 签名者对应的公钥证书,本应用中统一采用X509标准的数字证书
	 * @return 区块签名对象
	 */
	public static BlockSignature sign(byte[] merkleroot,ContentSigner signer,X509Certificate cert) throws IOException, CertificateEncodingException{
		BlockSignature b=new BlockSignature();
		b.setSignAlgorithm(signer.getAlgorithmIdentifier());
		b.setMerkleroot(new DERBitString(merkleroot));
		b.setSignature(new DERBitString(SignVerify.sign(signer,merkleroot)));
		b.setSinger(Certificate.getInstance(cert.getEncoded()));
		return b;
	}
	
	/**
	 * 根据对象序列化区块签名
	 * @param 区块签名对象
	 * @return 序列化完成的区块签名对象
	 */
	public static BlockSignature getInstance(Object obj){
		if(obj instanceof BlockSignature){
			return (BlockSignature)obj;
		}
		if(obj!=null){
			return new BlockSignature(ASN1Sequence.getInstance(obj));
		}
		throw new IllegalArgumentException("unknown object in getInstance");
	}

	private BlockSignature(ASN1Sequence seq){
		if(seq.size()==4){
			merkleroot=DERBitString.getInstance(seq.getObjectAt(0));
			signAlgorithm=AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
			singer=Certificate.getInstance(seq.getObjectAt(2));
			signature=DERBitString.getInstance(seq.getObjectAt(3));
		}else
			throw new IllegalArgumentException("Bad sequence size: " + seq.size());
	}
	
	public AlgorithmIdentifier getSignAlgorithm() {
		return signAlgorithm;
	}

	public void setSignAlgorithm(AlgorithmIdentifier signAlgorithm) {
		this.signAlgorithm = signAlgorithm;
	}

	public Certificate getSinger() {
		return singer;
	}

	public void setSinger(Certificate singer) {
		this.singer = singer;
	}

	public DERBitString getSignature() {
		return signature;
	}

	public void setSignature(DERBitString signature) {
		this.signature = signature;
	}

	public DERBitString getMerkleroot() {
		return merkleroot;
	}

	public void setMerkleroot(DERBitString merkleroot) {
		this.merkleroot = merkleroot;
	}

	/**
	*
	*@return
	*@see (参阅)
	*/
	@Override
	public ASN1Primitive toASN1Primitive() {
		ASN1EncodableVector seq = new ASN1EncodableVector();
		seq.add(signAlgorithm);//签名算法
		seq.add(singer);//签名者
		seq.add(signature);//签名值
		return new DERSequence(seq);
	}
	
	/**
	 * 获取字节流形式的区块签名
	 */
	public byte[] getBytes() throws IOException{
		 ByteArrayOutputStream bOut = new ByteArrayOutputStream();
	     ASN1OutputStream      aOut = new ASN1OutputStream(bOut);
	     aOut.writeObject(this);
	     return bOut.toByteArray();
	}
	
}
