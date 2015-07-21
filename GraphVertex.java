import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class GraphVertex{
	//GraphVertex attributes defined
	private GrpNode 						source;
	private GrpNode 						destination;
	private Map<Integer, GrpNode> 	    vertices;
	private int							noOfNodes, noOfEdges;
 	
	//Default values assigned to the atributes
	public GraphVertex (){
		vertices = new HashMap<Integer, GrpNode>();
		source = null;
		destination = null;
		noOfNodes = 0;
		noOfEdges = 0;
	}
	//a node of the graph 
	private class GrpNode{
			
			//attributes of a node in a graph
			private Integer index;
			private Map<Integer, Integer> adjacent; 
			private TrieDataStruct	routingTable;
			
			public GrpNode(Integer index) {
				this.index = index;//assign the index to the node
				adjacent = new HashMap<Integer, Integer>();
				routingTable = new TrieDataStruct();
			}
			
			//returns the index of a specified node
			public Integer getIndex() {
				return index;
			}

			//assigns the index of a particular node
			public void setIndex(Integer index) {
				this.index = index;
			}
			
			//adding the vertex + edge weight into the adjacent map.
			public void adjacentVertex(Integer vertex2, Integer weight){
				adjacent.put(vertex2, weight);			
			}

			//get adjacent vertex set
			public Set<Entry<Integer, Integer>> getadjacents(){
				return adjacent.entrySet();
			}
			
			//insert the next node into routing table
			public void insertPairIntoRouting(String destination, Integer next) throws Exception{ 
				routingTable.insertTrie(destination, next);
			}
			
			//returns the routing table
			public TrieDataStruct retrieveRoutingTable(){
				return routingTable;
			}
						
		}
			
	//Reads the file and parses it.
	public void construct (String file) throws IOException{
	
		FileInputStream inputFile = new FileInputStream(file);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(inputFile));

		String lineInput;
		
		//Read First Line
		lineInput = buffRead.readLine();
		String[] input = lineInput.split(" ");
		noOfNodes = Integer.parseInt(input[0]); //first integer of file contains total no of vertices
		noOfEdges = Integer.parseInt(input[1]);//second integer of file contains total no of edges
		buffRead.readLine();
		
		//Create all vertices
		for (int i = 0; i < noOfNodes; i++) {
			vertices.put((Integer) i, new GrpNode(i));
		}
		while ((lineInput = buffRead.readLine()) != null)   {
			String[] edge =  lineInput.split(" ");
			//every line contains edge between the vertices and it's weight.
			GrpNode vertex1 = vertices.get(new Integer(edge[0])); 
			GrpNode vertex2 = vertices.get(new Integer(edge[1]));
			int weight = Integer.parseInt(edge[2]);
			
			//adding the vertices that have an edge between them into each other's adjacent map.
			vertex1.adjacentVertex(vertex2.getIndex(), weight);
			vertex2.adjacentVertex(vertex1.getIndex(), weight);
			buffRead.readLine(); //read next line.
		}

		//Close the input stream
		buffRead.close();
		
	}	
	
	//assigns source node of the graph as the given integer.
	public void sourceAssign(Integer source) throws Exception{
			
			if(vertices.get(source) != null)
				this.source = vertices.get(source);
			else{
				throw new Exception("source vertex not found!");
			}
		}
	//assigns destination node of the graph as the given integer.
	public void destinationAssign(Integer destination) throws Exception {
			if(vertices.get(destination) != null)
				this.destination = vertices.get(destination);
			else{
				throw new Exception("destination vertex not found!");
			}
		}
	
	//returns the total number of vertices in the graph
		public int totalNodes(){
			return noOfNodes;
		}
		
	//returns the source node of the graph
	public Integer sourceNode() {
			return source.getIndex();
		}

		//retuns the destination node of the graph
		public Integer DestinationNode() {
			return destination.getIndex();
		}
		
	//returns all vertices of the GraphVertex as a map that contains the index of the vertex as key.
	public Map<Integer, GrpNode> getVertices() {
			return vertices;
		}
	
	//returns all the adjacent vertices along with the edge weights for a given node
	public Set<Entry<Integer, Integer>>  adjacentNodeList(Integer vertex){
		return vertices.get(vertex).getadjacents();
	}
	
		
	public void nodeRoute(Integer n){
		vertices.get(n).retrieveRoutingTable().traverse();
		
	}
	
	//add the node, and its next node into the routing table.
	public void RoutingTblAdd(Integer node, String destination, Integer next) throws Exception{
		
		vertices.get(node).insertPairIntoRouting(destination, next);
		
	}
	
	//returns the next node in routing
		public Map.Entry<Integer, String> nextNodeHub(Integer n, String destination){
			
			return vertices.get(n).routingTable.longestPrefixMatch(destination);
		
		}
	
	public String toString(){
		String str = "";
		str += "Source: "+sourceNode()+"\n";
		str += "Destination: "+DestinationNode()+"\n";
		for (Map.Entry<Integer, GrpNode> entry : vertices.entrySet()) {
		    Integer key = entry.getKey();
		    GrpNode value = entry.getValue();
		    str += "GrpNode("+key+"):";
		    for(Map.Entry<Integer, Integer> e: value.getadjacents()){
		    	str += "Ady("+e.getKey()+", "+e.getValue()+"), ";
		    }
		    str += "\n";
		}
		
		return str;
	}

}
