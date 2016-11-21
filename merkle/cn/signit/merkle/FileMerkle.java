package cn.signit.merkle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class constructs a Merkle tree from files within a given directory
 * 
 * @author Daniel Boehm, Matthew Knox, Noah Malmed (Refactored)
 * @organization ProveBit
 * @version 0.1
 * 
 * @TODO: Add method to flip the endian-ness of the hashes (to conform to
 *        Bitcoin)
 * @TODO: Add saving/loading trees from files
 * @TODO: Figure out fancy indexing scheme for last two levels so tree doesn't
 *        have to be allocated as if it were complete Current allocation wastes
 *        O(N) tree nodes
 */
public class FileMerkle extends Merkle {
	private static final long serialVersionUID = 4565319839143960668L;
	private List<File> trackedFiles; // List of files being tracked
	private Map<File, Boolean> trackedDirectories; // List of directories being
													// tracked and whether
	// or not they are recursively tracked

	/**
	 * Default constructor
	 */
	public FileMerkle(HashType type) {
		super(type);
		trackedFiles = new ArrayList<File>();
		trackedDirectories = new HashMap<File, Boolean>();
	}

	/**
	 * Add a file or directory to the list of tracked files in this tree if the
	 * file is not already tracked
	 * 
	 * @param file
	 *            - File/Directory to track
	 * @param recursive
	 *            - If file is a directory, specify whether or not the directory
	 *            should be recursively searched
	 */
	public void addTracking(File file, boolean recursive) {
		if (!file.isDirectory() && !isTracking(file)) {
			trackedFiles.add(file);
		} else if ((file.isDirectory() && !isTracking(file)) || trackedDirectories.containsKey(file)) {
			trackedDirectories.put(file, recursive);
			// If we add a directory, we want to make sure that any files that
			// were in that directory but are also
			// in the trackedFiles list are removed from the trackedFiles list
			// since they are duplicated by the coverage
			// provided by the trackedDirectories list
			List<File> duplicates = new ArrayList<File>();
			for (File existingFile : trackedFiles) {
				if (MerkleUtils.isTrackedRecursively(existingFile, trackedDirectories)) {
					duplicates.add(existingFile);
				}
			}
			trackedFiles.removeAll(duplicates);
		}
	}

	/**
	 * Wrapper that accepts a <File, Boolean> map and adds all elements to
	 * tracking
	 * 
	 * @param trackingMap
	 */
	public void addAllTracking(Map<File, Boolean> trackingMap) {
		for (File file : trackingMap.keySet()) {
			addTracking(file, trackingMap.get(file));
		}
	}

	/**
	 * Remove file/directory from tracking
	 * 
	 * @param file
	 *            - file/directory to remove from tracking
	 */
	public void removeTracking(File file) {
		if (trackedDirectories.containsKey(file)) {
			trackedDirectories.remove(file);
		} else if (trackedFiles.contains(file)) {
			trackedFiles.remove(file);
		}
	}

	/**
	 * Wrapper that accepts a list of files and removes all from tracking
	 * 
	 * @param fileList
	 *            - List of files to remove from tracking
	 */
	public void removeAllTracking(List<File> fileList) {
		for (File file : fileList) {
			removeTracking(file);
		}
	}

	/**
	 * Make the merkle tree from the files in the specified directory and return
	 * the root hash after construction
	 * 
	 * @return byte[] root hash of constructed tree
	 */
	public byte[] makeTree() {
		List<File> fileList = MerkleUtils.getFiles(trackedFiles, trackedDirectories);
		if (fileList.size() == 0) {
			resetTree();
			return getRootHash();
		}

		List<byte[]> hashList = MerkleUtils.getFilesHashes(fileList);

		return super.makeTree(hashList);
	}

	/**
	 * Get total number of tracked discrete files and directories
	 * 
	 * @return total discrete files + total directories
	 */
	public int getNumTracked() {
		return trackedFiles.size() + trackedDirectories.keySet().size();
	}

	/**
	 * Get the list of directories being tracked
	 * 
	 * @return List<File> of directories being tracked, may be empty
	 */
	public List<File> getTrackedDirs() {
		List<File> directoryList = new ArrayList<File>();
		directoryList.addAll(trackedDirectories.keySet());
		return directoryList;
	}

	/**
	 * Get the list of files being tracked
	 * 
	 * @return List<File> of specific files being tracked, may be empty
	 */
	public List<File> getTrackedFiles() {
		return trackedFiles;
	}

	/**
	 * Check to see whether the specific file is being tracked by the tree
	 * 
	 * @param file
	 *            - File to check for tracking
	 * @return true if file is being tracked, false o/w
	 */
	public Boolean isTracking(File file) {
		if (trackedFiles.contains(file) || trackedDirectories.containsKey(file)
				|| MerkleUtils.isTrackedRecursively(file, trackedDirectories)) {
			return true;
		}
		return false;
	}

	/**
	 * Check to see whether directory is being recursively tracked
	 * 
	 * @param dir
	 *            -Tracked directory
	 * @return true if directory is recursively tracked, false if not a
	 *         directory or not recursively tracked
	 */
	public Boolean isDirRecursive(File dir) {
		return (dir.isDirectory() && trackedDirectories.get(dir));
	}
}
