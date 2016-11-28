package cn.signit.jgit.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.FS;

import cn.signit.conf.ConfigProps;

public class JgitTest {
	private final static String HDFS_URL = ConfigProps.get("hadoop.url");
	private final static Configuration HDFS_CONF = new Configuration();
	
	public static void main(String[] args) {
		
		try {
			JgitTest.createNewRepository();
			System.out.println("Hello world");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public static Repository openRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.readEnvironment().findGitDir().build();
		return repository;
	}
	
	public static Repository createNewRepository() throws IOException {
		Repository repository = FileRepositoryBuilder.create(new File("/home/japin/testgit/.git"));
		repository.create();
		StoredConfig storedConfig = repository.getConfig();
		String branch = repository.getBranch();
		ObjectDatabase objectDatabase = repository.getObjectDatabase();
		
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		Set<String> names = repository.getRemoteNames();
		for (String name : names) {
			System.out.println(name);
		}
		
		return repository;
	}
	public void createRepository(String path) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(path)).readEnvironment().findGitDir().build();
	}
}
