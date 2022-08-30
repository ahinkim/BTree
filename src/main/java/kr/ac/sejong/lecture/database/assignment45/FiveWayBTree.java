package kr.ac.sejong.lecture.database.assignment45;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.Stack;

public class FiveWayBTree implements NavigableSet<Integer> {
	
	@SuppressWarnings("unused")
	private FiveWayBTreeNode root;
	public static int n; //**
	
	public FiveWayBTree(){
		n = 0;
		root = new FiveWayBTreeNode();
		root.setM(0);
		root.setKeynum(0);
		root.setParent(null);
	}
	
	class BTreeIterator implements Iterator<Integer>{
		
		private Stack<FiveWayBTreeNode> nodeStack;
		private Stack<Integer> idxStack;
		
		public BTreeIterator() {	//iterator 초기화
			nodeStack  = new Stack<>();
			idxStack = new Stack<>();
			if (root.getKeynum() > 0)
				pushLeft(root);	//처음 시작 할 때 pushLeft함수 호출
		}
		
		public boolean hasNext() {
			return !nodeStack.isEmpty();	//스택이 비어 있지 않으면 true
		}
		
		
		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			FiveWayBTreeNode node = nodeStack.peek();
			int idx = idxStack.pop();
			Integer result = node.getKeyList().get(idx);	
			idx++;
			if (idx < node.getKeynum())	//리스트를 idx++을 push하면서 차례대로 돌기
				idxStack.push(idx);
			else
				nodeStack.pop();	//node의 리스트를 다 돌았을 경우 node를 stack에서 빼기
			if (node.getM()!=0)	//노드의 자식이 있다면 현재 idx의 children을 push함으로써 현재 노드의 왼쪽자식 노드부터 맨 끝 왼쪽 자식 노드까지 다 push
				pushLeft(node.getChildren().get(idx));
			return result;
		}
		
		
		
