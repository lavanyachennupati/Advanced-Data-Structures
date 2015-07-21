import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


//	Fibonacci Heap Implementation
public class FHeap {
	private Map<Integer, Vertex> vertices; //Map of the vertices and their indexes as keys.
    private Vertex minNode; //Min node of the Fibonacci Heap
    private int totalNodes; //total number of vertices in the fib heap
    
  //Vertex class of Fibonacci Heap.
    private class Vertex {
    	//default initialization of all attributes of the vertex.
        private int     	degree = 0;       
        private boolean 	childCut = false;  
        private Vertex 		next, prev, parent, child;  
        private Integer     index;     
        private int 		nodeValue; 

        //assign given values to a Vertex.
        private Vertex(Integer index, int nodeValue) {
            next = this;
            prev = this;
            this.index = index;
            this.nodeValue = nodeValue;
        }
        //assign index to a given vertex
        public void setIndex(Integer index) {
            this.index = index;
        }
        //returns the index. 
        public Integer getIndex() {
            return index;
        }
        //returns the value of the vertex
        public double getValue() {
            return nodeValue;
        }
 }

    // Attributes of the Heap assigned to default values.
    public FHeap() {
		minNode = null;
		totalNodes = 0;
		vertices = new HashMap<Integer, Vertex>();
	}
    
    //returns true if there are no Vertices in the FibHeap.
    public boolean isEmpty() {
        return minNode == null;
    }
    
    //returns the total number of Nodes.
    public int getTotalNodes() {
        return totalNodes;
    }
    
    //returns the index of the min Vertex of the FibHeap.
    public Integer getMinIndex() {
       
        return minNode.getIndex();
    }

    
    //inserts a new Vertex into the fibonacci with given index and value.
    public void insert(Integer index, int nodeValue) {
    	
    	
        Vertex newNode = new Vertex(index, nodeValue);
        
        vertices.put(index, newNode); //put into the map
       
        
        minNode = join(minNode, newNode); //merge the new Vertex with the minNode of the FibHeap.
        totalNodes += 1; //increment the totalNodes by 1.
        
    }
    
    // deletes by making its value to -infinity and then performs extractMin
    public void delete(Vertex entry) throws Exception{  
        decreaseKey(entry.getIndex(), Integer.MIN_VALUE);
        extractMin();
    }

    //When FHeap is not empty, return and delete the min entry in the FHeap
    public Integer extractMin() {
        
    	if (isEmpty()){
            throw new NoSuchElementException("Heap is empty.");
        }
    	
        totalNodes -= 1;;
        Vertex minNodeCopy = minNode; //temp reference to minNode we will return later
        
        //Only one node in the FibHeap.
        if (minNode.next == minNode) { 
            minNode = null; 
        }
        else {
        	// More than 1 entry, remove minNode and set arbitrary node to minNode.
            minNode.prev.next = minNode.next;
            minNode.next.prev = minNode.prev;
            minNode = minNode.next; // Arbitrary element of the root list to minNode.
        }
        //All children of the minNode removed become individual nodes
        if (minNodeCopy.child != null) {
            
            Vertex minChild = minNodeCopy.child;
            
            do {
            	minChild.parent = null;
            	minChild = minChild.next;
            } while (minChild != minNodeCopy.child);
        }
        
        // Next, splice the children of the root node into the top level list, 
        // then set mMin to point somewhere in that list.
        minNode = join(minNode, minNodeCopy.child);

        
        //pairwise combine. If no elements in the list then min entry is the only element.
        if (minNode == null) return minNodeCopy.getIndex();
        
        List<Vertex> degreeTable = new ArrayList<Vertex>();//roots with different degree
        List<Vertex> toVisit = new ArrayList<Vertex>();//entries in the root list

      //Add all node in the top level list to the toVisit
        for (Vertex v = minNode; toVisit.isEmpty() || toVisit.get(0) != v; v = v.next)
            toVisit.add(v);
        
     // Traverse this list and join.
        for (Vertex v: toVisit) {    
            while (true) { 
            	// Check if an element of degree can be accommodated.
                while (v.degree >= degreeTable.size())
                    degreeTable.add(null);
                
                // if there is no tree with the same degree we visited before, just record this node
                if (degreeTable.get(v.degree) == null) {
                    degreeTable.set(v.degree, v);
                    break;
                }
                // join.
                Vertex n2 = degreeTable.get(v.degree);
                degreeTable.set(v.degree, null);
                
                // smaller root between the 2 trees
                Vertex minNode = (n2.nodeValue < v.nodeValue)? n2 : v;
                Vertex maxNode = (n2.nodeValue < v.nodeValue)? v  : n2;
                
                // Break maxNode out of the root list, then merge it into min's child list.
                maxNode.next.prev = maxNode.prev;
                maxNode.prev.next = maxNode.next;

                // maxNode into a singleton node
                maxNode.next = maxNode;
                maxNode.prev = maxNode;
                minNode.child = join(minNode.child, maxNode);
                
                // Re-parent maxNode.
                maxNode.parent = minNode;
                // maxNode can now lose another child./
                maxNode.childCut = false;
                // Increase min's degree
                minNode.degree += 1;               
                
                //Another pair-wise combine if possible 
                v = minNode;
            }
            // the global min updated
            if (v.nodeValue <= minNode.nodeValue) minNode = v;
        }
        return minNodeCopy.getIndex();
    }

