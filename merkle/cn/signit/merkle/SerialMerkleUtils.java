package cn.signit.merkle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * (这里用一句话描述这个类的作用)
 * 
 * @ClassName SerialMerkleUtils
 * @author Liwen
 * @date 2016年10月27日-上午10:10:03
 * @version (版本号)
 * @see (参阅)
 */
public class SerialMerkleUtils {
	/**
	 * Writes a Merkle object to a file
	 * 
	 * 
	 * 
	 * @param merkle
	 *            - Merkle to write to file
	 * @param file
	 *            - file to write merkle to
	 */
	public static void writeToFile(Merkle merkle, File file) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(merkle);
			oos.close();
		} catch (IOException e) {
			System.err.println("Merkle save failed, stack trace follows");
			e.printStackTrace();
		}
	}

	/**
	 * Recovers Merkle object from file
	 * 
	 * @param file
	 *            - File to read Merkle from
	 * @return Merkle object recovered from file or null if recovery failed
	 */
	public static Merkle readMerkleFromFile(File file) {
		Merkle merkle = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			merkle = (Merkle) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Merkle recovery failed, stack trace follows");
			e.printStackTrace();
		}
		return merkle;
	}

	/**
	 * Recovers FileMerkle object from file
	 * 
	 * @param file
	 *            - File to read FileMerkle from
	 * @return FileMerkle object recovered from file or null if recovery failed
	 */
	public static FileMerkle readFileMerkleFromFile(File file) {
		FileMerkle merkle = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			merkle = (FileMerkle) ois.readObject();
			ois.close();

			for (File oldFile : merkle.getTrackedFiles()) {
				if (!oldFile.exists()) {
					System.err.println("Recovered tree contained file: " + oldFile.getAbsolutePath()
							+ " which no longer exists, removing...");
					merkle.removeTracking(oldFile);
				}
			}

			for (File oldDir : merkle.getTrackedDirs()) {
				if (!oldDir.exists() || !oldDir.isDirectory()) {
					System.err.println("Recovered tree contained directory: " + oldDir.getAbsolutePath()
							+ " which is invalid, removing");
					merkle.removeTracking(oldDir);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Merkle recovery failed, stack trace follows");
			e.printStackTrace();
		}
		return merkle;
	}
}
