package cn.signit.server.conf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.treewalk.filter.OrTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.signit.conf.ConfigProps;
import cn.signit.utils.repo.TreeFilterType;


@Configuration
public class CategoryFilterConfig {

	private final static Logger LOG = LoggerFactory.getLogger(CategoryFilterConfig.class);
	
	@Bean
	public TreeFilterType getDocCategory() {
		LOG.info("======================>> 初始化类型过滤器 {}", CategoryFilterConfig.class.getName());
		
		String[] doc = ConfigProps.get("filter.doc").split(" ");
		List<TreeFilter> docFilterList = new ArrayList<TreeFilter>();
		for (String item : doc) {
			docFilterList.add(PathSuffixFilter.create(item));
			docFilterList.add(PathSuffixFilter.create(item.toUpperCase()));
		}
		
		String[] image = ConfigProps.get("filter.image").split(" ");
		List<TreeFilter> imageFilterList = new ArrayList<TreeFilter>();
		for (String item : image) {
			imageFilterList.add(PathSuffixFilter.create(item));
			imageFilterList.add(PathSuffixFilter.create(item.toUpperCase()));
		}
		
		String[] video = ConfigProps.get("filter.video").split(" ");
		List<TreeFilter> videoFilterList = new ArrayList<TreeFilter>();
		for (String item : video) {
			videoFilterList.add(PathSuffixFilter.create(item));
			videoFilterList.add(PathSuffixFilter.create(item.toUpperCase()));
		}
	
		TreeFilter[] filterList = 
			{ 
				OrTreeFilter.create(docFilterList), 
				OrTreeFilter.create(imageFilterList), 
				OrTreeFilter.create(videoFilterList)
			};
		
		TreeFilterType filterType = new TreeFilterType();
		filterType.setDocsFilter(filterList[0]);
		filterType.setImageFilter(filterList[1]);
		filterType.setVideoFilter(filterList[2]);
		filterType.setOtherFilter(OrTreeFilter.create(filterList).negate());
		
		return filterType;
	}
}
