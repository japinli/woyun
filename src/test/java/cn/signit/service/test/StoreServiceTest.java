/**
* @author:ZhangHongdong
* @date:2016年2月22日-上午9:49:16
* @see: (参阅)
*/
package cn.signit.service.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.signit.domain.mongo.file.LocalStoreFile;
import cn.signit.pkcs.p12.FileHandler;
import cn.signit.server.conf.MongoConfig;
import cn.signit.server.conf.StoreServiceConfig;
import cn.signit.service.files.MongoStoreService;


/**
 *(这里用一句话描述这个类的作用)
 * @ClassName: StoreServiceTest
 * @author:ZhangHongdong
 * @date:2016年2月22日-上午9:49:16
 * @version:(版本号)
 * @see: (参阅)
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
		MongoConfig.class,StoreServiceConfig.class
})
@WebAppConfiguration
public class StoreServiceTest{
	@Autowired
	MongoStoreService mongoStoreService;
	
	@Test
	public void testSave() throws FileNotFoundException{
		LocalStoreFile sf = new LocalStoreFile();
		sf.setTimeStamp("asdfsfd");
		sf.contentStream(new FileInputStream("H:\\pdf\\PDF测试 (4).pdf"));
		sf.setUri("adsfjasklfjlksjflj");
		sf.setPath("asdfjlkasfjlkflk/asdfkj");
		sf.setSignature("asdfalskfjkljflkj");
        Object id = mongoStoreService.save(sf);
        System.out.println(id);
        //56ca74b7454c46234c67779f
	}
	
	@Test
	public void testGet() throws IOException{
		long s = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			LocalStoreFile sf = mongoStoreService.get("5820227b0e13aa116ce7c87d");//
			FileHandler.writeStream2Path(sf.getContentStream(), "H:\\pdf\\PDF测试 (4)_.pdf");
			//System.out.println(BeanToMap.INSTANCE.getMap(sf));
		}
		long e = System.currentTimeMillis();
		System.out.println((e - s )/1000+"秒");
	}
	
	@Test
	public void testDelete(){
		int ret = mongoStoreService.delete("56ca7466454c4622a8896cf3");
		System.out.println(ret);
	}
	
	@Test
	public void testUpdate(){
		LocalStoreFile sf = mongoStoreService.get("56ca7686454c46249c98698b");
		sf.setFilename("zhdppppppppppppppppzhd.pdf");
		Object newId = mongoStoreService.update(sf);
		System.out.println(newId);
	}
}
