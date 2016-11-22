package cn.signit.merkle.test;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertFalse; 
import static org.junit.Assert.assertNotEquals; 
import static org.junit.Assert.assertNull; 
import static org.junit.Assert.assertTrue; 
 
import java.io.File; 
import java.io.IOException; 
import java.util.List; 
 
import org.apache.commons.codec.DecoderException; 
import org.apache.commons.codec.binary.Hex; 
import org.apache.commons.io.FileUtils; 
import org.junit.BeforeClass; 
import org.junit.ClassRule; 
import org.junit.Test; 
import org.junit.rules.TemporaryFolder;

import cn.signit.merkle.FileMerkle;
import cn.signit.merkle.HashType;
import cn.signit.merkle.MerklePathStep;
import cn.signit.merkle.MerkleStepSerializer;
import cn.signit.merkle.SerialMerkleUtils; 
 
public class FileMerkleTest { 
    final public static String COMPLETEDIR = "testCompleteDir"; 
    final public static String INCOMPLETEDIR = "testIncompleteDir"; 
    final public static String RECURSIVEDIR = "testRecursiveDir"; 
    final public static String RECURSIVEDIR2 = "testRecursiveDir2"; 
     
    @ClassRule 
    public static TemporaryFolder emptyFolder = new TemporaryFolder(); 
     
    public static File completeDirPath; 
    public static File incompleteDirPath; 
    public static File recursiveDirPath; 
    public static File recursiveDir2Path; 
    public static File emptyDirPath; 
 
    @BeforeClass 
    public static void setUpBeforeClass() throws Exception { 
        completeDirPath = new File(FileMerkleTest.class.getResource(COMPLETEDIR).getPath()); 
        incompleteDirPath = new File(FileMerkleTest.class.getResource(INCOMPLETEDIR).getPath()); 
        recursiveDirPath = new File(FileMerkleTest.class.getResource(RECURSIVEDIR).getPath()); 
        recursiveDir2Path = new File(FileMerkleTest.class.getResource(RECURSIVEDIR2).getPath()); 
        emptyDirPath = emptyFolder.getRoot(); 
    } 
 
    @Test 
    public void testTreeHeightEvenLeaves() { 
        FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(completeDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getHeight() == 3); 
    } 
 
    @Test 
    public void testTreeHeightOddLeaves() { 
        FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(incompleteDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getHeight() == 4); 
    } 
 
