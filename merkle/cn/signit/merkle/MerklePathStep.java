package cn.signit.merkle;

/**
 * This class is just a data class used to describe a step of a tree traversal
 * 
 * @ClassName MerklePathStep
 * @author Liwen
 * @date 2016年10月27日-上午10:09:32
 * @version (版本号)
 * @see (参阅)
 */
public class MerklePathStep {
	private boolean onLeft;
	private byte[] hash;

	/**
	 * Parameterized constructor, sets paramter values to class values
	 * 
	 * @param onLeft
	 * @param fullHash
	 */
	public MerklePathStep(boolean onLeft, byte[] fullHash) {
		this.onLeft = onLeft;
		this.hash = fullHash;
	}

	public boolean onLeft() {
		return onLeft;
	}

	public void setOnLeft(boolean onLeft) {
		this.onLeft = onLeft;
	}

	public byte[] getHash() {
		return hash;
	}

	public void setFullHash(byte[] fullHash) {
		this.hash = fullHash;
	}
}
