package cn.signit.utils.repo;

import org.eclipse.jgit.treewalk.filter.TreeFilter;

public class TreeFilterType {
	private TreeFilter docsFilter;
	private TreeFilter videoFilter;
	private TreeFilter imageFilter;
	private TreeFilter otherFilter;
	
	public TreeFilter getFilter(String type) {
		
		if (type.equals("doc")) {
			return docsFilter;
		} else if (type.equals("video")) {
			return videoFilter;
		} else if (type.equals("image")) {
			return imageFilter;
		}
		
		return otherFilter;
	}
	
	public TreeFilter getDocsFilter() {
		return docsFilter;
	}
	public void setDocsFilter(TreeFilter docsFilter) {
		this.docsFilter = docsFilter;
	}
	public TreeFilter getVideoFilter() {
		return videoFilter;
	}
	public void setVideoFilter(TreeFilter videoFilter) {
		this.videoFilter = videoFilter;
	}
	public TreeFilter getImageFilter() {
		return imageFilter;
	}
	public void setImageFilter(TreeFilter imageFilter) {
		this.imageFilter = imageFilter;
	}
	public TreeFilter getOtherFilter() {
		return otherFilter;
	}
	public void setOtherFilter(TreeFilter otherFilter) {
		this.otherFilter = otherFilter;
	}
	
}
