import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.Map;


//class Tries
public class TrieDataStruct {
	private Vertex rootVertex;
	
	//default constructor for Tries class
	public TrieDataStruct(){
		rootVertex = (Vertex) new nodesInBranch(); //creating the branch level and assign its left and right to null
		rootVertex.AssignRight(null);
		rootVertex.AssignLeft(null);
	}
	
	// create a class Vertex which is private to the Tries class
		private class Vertex{
			
			//setter and getter methods for required keys and values and all the get methods return null
			public Vertex getRight(){	return null;}
			//trying to get the RightNode returns null
			public Vertex getLeft(){return null;} 
			//trying to get the LeftNode returns null
			public String getKey(){	return null;}
			//does not set anything
			public void AssignRight(Vertex r){ }
			
			public void AssignLeft(Vertex r){ }
			
			public Integer getValue() { return null;}
		}
		
		
		private class nodesInBranch extends Vertex{
			private Vertex left;
			private Vertex right;
			
			//default constructor for the next levelHeight where its left and right nodes are set to null
			public nodesInBranch(){
				left = null;
				right = null;
			}
			
			@Override
			public Vertex getRight(){//returns the right vertex
				return right;
			}
			
			@Override
			public Vertex getLeft(){//returns the left vertex
				return left;
			}
			
			public void AssignRight(Vertex r){//sets Right vertex
				right = r;
			}
			
			public void AssignLeft(Vertex r){//sets Left vertex
				left = r;
			}
		}

	//the class VertexElement extends the Vertex class and has the getKey and getValue methods
		private class vertexElement extends Vertex{
			private String key;
			private Integer value;
			
			public vertexElement(String key, Integer value){
				this.value = value;
				this.key = key;
			}
			
			//returns key
			public String getKey(){
				return key;
			}
			
			//returns value
			public Integer getValue(){
				return value;
			}
			
			
		}
	private void VisitNode(Vertex vertex1, Vertex vertex2, int destination){
		if(vertex2.getKey() == null){//if node2 is null
			if(vertex2.getLeft() != null && vertex2.getRight() != null){//if node2 left and right are not null
				if(vertex2.getLeft().getValue() != null && vertex2.getRight().getValue() != null){
					//if their values are not null
					if(vertex2.getLeft().getValue().equals(vertex2.getRight().getValue())){
						//if they are equal set the left and right of node1
						if(destination == 1){
							vertex1.AssignRight(vertex2.getLeft());
						}
						else{
							vertex1.AssignLeft(vertex2.getLeft());
						}
					}
				}
			}
			else{//if either of node2 left or right are null 
				if(vertex2.getLeft() == null && vertex2.getRight() != null){
					if( vertex2.getRight().getValue() != null){
						 //if right vertex value is not null
						if(destination == 1){
							//set node1 right to node2 right
							vertex1.AssignRight(vertex2.getRight());
						}
						else{//set node1 left to node2 right
							vertex1.AssignLeft(vertex2.getRight());
						}
						
					}
				}
				else{
					if(vertex2.getLeft() != null && vertex2.getRight() == null){
						if(vertex2.getLeft().getValue() != null){
							if(destination == 1){//set node1 right to node2 left
								vertex1.AssignRight(vertex2.getLeft());
							}
							else{ //set node1 left to node2 left
								vertex1.AssignLeft(vertex2.getLeft());
							}
						}
					}
				}				
			}
		}		
	}
	
	//a recursive post order traversal
	private void traversePostOrder(Vertex vertex1, Vertex vertex2, int destination){
		
		if (vertex2 != null){
			traversePostOrder(vertex2, vertex2.getLeft(), 0);
			traversePostOrder(vertex2, vertex2.getRight(), 1);
			VisitNode(vertex1, vertex2, destination);
			/*visiting the root at the end in post order. So a call is made to the method toVisit
            which visits the root vertex*/
		}
			
	}
	
