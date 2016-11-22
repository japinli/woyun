package cn.signit.merkle;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * This class constructs a Merkle tree given a list of files
 * 
 * @author Daniel Boehm, Matthew Knox, Noah Malmed (refactored)
 * @organization ProveBit
 * @version 0.1
 * 
 *          TODO: Add saving/loading trees from files TODO: Figure out fancy
 *          indexing scheme for last two levels so tree doesn't have to be
 *          allocated as if it were complete Current allocation wastes O(N) tree
 *          nodes
 */
public class Merkle implements Serializable {
	private static final long serialVersionUID = 3602863826872676332L;
	protected byte[][] tree;
	protected int height; // Root is level 0
	protected int numLeaves;
	protected int totalNodes;
	protected boolean exists;
	protected HashType hashAlgorithm;
	protected List<byte[]> recentLeaves; // -Sorted- list of leaves in most
											// recent tree construction
	private static final String ZERO_64 = "0000000000000000000000000000000000000000000000000000000000000000";

	/**
	 * Default constructor
	 */
	public Merkle(HashType type) {
		hashAlgorithm = type;
		recentLeaves = new ArrayList<byte[]>();
		tree = null;
		exists = false;
	}

	/**
	 * Make the merkle tree from a provided list of hashes. return the root hash
	 * after construction
	 * 
	 * @return byte[] root hash of constructed tree
	 */
	public byte[] makeTree(List<byte[]> hashList) {
		if (hashList.size() == 0) {
			resetTree();
			return getRootHash();
		}

		if (hashList.size() % 2 == 1) { // If odd number of hashes, duplicate
										// last
			hashList.add(hashList.get(hashList.size() - 1));
		}
		numLeaves = hashList.size();
		recentLeaves = hashList;

		allocateTree();
		makeLeaves(hashList);
		try {
			makeInternalNodes();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		exists = true;
		return getRootHash();
	}

	/**
	 * Get height of tree, root is level 0
	 * 
	 * @return height of tree
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get number of non empty leaves in tree
	 * 
	 * @return number of leaves
	 */
	public int getNumLeaves() {
		return numLeaves;
	}

	/**
	 * Get the top level hash of the merkle tree
	 * 
	 * @return byte[] of top level hash, or all 0's if emtry tree
	 */
	public byte[] getRootHash() {
		byte[] root = null;
		try {
			root = (tree != null) ? tree[0] : Hex.decodeHex(ZERO_64.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}

		return root;
	}

	/**
	 * Check to see if a given hash exists as a leaf
	 * 
	 * @param hash
	 *            - used to check all leaves
	 * @return
	 */
	public boolean existsAsLeaf(byte[] hash) {
		int index = findLeafIndex(hash); // Collections.binarySearch(recentLeaves,
											// hash, new FileHashComparator());
		return (index >= 0) ? true : false;
	}

	public List<byte[]> getLeaves() {
		return recentLeaves;
	}

	/**
	 * Get the total number of nodes in the merkle tree
	 * 
	 * @return number of nodes in the tree
	 */
	public int getTreeSize() {
		return totalNodes;
	}

	/**
	 * Get entire representation of the current tree
	 * 
	 * @return ArrayList<byte[]> representation of tree, root is ?? element
	 */
	public byte[][] getTree() {
		return tree;
	}

	/**
	 * Returns whether or not a tree is currently being stored
	 * 
	 * @return - true if a tree has been created, false otherwise
	 */
	public boolean exists() {
		return exists;
	}

	/**
	 * Computes log base 2 of the argument value
	 * 
	 * @param value
	 *            - double to compute log_2 of
	 * @return log_2(value)
	 */
	protected double logBase2(double value) {
		return (Math.log(value) / Math.log(2.0));
	}

	/**
	 * Function that returns a list of the path of hashes to the root given a
	 * leaf hash
	 * 
	 * @param startingHash
	 *            - leaf hash to start the path with
	 * @return The path in list form. The list starts with the lowest hash.
	 */
	public List<MerklePathStep> findPath(byte[] startingHash) {
		List<MerklePathStep> merklePath = new ArrayList<MerklePathStep>();
		if (existsAsLeaf(startingHash)) {
			int currentIndex = findLeafIndex(startingHash);
			while (currentIndex != 0) {
				int siblingIndex = getSibling(currentIndex);
				byte[] sibling = tree[siblingIndex];
				boolean siblingOnLeft = isLeftNode(siblingIndex);
				// not nodeOnLeft because sibling is on other side
				merklePath.add(new MerklePathStep(siblingOnLeft, sibling));
				currentIndex = getParent(currentIndex);
			}
		}

		return merklePath;
	}

	protected int getSibling(int nodeIndex) {
		boolean nodeOnLeft = isLeftNode(nodeIndex);
		int sibling;
		if (nodeOnLeft) {
			if (tree[nodeIndex + 1] == null) // odd one out
				sibling = nodeIndex;
			else // regular case
				sibling = nodeIndex + 1;
		} else {
			sibling = nodeIndex - 1;
		}
		return sibling;
	}

	/**
	 * Function that takes in a list of merkle steps and checks whether the path
	 * is valid
	 * 
	 * @param path
	 *            - list of merkle steps starting from the leafs
	 * @return Whether the given path is valid
	 */
	public boolean checkPath(List<MerklePathStep> path) {
		boolean validPath = (this.height == path.size());

		if (validPath && path.size() > 0) {
			byte[] currentHash = path.get(0).getHash();
			if (existsAsLeaf(currentHash)) {
				int treeIndex = findLeafIndex(currentHash);
				for (int listIndex = 0; listIndex < path.size() && validPath; listIndex++) {
					boolean correctSide = isLeftNode(treeIndex) == path.get(listIndex).onLeft();
					boolean correctHash = Arrays.equals(tree[treeIndex], path.get(listIndex).getHash());

					if (!correctSide || !correctHash) {
						validPath = false;
					}

					treeIndex = getSibling(getParent(treeIndex));
				}
			} else {
				validPath = false;
			}
		}
		return validPath;
	}

	/**
	 * Calculates the total number of actual nodes in the tree
	 * 
	 * @return number of nodes in the tree
	 */
	protected int getNumNodes() {
		if (numLeaves == 0) {
			return 0;
		}
		// At this time I cannot determine a closed form for this sum
		int curr = numLeaves;
		int total = numLeaves;
		while (curr != 1) {
			curr = (curr / 2);
			if (curr % 2 != 0) {
				if (curr != 1) {
					curr += 1;
				}
			}
			total += curr;
		}
		return total;
	}

	protected int getParent(int index) {
		return (index - 1) / 2;
	}

	protected int getLeftChild(int index) {
		return 2 * (index) + 1;
	}

	protected int getRightChild(int index) {
		return 2 * (index + 1);
	}

	protected boolean isLeftNode(int index) {
		return (index % 2) != 0;
	}

	protected void resetTree() {
		numLeaves = 0;
		height = 0;
		recentLeaves.clear();
		tree = null;
		exists = false;
	}

	/**
	 * Wrapper that builds each level of the tree (except last)
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	protected void makeInternalNodes() throws NoSuchAlgorithmException {
		int level = height - 1; // Leaf level built already
		for (; level >= 0; level--) {
			buildLevel(level);
		}
	}

	/**
	 * Helper function to build internal nodes of tree ensuring there are an
	 * even number of nodes on each level
	 * 
	 * @param level
	 *            - Level to build nodes of
	 * @throws NoSuchAlgorithmException
	 */
	protected void buildLevel(int level) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		int nodeIndex = (int) Math.pow(2, level) - 1; // Leftmost node at level
														// 'level'
		int lastNodeIndex = (int) Math.pow(2, level + 1) - 2; // Last
																// (rightmost)
																// node at level
																// 'level'
		int nodesBuilt = 0;
		for (; nodeIndex <= lastNodeIndex; nodeIndex++, nodesBuilt++) {
			md.reset();
			byte[] leftChildHash = tree[getLeftChild(nodeIndex)];
			byte[] rightChildHash = tree[getRightChild(nodeIndex)];
			if (leftChildHash == null) { // Implies right child is also null by
											// merkle construction
				if (nodesBuilt % 2 != 0) { // Odd number of nodes on this level,
											// copy left sibling
					tree[nodeIndex] = tree[nodeIndex - 1];
					nodesBuilt++;
				}
				return; // Reached last non empty node at this level, we are
						// done
			}
			byte[] newHash = MerkleUtils.concatHashes(leftChildHash, rightChildHash);
			switch (hashAlgorithm) {
			case SHA256:
				md.update(newHash);
				break;
			case DOUBLE_SHA256:
				md.update(newHash);
				byte[] intermediate = md.digest();
				md.reset();
				md.update(intermediate);
				break;

			}
			tree[nodeIndex] = md.digest();
		}
	}

	/**
	 * Helper function that makes the leaf nodes of the tree
	 * 
	 * @param hashList
	 *            - Hashes of files that make up the leaf nodes
	 */
	protected void makeLeaves(List<byte[]> hashList) {
		int i = (int) Math.pow(2, height) - 1; // Leftmost leaf node
		for (byte[] hash : hashList) {
			tree[i] = hash;
			i++;
		}
	}

	/**
	 * Helper function that finds the array index of a given hash
	 * 
	 * @param leafHash
	 *            - Hash to be looked for in the leaves
	 * @return - Array index of the hash. -1 returned if not found
	 */
	protected int findLeafIndex(byte[] leafHash) {
		// Start at the Leftmost leaf node
		for (int i = (int) Math.pow(2, height) - 1; i < tree.length; i++) {
			if (Arrays.equals(leafHash, tree[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper function that allocates space for tree and initializes all
	 * elements to null
	 */
	protected void allocateTree() {
		height = (int) Math.ceil(logBase2(numLeaves));
		totalNodes = getNumNodes();
		tree = new byte[(int) (Math.pow(2, height + 1) - 1)][]; // Allocate
																// space as if
																// tree is
																// complete
																// (easier
																// representation)
		for (int i = 0; i < tree.length; i++) {
			tree[i] = null;
		}
	}

	public HashType getHashAlgorithm() {
		return hashAlgorithm;
	}

	/**
	 * Function for flipping the Endian-ness of byte[]
	 * 
	 * @param input
	 *            - byte[] to have endian-ness swapped
	 * @return new byte[] of input with bytes swapped
	 */
	public byte[] flipByteEndianness(byte[] input) {
		byte[] output = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			int j = input.length - (i + 1);
			output[i] = input[j];
		}
		return output;

	}
}
