package cn.signit.service.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.signit.domain.mysql.EvidenceInfo;
import cn.signit.server.conf.DataConfig;
import cn.signit.server.conf.MongoConfig;
import cn.signit.server.conf.ServiceConfig;
import cn.signit.server.conf.StoreServiceConfig;
import cn.signit.service.db.EvidencesService;
import cn.signit.untils.file.FileHandler;
import cn.signit.utils.DecimalFormator;

/**
*(这里用一句话描述这个类的作用)
* @ClassName EnvidenceServiceTest
* @author Liwen
* @date 2016年11月17日-下午1:18:58
* @version (版本号)
* @see (参阅)
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
		DataConfig.class,ServiceConfig.class,MongoConfig.class,StoreServiceConfig.class,
})
@WebAppConfiguration
public class EnvidenceServiceTest {
	@Autowired
	EvidencesService evidenceService;
	
	@Test
	public void testAdd() throws FileNotFoundException{
		File file=new File("H:\\pdf\\PDF测试 (4).pdf");
		EvidenceInfo info=new EvidenceInfo().init().setName(file.getName())
				.setSize(DecimalFormator.getDouble(file.length()/1024))
				.attachFile(new FileInputStream("H:\\pdf\\PDF测试 (4).pdf"));
		evidenceService.addEvidencesAndGetId(1L, info);
		Long id=info.getId();
		System.out.println(id);
	}
}
