package cn.signit.merkle;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

public class MerkleUtils {
	/**
	 * Wrapper that gets all files being tracked in all tracked directories
	 * 
	 * @param trackedFiles
	 *            - List of discrete files being tracked
	 * @param trackedDirectories
	 *            - Map of tracked directories and whether or not they are
	 *            recursively tracked
	 * @return List<File> of all files in tracking
	 */
	public static List<File> getFiles(List<File> trackedFiles, Map<File, Boolean> trackedDirectories) {
		List<File> fileList = new ArrayList<File>();
		fileList.addAll(trackedFiles);
		for (File directory : trackedDirectories.keySet()) {
			fileList.addAll(getFiles(directory, trackedDirectories.get(directory)));
		}
		return fileList;
	}

	/**
	 * Get list of all files in directory, recursively if need be
	 * 
	 * @param directory
	 *            - Directory to check for files
	 * @param recursive
	 *            - true if sub-directories should be searched, false o/w
	 * @return List<File> of all files in directory
	 */
	private static List<File> getFiles(File directory, boolean recursive) {
		List<File> fileList = new ArrayList<File>();
		for (File file : directory.listFiles()) {
			if (!file.isDirectory()) {
				fileList.add(file);
			} else if (recursive) {
				fileList.addAll(getFiles(file, recursive));
			}

		}
		return fileList;
	}

	/**
	 * Determines whether or not a file is being recursively tracked
	 * 
	 * @param file
	 *            - File to check for tracking
	 * @param trackedDirectories
	 *            - Map of tracked directories and their associated recursive
	 *            status
	 * @return true if file is tracked, false o/w
	 */
	public static Boolean isTrackedRecursively(File file, Map<File, Boolean> trackedDirectories) {
		for (File directory : trackedDirectories.keySet()) {
			try {
				if ((!file.isDirectory() && file.getParentFile().equals(directory))
						|| (trackedDirectories.get(directory) && FileUtils.directoryContains(directory, file))) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		return false;
	}

	/**
	 * Get SHA-256 hash of each file in argument list
	 * 
	 * @param fileList
	 *            - ArrayList<File> of files to get hashes of
	 * @return Lexicographically sorted List<byte[]> of corresponding SHA-256
	 *         file hashes or null if error occurred
	 */
	public static List<byte[]> getFilesHashes(List<File> fileList) {
		List<byte[]> hashList = new ArrayList<byte[]>();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			for (File file : fileList) {
				md.reset();
				byte[] fileBytes = FileUtils.readFileToByteArray(file);
				md.update(fileBytes);
				hashList.add(md.digest());
			}
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			return null;
		}

		Collections.sort(hashList, new FileHashComparator());
		return hashList;
	}

	/**
	 * Minimal custom comparator subclass for sorting hashes
	 */
	public static class FileHashComparator implements Comparator<byte[]> {
		public int compare(byte[] hash1, byte[] hash2) {
			String hash1Hex = Hex.encodeHexString(hash1);
			String hash2Hex = Hex.encodeHexString(hash2);
			return hash1Hex.compareTo(hash2Hex);
		}
	}

	/**
	 * Concatenates argument hashes L + R and returns result
	 * 
	 * @param leftHash
	 * @param rightHash
	 * @return leftHash + rightHash
	 */
	public static byte[] concatHashes(byte[] leftHash, byte[] rightHash) {
		byte[] newHash = new byte[leftHash.length + rightHash.length];
		System.arraycopy(leftHash, 0, newHash, 0, leftHash.length);
		System.arraycopy(rightHash, 0, newHash, leftHash.length, rightHash.length);
		return newHash;
	}
}