    @Test 
    public void testNumLeavesEven() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(completeDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getNumLeaves() == 8); 
    } 
 
    @Test 
    public void testNumLeavesOdd() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(incompleteDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getNumLeaves() == 14); 
    } 
 
    @Test 
    public void testTreeSizeEven() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(completeDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getTreeSize() == 15); 
    } 
 
    @Test 
    public void testTreeSizeOdd() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(incompleteDirPath, false); 
        mTree.makeTree(); 
        assertTrue(mTree.getTreeSize() == 29); 
    } 
     
    @Test 
    public void testNoTree() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(incompleteDirPath, false); 
     assertNull(mTree.getTree()); 
    } 
     
    @Test 
    public void testLeafExists() { 
     FileMerkle m = new FileMerkle(HashType.SHA256); 
     m.addTracking(completeDirPath, false); 
     m.makeTree(); 
     assertEquals(true, m.existsAsLeaf(m.getLeaves().get(0))); 
    } 
     
    @Test public void testLeafNoExist() throws DecoderException { 
     FileMerkle m = new FileMerkle(HashType.SHA256); 
     m.addTracking(completeDirPath, false); 
     m.makeTree(); 
     assertEquals(false, m.existsAsLeaf(Hex.decodeHex("0000000000000000000000000000000000000000000000000000000000000000".toCharArray()))); 
    } 
 
    @Test 
    public void testLeafPositions() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(completeDirPath, false); 
        mTree.makeTree(); 
        byte[][] tree = mTree.getTree(); 
        int i = (int) Math.pow(2, mTree.getHeight()) - 1; 
        for (; i < Math.pow(2, mTree.getHeight()+1) - 1; i++) { 
            assertTrue(Hex.encodeHexString(tree[i]).length() == 64); 
        } 
    } 
 
    @Test 
    public void testSortedLeaves() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(completeDirPath, false); 
        mTree.makeTree(); 
        byte[][] tree = mTree.getTree(); 
        int i = (int) Math.pow(2, mTree.getHeight()) - 1; 
        String last = Hex.encodeHexString(tree[i]); 
        for (; i < Math.pow(2, mTree.getHeight()) - 1 + mTree.getNumLeaves(); i++) { 
            String curr = Hex.encodeHexString(tree[i]); 
            assertTrue(curr.compareTo(last) >= 0); 
            last = curr; 
        } 
    } 
 
    @Test 
    public void testRecursiveSearch() { 
        FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(recursiveDirPath, true); 
        mTree.makeTree(); 
        assertTrue(mTree.getNumLeaves() == 8); 
    } 
 
    @Test 
    public void testRecursiveSearch2() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(recursiveDir2Path, true); 
        mTree.makeTree(); 
        assertTrue(mTree.getNumLeaves() == 8); 
    } 
     
    @Test 
    public void testFalseRecursiveSearch() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(recursiveDirPath, false); 
     mTree.makeTree(); 
     assertTrue(mTree.getNumLeaves() == 4); 
    } 
     
    @Test 
    public void testFalseRecursiveSearch2() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(recursiveDir2Path, false); 
     mTree.makeTree(); 
     assertTrue(mTree.getNumLeaves() == 2); 
    } 
 
    @Test 
    public void testRootHashEquivalence() { 
     FileMerkle mTree1 = new FileMerkle(HashType.SHA256); 
        mTree1.addTracking(recursiveDirPath, true); 
        FileMerkle mTree2 = new FileMerkle(HashType.SHA256); 
        mTree2.addTracking(recursiveDir2Path, true); 
        mTree1.makeTree(); 
        mTree2.makeTree(); 
        String tree1Root = Hex.encodeHexString(mTree1.getRootHash()); 
        String tree2Root = Hex.encodeHexString(mTree2.getRootHash()); 
        assertTrue(tree1Root.compareTo(tree2Root) == 0); 
    } 
     
    @Test 
    public void testDoubleSHA256(){ 
     FileMerkle mTree1 = new FileMerkle(HashType.DOUBLE_SHA256); 
        mTree1.addTracking(recursiveDirPath, true); 
        FileMerkle mTree2 = new FileMerkle(HashType.SHA256); 
        mTree2.addTracking(recursiveDir2Path, true); 
        mTree1.makeTree(); 
        mTree2.makeTree(); 
        String tree1Root = Hex.encodeHexString(mTree1.getRootHash()); 
        String tree2Root = Hex.encodeHexString(mTree2.getRootHash()); 
        assertTrue(tree1Root.compareTo(tree2Root) != 0); 
    } 
 
    @Test 
    public void testEmptyDirectory() { 
        FileMerkle mTree = new FileMerkle(HashType.SHA256); 
        mTree.addTracking(emptyDirPath, false); 
        assertTrue(mTree.makeTree() != null); 
    } 
     
    @Test 
    public void testTrackSpecificFile() throws IOException { 
     File tempFile = new File(emptyDirPath.getAbsolutePath() + "/tempFile"); 
     FileUtils.write(tempFile, "temp data"); 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(tempFile, false); 
     mTree.makeTree(); 
     assertTrue(mTree.getRootHash() != null); 
    } 
     
    @Test 
    public void testTrackMultipleFiles() throws IOException { 
     File tempFile = new File(emptyDirPath.getAbsolutePath() + "/tempFile"); 
     File tempFile2 = new File(emptyDirPath.getAbsolutePath() + "/tempFile2"); 
     FileUtils.write(tempFile, "temp data"); 
     FileUtils.write(tempFile2, "temp data 2"); 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     FileMerkle mTreeNoTemp2 = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(tempFile, false); 
     mTree.addTracking(tempFile2, false); 
     mTreeNoTemp2.addTracking(tempFile, false); 
     assertTrue(!Hex.encodeHex(mTree.getRootHash()).equals(Hex.encodeHex(mTreeNoTemp2.getRootHash()))); 
    } 
     
    @Test 
    public void testNoDuplicateTrack() throws IOException { 
     File tempFile = new File(emptyDirPath.getAbsolutePath() + "/tempFile"); 
     File tempFile2 = new File(emptyDirPath.getAbsolutePath() + "/tempFile2"); 
     FileUtils.write(tempFile, "temp data"); 
     FileUtils.write(tempFile2, "temp data 2"); 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(tempFile, false); 
     mTree.addTracking(emptyDirPath, false); 
     assertEquals(0, mTree.getTrackedFiles().size()); 
     assertEquals(1, mTree.getTrackedDirs().size()); 
    } 
     
    @Test 
    public void testNoDuplicateTrackRecursive() { 
     File testLevel2 = new File(FileMerkleTest.class.getResource(RECURSIVEDIR2).getPath() + "/testLevel1/testLevel2"); 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(testLevel2, false); 
     mTree.addTracking(recursiveDir2Path, true); 
     assertEquals(1, mTree.getTrackedDirs().size()); 
    } 
     
    @Test 
    public void testMultipleDirTracking() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(completeDirPath, false); 
     mTree.addTracking(incompleteDirPath, false); 
     mTree.makeTree(); 
     assertEquals(22, mTree.getNumLeaves()); 
     assertEquals(5, mTree.getHeight()); 
    } 
     
    @Test 
    public void testRemoveTracking() throws IOException { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     File tempFile = new File(emptyDirPath.getAbsolutePath() + "/tempFile"); 
     File tempFile2 = new File(emptyDirPath.getAbsolutePath() + "/tempFile2"); 
     FileUtils.write(tempFile, "temp data"); 
     FileUtils.write(tempFile2, "temp data 2"); 
     mTree.addTracking(tempFile, false); 
     mTree.addTracking(tempFile2, false); 
     String twoFileRoot = Hex.encodeHexString(mTree.makeTree()); 
     mTree.removeTracking(tempFile2); 
     String oneFileRoot = Hex.encodeHexString(mTree.makeTree()); 
     assertNotEquals(null, oneFileRoot, twoFileRoot); 
    } 
     
    @Test  
    public void testListPathCreation() { 
     FileMerkle mtree = new FileMerkle(HashType.SHA256); 
     mtree.addTracking(completeDirPath, false); 
     mtree.makeTree(); 
     byte[][] tree = mtree.getTree(); 
     byte[] startingHash = tree[9]; 
     List<MerklePathStep> path = mtree.findPath(startingHash); 
     assertFalse(path.get(0).onLeft()); 
     assertEquals(path.get(0).getHash(), tree[10]); 
      
     assertTrue(path.get(1).onLeft()); 
     assertEquals(path.get(1).getHash(), tree[3]); 
      
     assertFalse(path.get(2).onLeft()); 
     assertEquals(path.get(2).getHash(), tree[2]); 
    } 
     
    @Test 
    public void testCheckListPathValid(){ 
     FileMerkle mtree = new FileMerkle(HashType.SHA256); 
     mtree.addTracking(completeDirPath, false); 
     mtree.makeTree(); 
     byte[][] tree = mtree.getTree(); 
     byte[] startingHash = tree[9]; 
     List<MerklePathStep> path = mtree.findPath(startingHash); 
      
     assertTrue(mtree.checkPath(path)); 
      
    } 
     
    @Test 
    public void testCheckListShortPath(){ 
     FileMerkle mtree = new FileMerkle(HashType.SHA256); 
     mtree.addTracking(completeDirPath, false); 
     mtree.makeTree(); 
     byte[][] tree = mtree.getTree(); 
     byte[] startingHash = tree[9]; 
     List<MerklePathStep> path = mtree.findPath(startingHash); 
     path.remove(0); 
     assertTrue(!mtree.checkPath(path));      
    } 
     
    @Test 
    public void testCheckListInvalidPath(){ 
     FileMerkle mtree = new FileMerkle(HashType.SHA256); 
     mtree.addTracking(completeDirPath, false); 
     mtree.makeTree(); 
     byte[][] tree = mtree.getTree(); 
     byte[] startingHash = tree[9]; 
     List<MerklePathStep> path = mtree.findPath(startingHash); 
     //Set to an invalid byte [] 
     path.get(0).setFullHash(tree[2]); 
     assertTrue(!mtree.checkPath(path));      
    } 
     
    @Test 
    public void testCheckStepSerializer() { 
     FileMerkle mtree = new FileMerkle(HashType.SHA256); 
     mtree.addTracking(completeDirPath, false); 
     mtree.makeTree(); 
     byte[][] tree = mtree.getTree(); 
     byte[] startingHash = tree[9]; 
     List<MerklePathStep> path = mtree.findPath(startingHash); 
     byte[] serializedPath = MerkleStepSerializer.serialize(path); 
 
     for(int i = 0; i < path.size(); i++){ 
      assertTrue((serializedPath[i*33] != 0) == path.get(i).onLeft()); 
      for(int j = 0; j < 32; j++){ 
       assertTrue((serializedPath[(i*33) + j + 1]) == path.get(i).getHash()[j]); 
      } 
     } 
    } 
     
    @Test 
    public void testSerialization() { 
     FileMerkle mTree = new FileMerkle(HashType.SHA256); 
     mTree.addTracking(completeDirPath, false); 
     mTree.makeTree(); 
     SerialMerkleUtils.writeToFile(mTree, new File(completeDirPath, "saveTree")); 
     FileMerkle recoveryTree = SerialMerkleUtils.readFileMerkleFromFile(new File(completeDirPath, "saveTree")); 
     assertTrue(Hex.encodeHexString(recoveryTree.getRootHash()).equals(Hex.encodeHexString(mTree.getRootHash()))); 
     FileUtils.deleteQuietly(new File(completeDirPath, "saveTree")); 
    } 
}