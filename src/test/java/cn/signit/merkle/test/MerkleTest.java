package cn.signit.merkle.test;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.DecoderException; 
import org.apache.commons.codec.binary.Hex;

import org.bitcoinj.core.Utils;

import org.junit.rules.TemporaryFolder;

import cn.signit.merkle.HashType;
import cn.signit.merkle.Merkle;
import cn.signit.merkle.SerialMerkleUtils;

import org.junit.ClassRule;
import org.junit.Test;

/**
*(这里用一句话描述这个类的作用)
* @ClassName MerkleTest
* @author Liwen
* @date 2016年10月31日-下午3:44:16
* @version (版本号)
* @see (参阅)
*/
public class MerkleTest {
	@ClassRule 
	 public static TemporaryFolder emptyFolder = new TemporaryFolder(); 
	
	// using block 351043 
	 @Test 
	 public void testDoubleHash() throws DecoderException { 
	  List<byte[]> hashList = new ArrayList<byte[]>(); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("04a2808134e646ba67ff83f0bc7535a008b6e154c98953f5e2c9d40429880faf" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("b6b3ff7b4d004a788c751f3f8fc881f96c7b647ae06eb9a720bddc924e6f9147" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("e614ebb7e059e248e1f4c440f91af5c9617394a05d72233d7acf6feb153362f1" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("5bbc4545145126108c91689e62c1806646468c547999241f5c2883a526e015b6" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("de56c21783d3d466c0a5a155ed909c7011879df1996d8c418dac74465ebc3564" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("d327f96d32afdbf4238458684570189de26ba5dc300d5cd19fa1a9cdcecdb527" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("702c3d845810f31c194e7c9ea3d2b3636f3b8b9ee71f3d93a2f36e9d1a4e9a81" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("b320e44b0e4cbe5973b4ebdea0c63939f9cc196982e3f4d15daaa1baa16f0004" 
	      .toCharArray()))); 
	 
	  Merkle t = new Merkle(HashType.DOUBLE_SHA256); 
	  t.makeTree(hashList); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(t.getRootHash()), 
	      Hex.decodeHex("0b0192e318af62f8f91243948ea4c7ea9d696197e88b9401bce35ecb0a0cb59b" 
	        .toCharArray()))); 
	 } 
	 
	 @Test 
	 public void testSerialization() throws DecoderException { 
	  List<byte[]> hashList = new ArrayList<byte[]>(); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("04a2808134e646ba67ff83f0bc7535a008b6e154c98953f5e2c9d40429880faf" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("b6b3ff7b4d004a788c751f3f8fc881f96c7b647ae06eb9a720bddc924e6f9147" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("e614ebb7e059e248e1f4c440f91af5c9617394a05d72233d7acf6feb153362f1" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("5bbc4545145126108c91689e62c1806646468c547999241f5c2883a526e015b6" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("de56c21783d3d466c0a5a155ed909c7011879df1996d8c418dac74465ebc3564" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("d327f96d32afdbf4238458684570189de26ba5dc300d5cd19fa1a9cdcecdb527" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("702c3d845810f31c194e7c9ea3d2b3636f3b8b9ee71f3d93a2f36e9d1a4e9a81" 
	      .toCharArray()))); 
	  hashList.add(Utils.reverseBytes(Hex 
	    .decodeHex("b320e44b0e4cbe5973b4ebdea0c63939f9cc196982e3f4d15daaa1baa16f0004" 
	      .toCharArray()))); 
	 
	  Merkle t = new Merkle(HashType.DOUBLE_SHA256); 
	  t.makeTree(hashList); 
	  File savedTree = new File(emptyFolder.getRoot(), "save"); 
	  SerialMerkleUtils.writeToFile(t, savedTree); 
	  Merkle recovered = SerialMerkleUtils.readMerkleFromFile(savedTree); 
	 
	  assertTrue(Hex.encodeHexString(t.getRootHash()).equals( 
	    Hex.encodeHexString(recovered.getRootHash()))); 
	 } 
	 
	 @Test 
	 public void testEndiannessFlipper() throws DecoderException { 
	  Merkle m = new Merkle(HashType.DOUBLE_SHA256); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("04a2808134e646ba67ff83f0bc7535a008b6e154c98953f5e2c9d40429880faf" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("04a2808134e646ba67ff83f0bc7535a008b6e154c98953f5e2c9d40429880faf" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("b6b3ff7b4d004a788c751f3f8fc881f96c7b647ae06eb9a720bddc924e6f9147" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("b6b3ff7b4d004a788c751f3f8fc881f96c7b647ae06eb9a720bddc924e6f9147" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("e614ebb7e059e248e1f4c440f91af5c9617394a05d72233d7acf6feb153362f1" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("e614ebb7e059e248e1f4c440f91af5c9617394a05d72233d7acf6feb153362f1" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("5bbc4545145126108c91689e62c1806646468c547999241f5c2883a526e015b6" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("5bbc4545145126108c91689e62c1806646468c547999241f5c2883a526e015b6" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("de56c21783d3d466c0a5a155ed909c7011879df1996d8c418dac74465ebc3564" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("de56c21783d3d466c0a5a155ed909c7011879df1996d8c418dac74465ebc3564" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("d327f96d32afdbf4238458684570189de26ba5dc300d5cd19fa1a9cdcecdb527" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("d327f96d32afdbf4238458684570189de26ba5dc300d5cd19fa1a9cdcecdb527" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("702c3d845810f31c194e7c9ea3d2b3636f3b8b9ee71f3d93a2f36e9d1a4e9a81" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("702c3d845810f31c194e7c9ea3d2b3636f3b8b9ee71f3d93a2f36e9d1a4e9a81" 
	          .toCharArray())))); 
	  assertTrue(Arrays 
	    .equals(Utils.reverseBytes(Hex 
	      .decodeHex("b320e44b0e4cbe5973b4ebdea0c63939f9cc196982e3f4d15daaa1baa16f0004" 
	        .toCharArray())), 
	      m.flipByteEndianness(Hex 
	        .decodeHex("b320e44b0e4cbe5973b4ebdea0c63939f9cc196982e3f4d15daaa1baa16f0004" 
	          .toCharArray())))); 
	 } 
}