    //joins two circularly double linked lists into one and returns the min-pointer of the joined list.
    private Vertex join(Vertex n1, Vertex n2) {
       
        if (n1 == null && n2 == null) { // Both vertices null means the list is null
            return null;
        }
        else if (n1 != null && n2 == null) { // Two is null, result is one.
            return n1;
        }
        else if (n1 == null && n2 != null) { // One is null, result is two.
            return n2;
        }
        else { //both not null, min-pointer of the resulting join returned.
            Vertex oneNext = n1.next;
            n1.next = n2.next;
            n1.next.prev = n1;
            n2.next = oneNext;
            n2.next.prev = n2;
            
            return n1.nodeValue < n2.nodeValue? n1 : n2;
        }
    }
    //The value of the node is reduced to the given value.
    public void decreaseKey(Integer nodeIndex, int nodeValue) {
    	Vertex v = vertices.get(nodeIndex);
    	
    	// If after decreasing the key it's less than its parent, then cut this node and join to top of list.
    	if(v != null){
    		v.nodeValue = nodeValue;
    	       
            if (v.parent != null && v.nodeValue <= v.parent.nodeValue)
                cutNode(v);
          
            if (v.nodeValue <= minNode.nodeValue)
                minNode = v;    		
    	}
        
    }

    //recursively cuts from it's parents if it's already cut.
    private void cutNode(Vertex v) {
        
        v.childCut = false; //childCut is updated
        
        if (v.parent == null) return; //terminate if no parent
        
        //the node have parent, next if it has siblings, update them
        if (v.next != v) { 
            v.next.prev = v.prev;
            v.prev.next = v.next;
        }
        // If the node is the one identified by its parent as its child,
        // we need to rewrite that pointer to point to some arbitrary other child.
        if (v.parent.child == v) {
        	// If there are any other children, pick one of them arbitrarily.
            if (v.next != v) {
                v.parent.child = v.next;
            }
         // Otherwise, there aren't any other children
            else {
                v.parent.child = null;
            }
        }

       
        v.parent.degree -= 1;//vertice's parent lost a child T_T
        
      //join vertex into the top level list
        v.prev = v.next = v;
        minNode = join(minNode, v);
        
      //recursively check vertice's parent, whether cut it or update ChildCut
        if (v.parent.childCut)
            cutNode(v.parent);
        else
            v.parent.childCut = true;
        
        v.parent = null; //update parent.
    }

}