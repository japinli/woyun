package cn.signit.jgit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectDatabase;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
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

import com.sun.source.tree.Tree;

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
			// JgitTest.parseRepository(repository);
			// JgitTest.treeIterator(repository);
			/*
			final ObjectId head = repository.resolve("HEAD");
			List<String> items = readElementsAt(repository, head.getName(), "src");
			showItems(items);
			
			System.out.println("---------------------------------");
			items = readDirectory(repository.getWorkTree().getPath() + "/src");
			showItems(items);
			*/
			getCommitMessage(repository);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public static void getCommitMessage(Repository repository) throws IOException {
		Collection<Ref> allRefs = repository.getAllRefs().values();
		
		try (RevWalk walk = new RevWalk(repository)) {

			for (Ref ref : allRefs) {
				walk.markStart(walk.parseCommit(ref.getObjectId()));
			}
			
			for (RevCommit commit : walk) {
				
				System.out.println("\ncommit " + commit.name());
				System.out.println("Author: " + commit.getCommitterIdent().getName());
				System.out.println("Message: " + commit.getFullMessage());
			}
		}
	}
	
	public static void showItems(List<String> items) {
		for (String item : items) {
			System.out.println(item);
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
	
	public static void parseRepositoryPath(Repository repository, String path) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException {
		ObjectId lastCommit = repository.resolve("HEAD");
		
		try (RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(lastCommit);
			RevTree revTree = commit.getTree();
			
			TreeWalk treeWalk = TreeWalk.forPath(repository, path, revTree);
			treeWalk.addTree(revTree);
			treeWalk.setRecursive(false);
			String workingPath = repository.getWorkTree().getPath();
			while (treeWalk.next()) {
				String fullPath = treeWalk.getPathString();
				String name = treeWalk.getNameString();
				File file = new File(workingPath, fullPath);
				long mtime = FS.DETECTED.lastModified(file);
				boolean isDir = FS.DETECTED.isDirectory(file);
				long size = FS.DETECTED.length(file);
				System.out.println("path: " + fullPath + ", name: " + name + ", type: " + (isDir ? "dir" : "file") + 
						", size: " + size + ", mtime: " + new Date(mtime));
			}
			
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
	
	public static RevCommit getRevCommit(Repository repository, String commit) throws IOException {
		try (RevWalk revWalk = new RevWalk(repository)) {
			return revWalk.parseCommit(ObjectId.fromString(commit));
		}
	}
	
	public static ObjectId getRepositoryHead(Repository repository) throws IOException {
		final ObjectId head = repository.resolve("HEAD");
		return head;
	}
	
	public static TreeWalk getTreeWalk(Repository repository, RevTree tree, final String path) throws IOException {
		TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);
		if (treeWalk == null) {
			throw new FileNotFoundException("未找到指定的文件( " + path + ")");
		}
		return treeWalk;
	}
	
	public static List<String> readDirectory(String directory) {
		List<String> items = new ArrayList<String>();
		
		File root = new File(directory);
		File[] files = root.listFiles();
		
		for (File file : files) {
			System.out.println(file.getPath());
			items.add(file.getName());
		}
		
		return items;
	}
	
	public static List<String> readElementsAt(Repository repository, String commit, String path) throws IOException {
		RevCommit revCommit = getRevCommit(repository, commit);
		RevTree tree = revCommit.getTree();
		List<String> items = new ArrayList<String>();
		
		if (path.isEmpty()) { // 遍历根目录
			try (TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(false);
				System.out.println(path + "包含" + treeWalk.getTreeCount() + "个目录");
				while (treeWalk.next()) {
					items.add(treeWalk.getPathString());
				}
			}
		} else { // 遍历指定目录
			try (TreeWalk treeWalk = getTreeWalk(repository, tree, path)) {
				
				if ((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
					throw new IllegalStateException("不是目录项");
				}
				
				try (TreeWalk dirWalk = new TreeWalk(repository)) {
					dirWalk.addTree(treeWalk.getObjectId(0));
					dirWalk.setRecursive(false);
					while (dirWalk.next()) {
						items.add(dirWalk.getPathString());
					}
				}
			}
		}
		return items;
	}
}
