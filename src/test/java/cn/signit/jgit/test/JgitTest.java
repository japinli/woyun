package cn.signit.jgit.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.attributes.Attribute;
import org.eclipse.jgit.attributes.Attributes;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectDatabase;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeIterator;
import org.eclipse.jgit.util.FS;

import cn.signit.untils.RepoPath;



public class JgitTest {
	public final static String git_dir = "/home/japin/woyun-repo/testgit/.git";

	public static void main(String[] args) {
		
		
		try {
			// JgitTest.createNewRepository();
			
			Repository repository = JgitTest.openRepository(git_dir);
			JgitTest.parseRepositoryConfig(repository);
			// JgitTest.setRepositoryConfig(repository, "lijianping", "lijianping@qq.com");
			// JgitTest.parseRepositoryConfig(repository);
			// JgitTest.addFile(repository, "README.md");
			// JgitTest.addFile(repository, "dir/README.md");
			JgitTest.parseRepository(repository);
			JgitTest.treeIterator(repository);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public static Repository openRepository(String dir) throws IOException {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.setGitDir(new File(dir)).readEnvironment().build();
	}
	
	public static Repository createNewRepository() throws IOException {
		Repository repository = FileRepositoryBuilder.create(new File("/home/japin/woyun-repo/testgit/.git"));
		repository.create();
		
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		Set<String> names = repository.getRemoteNames();
		for (String name : names) {
			System.out.println(name);
		}
		
		return repository;
	}
	
	public static void parseRepositoryConfig(Repository repository) {
		StoredConfig config = repository.getConfig();
		String name = config.getString("user", null, "name");
		String email = config.getString("user", null, "email");
		System.out.println(name + ": " + email);
	}
	
	public static void setRepositoryConfig(Repository repository, String name, String email) {
		StoredConfig config = repository.getConfig();
		config.setString("user", null, "name", name);
		config.setString("user", null, "email", email);
		try {
			config.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addFile(Repository repository, String filename) throws IOException {
		try (Git git = new Git(repository)) {
			File tmp = new File(repository.getWorkTree(), filename);
			if (!tmp.createNewFile()) {
				System.out.println("create (" + filename + ") failed"); 
			}  else {
				git.add().addFilepattern(".").call();
				git.commit().setMessage("新建 \"" + filename + "\" 文件").call();
			}
		} catch (NoFilepatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseRepository(Repository repository) {
		try {
			ObjectId lastCommit = repository.resolve("HEAD");
			
			try (RevWalk revWalk = new RevWalk(repository)) {
				RevCommit commit = revWalk.parseCommit(lastCommit);
				RevTree revTree = commit.getTree();
				System.out.println(revTree);
				System.out.println(new Date(commit.getCommitTime()));
				System.out.println(commit.getShortMessage());
				System.out.println(repository.getWorkTree().getPath());
				
				try (TreeWalk treeWalk = new TreeWalk(repository)) {
					treeWalk.addTree(revTree);
					treeWalk.setRecursive(false);
					String workingPath = repository.getWorkTree().getPath();
					while (treeWalk.next()) {
						String path = treeWalk.getPathString();
						String name = treeWalk.getNameString();
						File file = new File(workingPath, path);
						long mtime = FS.DETECTED.lastModified(file);
						boolean isDir = FS.DETECTED.isDirectory(file);
						long size = FS.DETECTED.length(file);
						System.out.println("path: " + path + ", name: " + name + ", type: " + (isDir ? "dir" : "file") + 
								", size: " + size + ", mtime: " + new Date(mtime));
					}
				}
			}
		} catch (RevisionSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmbiguousObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void treeIterator(Repository repository) throws IOException {
		
		
		
		/*try (Git git = new Git(repository)) {
			DirCache dirCache = repository.readDirCache();
			for (int i = 0; i < dirCache.getEntryCount(); ++i) {
				DirCacheEntry entry = dirCache.getEntry(i);
				String path = entry.getPathString();
				long mtime = entry.getLastModified();
				
				System.out.println("path: " + path + ", mtime: " + new Date(mtime));
			}
		}*/
	}
}