	//simple method to traverse the tree in post order (call a recursive postorder traversal)
	public void traverse(){
		traversePostOrder(null, rootVertex, -1);
	}
	
	//the nodes are inserted based on the property of the root, its parent and its grand parent
	public void insertTrie (String key, Integer value) throws Exception{
		Vertex r = rootVertex;
		Vertex vertex1 = null;
		Vertex gp = null;
		int levelHeight = 0;
		
		//check if the root is null and if it is not keep moving to the next levels by assigning parent to grand parent and 
        //checking for the substring
		while(r != null){
			gp = vertex1;
			vertex1 = r;
			if(key.substring(levelHeight, levelHeight+1).equals("0")){
				r = r.getLeft();	
			}
			else{
				r = r.getRight();	
			}
			levelHeight += 1;
		}
		
		if(vertex1.getKey() == null){ //if the previous is not an element vertex
			Vertex newNode = new vertexElement(key, value);
			if(key.substring(levelHeight-1, levelHeight).equals("0")){
				vertex1.AssignLeft(newNode);
			}
			else{
				vertex1.AssignRight(newNode);
			}
		}else{ //vertex is element vertex
			if(vertex1.getKey().equals(key)){
				throw new Exception("Inserting duplicated IP key");
			}
			else{
				Vertex vertex2 = new nodesInBranch();
				//based on the previous level bit the vertex is set to either grand parent's left or right
				if(key.substring(levelHeight-2, levelHeight-1).equals("0")){
					gp.AssignLeft(vertex2);
				}
				else{
					gp.AssignRight(vertex2);
				}
				levelHeight = levelHeight -1; //decrease the levelHeight by one
				
				/*if the parent's bit and vertex's bit are same at that level the following steps are repeated until the 
                condition fails*/
				while(vertex1.getKey().substring(levelHeight, levelHeight+1).equals(key.substring(levelHeight, levelHeight+1))){
					Vertex n2 = new nodesInBranch();
					//set either right or left of the vertex based on the bit value
					if(key.substring(levelHeight, levelHeight+1).equals("0"))
						vertex2.AssignLeft(n2);
					else
						vertex2.AssignRight(n2);
					
					vertex2 = n2;
					levelHeight = levelHeight + 1;//increase the level by 1
				}
				
				if(key.substring(levelHeight, levelHeight+1).equals("0")){//if the bit is 0 at that level then its left is set to an instance of ELement
					//and right is set to parent else do the reverse
					vertex2.AssignLeft(new vertexElement(key, value));
					vertex2.AssignRight(vertex1);
				}
				else{
					vertex2.AssignRight(new vertexElement(key, value));
					vertex2.AssignLeft(vertex1);
				}
			}			
		}
	}
	
	// a simple method for searching for a value which returns false
	public boolean find(String value){
		
		return false;
	}
	
	//finding the longest prefix match string by initially considering an empty string 
	//and appending either 0 or 1 until both the conditions (root is not null and number of bits in the longest prefix 
	//match string is less than 32)
	public Map.Entry<Integer, String> longestPrefixMatch(String key){
		String match ="";
		Integer map;
		Vertex pq = null;
		Vertex q = rootVertex;
		int i = 0;
		while(q != null && i < 32){
			pq = q;
			//check if the bit at the position is zero, if so append 0 to the match string 
			//else append 1 and follow the right path
			if(key.substring(i, i+1).equals("0")){
				q = q.getLeft();
				if(q != null){match = match+"0";i += 1;}
			}else{
				q = q.getRight();
				if(q != null){match = match+"1";i += 1;}
			}
			
		}
		//if the number of bits is 32 then the value in root vertex is assigned to map 
		//else the value in n1 is assigned to map
		if(i == 32) map = q.getValue();
		else map = pq.getValue();
		
		
		Map.Entry<Integer,String> result = new AbstractMap.SimpleEntry<Integer, String>(map ,match );
		return result;
	}
	
}