		private void pushLeft(FiveWayBTreeNode node) {
			while (true) {	//현재 노드의 제일 왼쪽에 있는 노드까지 push 그래야 맨 왼쪽부터 순서대로 iterator 가능
				nodeStack.push(node);	
				idxStack.push(0);	
				if (node.getM()==0)
					break;
				node = node.getChildren().get(0);
			}
		}
	}
	
	@Override
	public Comparator<? super Integer> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer first() { //루트노드에서 노드의 맨 왼쪽 아래 노드의 key 리스트중 제일 앞에 있는 key 반환
		FiveWayBTreeNode p = root;
		while(p.getChildren()!=null) {
			p = p.getChildren().get(0);
		}
		return p.getKeyList().get(0);
	}

	@Override
	public Integer last() {	//루트노드에서 노드의 맨 오른쪽 아래 노드의 key 리스트중 제일 끝에 있는 key 반환
		FiveWayBTreeNode p = root;
		while(p.getChildren()!=null) {
			p = p.getChildren().get(p.getM()-1);
		}
		return p.getKeyList().get(p.getKeynum()-1);
	}

	@Override
	public int size() {
		return n; 
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size()==0;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FiveWayBTreeNode search(FiveWayBTreeNode h, Integer e) { 
		List<Integer> list = h.getKeyList();	
		for(int j = 0; j<h.getKeynum();j++) {	//현재 노드의 리스트 돌기
			if(list.get(j).intValue() == e) {	//리스트이 값과 e 같으면 null반환
				return null;
			}
			else if(e<list.get(j).intValue()) {	//list의 값보다 e가 작으면 왼쪽 자식노드 search
				if(h.getChildren()!=null)
					return search(h.getChildren().get(j),e);
			}
			if(e>list.get(j).intValue() && j+1==h.getKeynum()) { //list의 값보다 e가 크면 오른쪽 자식노드 search
				if(h.getChildren()!=null)
					return search(h.getChildren().get(j+1),e);
			}
		}
		return h;
	}
	
	
	public FiveWayBTreeNode newNode() {
		FiveWayBTreeNode newNode = new FiveWayBTreeNode();
		newNode.setChildren(null);
		newNode.setKeynum(0);
		newNode.setM(0);
		newNode.setParent(null);
		return newNode;
	}
	
	public void split(FiveWayBTreeNode u, List<Integer> u_list) { //쪼개기
		FiveWayBTreeNode middle = newNode(); //middle노드 만들기
		List<Integer> middleList = new ArrayList<Integer>();
		int i ;
		middleList.add(u_list.get(2));
		middle.setKeyList(middleList);
		middle.setKeynum(1);
		
		List<Integer> leftList = new ArrayList<Integer>();
		List<Integer> rightList = new ArrayList<Integer>();
		
		FiveWayBTreeNode right = newNode(); //right노드 만들기
		right.setKeyList(rightList);
		if(u.getM()>3)	//쪼갤 노드의 자식 개수가 3이상일 때 right의 자식 만들어주기
		{
			right.setChildren(new ArrayList<FiveWayBTreeNode>());
		}
		for(i = 0 ;i<2;i++)	//right 분할 && right의 자식 분할
		{
			right.getKeyList().add(u.getKeyList().get(3+i));
			if(3+i<u.getM())
			{
				right.getChildren().add(u.getChildren().get(3+i));
				right.getChildren().get(i).setParent(right);
				right.setM(i+1);
			}
		}
		if(3+i<u.getM())	
		{
			right.getChildren().add(u.getChildren().get(3+i));
			right.getChildren().get(i).setParent(right);
			right.setM(i+1);
		}
		
		right.setKeynum(2);
		
		//left노드 만들기
		FiveWayBTreeNode left = newNode();
		left.setKeynum(2);
		left.setKeyList(leftList);
		
		if(u.getM()>0)	///쪼갤 노드의 자식 개수가 있다면 left의 자식 만들어주기
		{
			left.setChildren(new ArrayList<FiveWayBTreeNode>());
		}
		
		for(i=0; i<2;i++)	//left 분할 && left의 자식 분할
		{
			left.getKeyList().add(u.getKeyList().get(i));
			if(i<u.getM())
			{
				left.getChildren().add(u.getChildren().get(i));
				left.getChildren().get(i).setParent(left);
				left.setM(i+1);
			}
		}
		if(i<u.getM())
		{
			left.getChildren().add(u.getChildren().get(i));
			left.getChildren().get(i).setParent(left);
			left.setM(i+1);
		}
		
		middle.setChildren(new ArrayList<FiveWayBTreeNode>());
		
		if(u.getParent()==null) { //root인경우
			root = middle;	
			root.getChildren().add(left); //middle의 child left, right로 초기화 
			root.getChildren().add(right);
			root.setM(2);	//미들의 자식 개수 = 2
			left.setParent(root); //left, right parent 초기화
			right.setParent(root);
		}
		else //위에 부모가 있을 때 쪼개진 후 합치기
		{
			int k = insert(u.getParent(),u_list.get(2),1);//부모노드에 삽입된 후 삽입된 인덱스 반환
			
			List<FiveWayBTreeNode> children_list = u.getParent().getChildren(); //child1,2를 삽입 할 children_list
			children_list.add(left); // 임시로 넣어서 children_list 공간 확보
			u.getParent().setM(u.getParent().getM()+1);//children 노드 개수 ++

			for(int j = u.getParent().getM()-2;j>k;j--) {	//children 노드들 1칸씩 옆으로 이동
				children_list.set(j+1, children_list.get(j));
			}
			children_list.set(k, left); //부모 인덱스가 k일 때, 자식 인덱스 k, k+1로 세팅
			children_list.set(k+1, right);
			
			//middle.setParent(u.getParent().getParent()); //middle의 parent 삽입한 노드의 부모로 초기화
			left.setParent(u.getParent()); //left,right의 부모 middle로 초기화
			right.setParent(u.getParent());
			
			u.getParent().setChildren(children_list);
			
			u = u.getParent();
			if(u.getKeynum()>4) {	//미들을 삽입한 노드의 key의 개수가 4를 넘어가면 split 다시 시도
				split(u,u.getKeyList());
			}
			else
			{
				return; //key 개수 4를 넘어가지 않으면 finish
			}
			
		}
	}
	
	
	
	public int insert(FiveWayBTreeNode u,Integer e, int f) { // f = 0이면 add에서 insert할 때, 1이면 split에서 호출될 때(split무한 호출을 막기위함)
		int k=0;
		List<Integer> newList = new ArrayList<Integer>();
		for(int i=0;i<u.getKeynum();i++) {	//node의 list중 insert할 인덱스 찾기
			if(e<u.getKeyList().get(i)) {
				k = i;
				break;
			}
			if(e>u.getKeyList().get(i) && i+1==u.getKeynum()) {
				k = i+1;
				break;
			}
		}
		if(u.getKeyList() == null)
			u.setKeyList(newList);
		List<Integer> u_list = u.getKeyList();

		u_list.add(e);	//list에 삽입할 e를 add하고  list정렬 상태 유지
		u_list.sort(null);
		u.setKeyList(u_list);
		u.setKeynum(u.getKeynum()+1);
		
		
		//maximum 검사
		if(u.getKeynum()>4 && f==0) {	//maxkeynum을 위반할 경우 split함수 호출
			split(u,u_list);
		}
		return k;
	}
	
	public FiveWayBTreeNode rightleft(FiveWayBTreeNode node, int index) { //오른쪽아래노드로 가서 제일 왼쪽값
		
		if(node.getM()-1<index+1)	//자식의 개수보다 index-1이 더 크면 반환할자식이 없는 것
			return null;
		
		node = node.getChildren().get(index+1); //오른쪽노드로가기
		if(node.getM()==0)
			return node;
		
		FiveWayBTreeNode p = node.getChildren().get(0);
	
		if(p.getChildren()!=null) {
			p=p.getChildren().get(0);
		}
		return p;
	}
	public FiveWayBTreeNode leftright(FiveWayBTreeNode node, int index) { //왼쪽아래노드로 가서 제일 오른쪽값
		if(index<0)	//0인덱스 전 예외처리
			return null;
		
		if(node.getM()-1<index)	//자식의 개수보다 index-1이 더 크면 반환할자식이 없는 것
			return null;
		
		node = node.getChildren().get(index);	//왼쪽노드로 가기
		if(node.getM()==0)
			return node;
		FiveWayBTreeNode p = node.getChildren().get(node.getM()-1);//index+1
	
		if(p.getChildren()!=null) {
			int m = p.getM();
			p=p.getChildren().get(m-1);
		}
		return p;
	}
	@Override
	public boolean add(Integer e) {
		FiveWayBTreeNode u = search(root,e);
		if(u==null) {
			return true;
		}
		insert(u,e,0);
		n++;
		return true;
	}
	
	public FiveWayBTreeNode search2(FiveWayBTreeNode h, Integer e) { //remove에 쓸 search
		List<Integer> list = h.getKeyList();	
		for(int j = 0; j<h.getKeynum();j++) {	//현재 노드의 리스트 돌기
			if(list.get(j).intValue() == e.intValue()) {	
				return h;	//search랑 다른 점 : null이 아닌 자기 자신 반환
			}
			else if(e.intValue()<list.get(j).intValue()) {	
				if(h.getChildren()!=null)
					return search2(h.getChildren().get(j),e.intValue());
			}
			if(e.intValue()>list.get(j).intValue() && j+1==h.getKeynum()) { 
				if(h.getChildren()!=null)
					return search2(h.getChildren().get(j+1),e.intValue());
			}
		}
		return null;//삭제할 노드 없으면 null반환
	}
	
	@Override
	public boolean remove(Object obj) {
		Objects.requireNonNull(obj);
		@SuppressWarnings("unchecked")
		Integer key = (Integer)obj;
		int index=0;
		int f = 0;
		int T = 0;
		//int myindex =0 ;
		// Walk down the tree
		FiveWayBTreeNode node = search2(root,key); //삭제할 노드 찾기
		if(node==null)	return false;
		
		for(int i =0; i<node.getKeynum();i++) {
			if(node.getKeyList().get(i) == key.intValue()) {
				index = i;	//해당노드에서 삭제할 인덱스 저장
				break;
			}
		}
		
		while (true) {
				if (node.getM()==0 || T==1) { //leaf node //P==T일 때 t=1 여기로 들어와서 재정비
					if (index >= 0) {  //f==1이면 이미 한 번 삭제 한것, while문 한 번 돌 때 삭제는 한 번만
						if(f==0)
						{
							node.getKeyList().remove(index);
							node.setKeynum(node.getKeynum()-1);
							n--;
							f = 1;
						}
						if(node.getKeynum()>=2) { //minkey property를 위배 x
							return true;
						}
						
						//minkey property를 위배했다면 
						//if(node.getParent()!=null)
						
						int myindex = 0;
						for(int i = 0; i<node.getParent().getM();i++) { //children리스트에서 내 index찾기
							if(node.getParent().getChildren().get(i)==node) 
							{
								myindex = i;
								break;
							}
						}
						
						FiveWayBTreeNode left = null;
						FiveWayBTreeNode right = null;
						if(myindex>0) {
							left  = node.getParent().getChildren().get(myindex-1); 
						}
						if(myindex+1<node.getParent().getM()) {
							right  = node.getParent().getChildren().get(myindex+1); 
						}
						//왼쪽에서 빌리기
						if(left!=null&&left.getKeynum()>2) {
							Integer leftMax = left.getKeyList().get(left.getKeynum()-1); //left노드 키리스트에서 max값
							left.getKeyList().remove(left.getKeynum()-1); //left의 max값 지움
							left.setKeynum(left.getKeynum()-1);
							node.getKeyList().add(node.getParent().getKeyList().get(myindex-1)); //parent의 값 right로 가져오기
							node.setKeynum(node.getKeynum()+1); //빌린 후 키 수 증가
							node.getKeyList().sort(null);
							node.getParent().getKeyList().set(myindex-1,leftMax); //parent에 left값 가져오기
							node.getParent().getKeyList().sort(null);
							if(T==1) { // P==T이면 자식 노드까지 신경써야함
								node.getChildren().add(new FiveWayBTreeNode());
								//왼쪽 노드의 오른쪽자식을 현재노드의 맨 왼쪽자식으로 등록
								for(int i=node.getM()-1;i>=0;i--) {
									node.getChildren().set(i+1, node.getChildren().get(i));
								}
								node.getChildren().set(0, left.getChildren().get(left.getM()-1));
								left.getChildren().get(left.getM()-1).setParent(node);
								left.getChildren().remove(left.getM()-1);
								node.setM(node.getM()+1);
								left.setM(left.getM()-1);
							}
							return true;
						}
						else if(right!=null&&right.getKeynum()>2)//오른쪽에서 빌리기
						{
							Integer rightMin = right.getKeyList().get(0); //right노드 키리스트에서 min값
							right.getKeyList().remove(0); //right의 min값 지움
							right.setKeynum(right.getKeynum()-1);
							node.getKeyList().add(node.getParent().getKeyList().get(myindex)); //parent의 값 left로 가져오기
							node.setKeynum(node.getKeynum()+1); //빌린 후 키 수 증가
							node.getKeyList().sort(null);
							node.getParent().getKeyList().set(myindex,rightMin); //parent에 right값 가져오기
							node.getParent().getKeyList().sort(null);
							if(T==1) { // P==T이면 자식 노드까지 신경써야함
								//오른쪽 노드의 왼쪽자식을 현재노드의 맨 오른쪽자식으로 등록
								node.getChildren().add(right.getChildren().get(0));
								right.getChildren().get(0).setParent(node);
								right.getChildren().remove(0);
								node.setM(node.getM()+1);
								right.setM(right.getM()-1);
							}
							return true;
						}
						else //merge
						{
							if(left!=null) //merge LS, PLV, T and be a left child of PRV
							{
								List<Integer> list = left.getKeyList();
								list.add(left.getParent().getKeyList().get(myindex-1));
								list.add(node.getKeyList().get(0));
								list.sort(null);
								left.getParent().getKeyList().remove(myindex-1);
								left.getParent().setKeynum(left.getParent().getKeynum()-1);
								left.getParent().getChildren().remove(myindex);
								left.getParent().setM(left.getParent().getM()-1);
								left.setKeyList(list);
								left.setKeynum(left.getKeynum()+2);
								if(T==1) { // P==T이면 자식 노드까지 신경써야함
									//System.arraycopy(right.getChildren(), 0, right.getChildren(), 2, right.getM());//왼쪽 노드의 오른쪽자식을 현재노드의 맨 왼쪽자식으로 등록
									node.getChildren().get(0).setParent(left);
									left.getChildren().add(node.getChildren().get(0));
									node.getChildren().get(1).setParent(left);
									left.getChildren().add(node.getChildren().get(1));
									
									left.setM(left.getM()+2);
									node.setM(node.getM()-2);
								}
								if(node.getParent().getKeynum()==0) { //node가 root인 경우 키가 없으면 합치고 root로 만들어줘야 함 
									root = left;
									root.setParent(null);
									return true;
								}
								if(node.getParent().getKeynum()<2 && node.getParent()!=root) { //부모가 minkeyproperty 위배한다면 //root는 key 개수 1이여도 됨
									node = node.getParent();
									index = 0; //부모가 minkeyproperty라는 건 하나남았다는 뜻이니까 index=0	
									T=1;// T=P 일 때 T=1
									continue;
								}
								else { //minkeyproperty위배x
									return true;
									}
							}
							else if(right!=null) {
								List<Integer> list = right.getKeyList();
								list.add(right.getParent().getKeyList().get(myindex));
								list.add(node.getKeyList().get(0));
								list.sort(null);
								right.getParent().getKeyList().remove(myindex);
								right.getParent().setKeynum(right.getParent().getKeynum()-1);
								right.getParent().getChildren().remove(myindex);
								right.getParent().setM(right.getParent().getM()-1);
								right.setKeyList(list);
								right.setKeynum(right.getKeynum()+2);
								if(T==1) { // P==T이면 자식 노드까지 신경써야함
									right.getChildren().add(new FiveWayBTreeNode());
									right.getChildren().add(new FiveWayBTreeNode());
									
									for(int i=right.getM()-1;i>=0;i--) {
										right.getChildren().set(i+2, right.getChildren().get(i));
									}
									right.setM(right.getM()+2);

									node.getChildren().get(0).setParent(right);
									right.getChildren().set(0, node.getChildren().get(0));
									
									node.getChildren().get(1).setParent(right);
									right.getChildren().set(1, node.getChildren().get(1));
									
									node.setM(node.getM()-2);
								}
								if(node.getParent().getKeynum()==0) { //키가 없으면 합치고 root로 만들어줘야 함 
									root = right;
									root.setParent(null);
									return true;
								}
								if(node.getParent().getKeynum()<2&& node.getParent()!=root) { //부모가 minkeyproperty 위배한다면
									node = node.getParent();
									//if(node==null) {root = right; return true;}//노드가 root인 경우
									//index값 재할당
									index = 0; //부모가 minkeyproperty라는 건 하나남았다는 뜻이니까 index=0	
									T =1 ;
									continue;
								}
								else
									return true;
									
							}
							return false;
						}
					} else
						return false; 
					
				} else {  //interner node //getM-1했는지확인하기
					if (index >= 0) {  
						if(f==0) //while문 한 번 돌 때 삭제 한 번
						{
							node.getKeyList().remove(index);
							node.setKeynum(node.getKeynum()-1);
							f = 1;
						}
						//FiveWayBTreeNode left  = node.getChildren().get(index); // 고치기: 왼쪽으로 가서 제일 오른쪽값
						//FiveWayBTreeNode right = node.getChildren().get(index + 1); //고치기 : 오른쪽으로가서 제일 왼쪽값
						FiveWayBTreeNode left = leftright(node,index);
						FiveWayBTreeNode right = rightleft(node,index);
						if (left!=null&&left.getKeynum() > 2) {   //왼쪽자식에서 빌리기
							Integer leftMax = left.getKeyList().get(left.getKeynum()-1); //left노드 키리스트에서 max값
							left.getKeyList().remove(left.getKeynum()-1); //left의 max값 지움
							left.setKeynum(left.getKeynum()-1);
							node.getKeyList().add(leftMax); //left의 max값을 빈 곳에 할당
							node.setKeynum(node.getKeynum()+1);
							node.getKeyList().sort(null);
							n--;
							return true;
						} else if (right!=null&&right.getKeynum() > 2) {  // 오른쪽 자식에서 빌리기
							Integer rightMin = right.getKeyList().get(0); //right노드 키리스트에서 min값
							right.getKeyList().remove(0); //right의 min값 지움
							right.setKeynum(right.getKeynum()-1);
							node.getKeyList().add(rightMin); //right의 min값을 빈 곳에 할당
							node.setKeynum(node.getKeynum()+1);
							node.getKeyList().sort(null);
							n--;
							return true;
						} else {  //left와 right에서 빌려오면 left나 right node가 min key property를 위배할 때
							if(left!=null) { //일단 left에서 빌려옴
								Integer leftMax = left.getKeyList().get(left.getKeynum()-1); //left값 일단 할당
								left.getKeyList().remove(left.getKeynum()-1); //left의 max값 지움
								left.setKeynum(left.getKeynum()-1);
								node.getKeyList().add(leftMax);//.set(index, leftMax); //left의 max값을 빈 곳에 할당
								node.setKeynum(node.getKeynum()+1);
								node.getKeyList().sort(null);
								node = left; //left = T로 두고 다시 돌기
								index = 0; //left minkey property위배상태니까 1개 남아서 idex = 0
							}
							else if(right!=null) { //일단 right에서 빌려옴
								Integer rightMin = right.getKeyList().get(0); //right노드 키리스트에서 min값
								right.getKeyList().remove(0); //right의 min값 지움
								right.setKeynum(node.getKeynum()-1);
								//node.getKeyList().set(index, rightMin); //right의 min값을 빈 곳에 할당
								node.getKeyList().add(rightMin);
								node.setKeynum(node.getKeynum()+1);
								node.getKeyList().sort(null);
								node = right; //right = T로 두고 다시 돌기
								index = 0;
							}
						}
					}
				}
			}
		}

	


	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer lower(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer floor(Integer e) {
		Integer prev = null;
		Integer cur;
		Iterator<Integer> BTtreeIterator = iterator();
		while (BTtreeIterator.hasNext()) {	//iterator 돌면서 prev 저장
			cur = BTtreeIterator.next();
			if (cur.intValue()==e.intValue()) {	//e랑 현재 노드의 리스트 값(cur) 같으면 그대로 e반환
				return e;
			}
			else if(e<cur) {	//cur보다 e가 작으면 바로 전 단계인 prev반환
				return prev;
			}
			else	//e>cur 이면 이 전값 prev 저장해두기
			{
				prev = cur;
			}
		}
		return prev;	//다 돌고도 값이 없으면 prev반환
	}

	@Override
	public Integer ceiling(Integer e) {
		Integer cur = null;
		Iterator<Integer> BTtreeIterator2 = iterator();
		while (BTtreeIterator2.hasNext()) {	//iterator 돌면서 prev 저장
			cur = BTtreeIterator2.next();
			if (cur.intValue()==e.intValue()) {	//e랑 현재 노드의 리스트 값(cur) 같으면 그대로 e반환
				return e;
			}
			else if(e<cur)	//cur보다 작으면 e 다음으로 큰 노드니까 cur반환
			{
				return cur;
			}
		}
		return null;	//다 돌고도 자기보다 큰 값이 없으면 null반환
	}

	@Override
	public Integer higher(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer pollLast() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new BTreeIterator();
	}

	@Override
	public NavigableSet<Integer> descendingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Integer> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
			boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
		return null;
	}

	@Override
	public NavigableSet<Integer> tailSet(Integer fromElement,boolean inclusive) {
		return null;
	}

	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		Iterator<Integer> BTtreeIterator = iterator();	
		int f = 0;
		Integer key;
		FiveWayBTree HeadSet = new FiveWayBTree();//HeadSet트리 새로 생성
		while(BTtreeIterator.hasNext()) {	
			key = BTtreeIterator.next(); 
			 if(key.intValue() < toElement.intValue()) {	//pivot미만의 값들 add
				 HeadSet.add(key);
			 }
		}
		return HeadSet;
	}

	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		Iterator<Integer> BTtreeIterator = iterator();
		int f = 0;
		Integer key;
		FiveWayBTree TailSet = new FiveWayBTree();	//TailSetTree새로 생성
		while(BTtreeIterator.hasNext()) {
			key = BTtreeIterator.next(); 
			 if(key.intValue() >= fromElement.intValue()) {	//pivot값이상 값들만 add
					TailSet.add(key);
			 }

		}
		return TailSet;
	}
	}

