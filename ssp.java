import java.util.AbstractMap;
import java.util.Map;

public class ssp {
	
	public static void main(String args[]){
		// A graph object is used to represent and use the behavior of the GraphVertex Class.
		GraphVertex grp = new GraphVertex();
		
		//GraphVertex attributes are assigned from the input text file.
		try{
			grp.construct(args[0]);
		}catch(Exception ex){
			System.exit(1);
		}
		
		//Destination Node assigned from the command line.
		try{
			grp.destinationAssign(Integer.parseInt(args[2]));
		}catch(Exception ex){
			System.exit(1);
		}
		
		//The distance of each node from the source vertex is stored in the distMap.
		Map.Entry<Integer[], Integer[]> distMap = dijkstraImplement(grp, args[1]);
		
		Integer[] visited = distMap.getValue();
		Integer parentNode = visited[grp.DestinationNode()]; //check if the destination node is visited.
		String path = grp.DestinationNode() + ""; //path from source node to destination node stored.
		
		//recursively add the parent node.
		while(parentNode != -1){
			path = parentNode + " "+ path;
			parentNode = visited[parentNode];
		}
		
		System.out.println(distMap.getKey()[grp.DestinationNode()]); //distance form source node to destination.
		System.out.print(path); //prints path from source to destination.

		
	}
	
	//Dijkstra implemented using Fibonacci heap.
	public static Map.Entry<Integer[], Integer[]> dijkstraImplement (GraphVertex grp, String source){		
		
		FHeap FibHeap = new FHeap();
		Integer[] lenFromSource = new Integer[grp.totalNodes()]; //stores distance of every node from source.
		Integer[] visited = new Integer[grp.totalNodes()]; //all nodes unvisited from the source initially.
		
		// source node assigned to the graph.
		try{
			grp.sourceAssign(new Integer(source));
		}catch(Exception distMap){
			System.out.println(distMap.getMessage());
			System.exit(1);
		}
		
		
		//All nodes of the graph assigned an initial MAX_VALUE from the source.
		for(int i=0;i<grp.totalNodes();i++){
			Integer valueofVertex = new Integer(i);
			if (!valueofVertex.equals(grp.sourceNode())){
				lenFromSource[i] = Integer.MAX_VALUE;
				
			}else
				lenFromSource[i] = 0;	// Source Node assigned a distance zero from itself.
			visited[i] = -1;
			FibHeap.insert(valueofVertex, lenFromSource[i]); // Insert All these nodes into the Fibonacci Heap.
		}
		
		//Remove the min node from the Fibonacci heap until empty.
		while(!FibHeap.isEmpty()){
			Integer minNode  = FibHeap.extractMin();
			
			//Distance of the adjacent nodes of the min node from the source recalculated.
			for (Map.Entry<Integer, Integer> adjNode: grp.adjacentNodeList(minNode)){
				Integer adjVertex = adjNode.getKey();
			    Integer edgeWeight = adjNode.getValue();
			    int initDistance = lenFromSource[minNode] + edgeWeight;
			    if(initDistance < lenFromSource[adjVertex]){
			    	lenFromSource[adjVertex] = initDistance;
			    	visited[adjVertex] = minNode;
			    	FibHeap.decreaseKey(adjVertex, initDistance); // assigned a new shortest length from the source vertex. 
			    }
			}
			
		}
		// Distance of each vertex from the source along with if it's visited or not returned.
		Map.Entry<Integer[],Integer[]> outputDistance = new AbstractMap.SimpleEntry<Integer[], Integer[]>(lenFromSource, visited);
		return outputDistance;
	}
	
	
	

}
