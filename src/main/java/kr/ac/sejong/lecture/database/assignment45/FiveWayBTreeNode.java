package kr.ac.sejong.lecture.database.assignment45;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FiveWayBTreeNode {
	private int m; //children의 개수
	private int keynum;	//key개수
	
	private FiveWayBTreeNode parent;
	private List<Integer> keyList; 
	private List<FiveWayBTreeNode> children; 
	
	
	public int getKeynum() {
		return keynum;
	}
	public void setKeynum(int keynum) {
		this.keynum = keynum;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) { 
		this.m = m;
	}
	public FiveWayBTreeNode getParent() {
		return parent;
	}
	public void setParent(FiveWayBTreeNode parent) {
		this.parent = parent;
	}
	public List<Integer> getKeyList() {
		return keyList;
	}
	public void setKeyList(List<Integer> keyList) {
		this.keyList = keyList;
	}
	public List<FiveWayBTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<FiveWayBTreeNode> children) {
		this.children = children;
	}

}
