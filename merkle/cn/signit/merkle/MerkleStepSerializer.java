package cn.signit.merkle;

import java.util.List;

/**
 * This class is used to convert a list MerklePathSteps and serializes them into a byte array
 * 
 * @ClassName MerkleStepSerializer
 * @author Liwen
 * @date 2016年10月27日-上午10:08:59
 * @version (版本号)
 * @see (参阅)
 */
public class MerkleStepSerializer {
	/**
	 * Static function that serializes a list of MerklePathSteps
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] serialize(List<MerklePathStep> path) {
		int list_size = path.size() * 33;
		byte[] output = new byte[list_size];

		for (int i = 0; i < path.size(); i++) {
			output[(33 * i)] = (byte) (path.get(i).onLeft() ? 1 : 0);
			for (int j = 1; j < 33; j++) {
				output[(33 * i) + j] = path.get(i).getHash()[j - 1];
			}
		}
		return output;
	}
}
