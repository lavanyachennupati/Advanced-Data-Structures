import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class routing {
	

	public static void main(String args[]){
		
		GraphVertex 					grp = new GraphVertex(); 
		Map<Integer, String>	ipMap = new HashMap<Integer, String>();
		try{
			grp.construct(args[0]); //file used to construct the graph is passed
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		
		try{
			ipMap = retrieveIps(args[1]); // file containing all the ipMap of the nodes passed to get ipMap
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			System.exit(1);
		}
		int source = Integer.parseInt(args[2]); //source vertex passed as an argument from command line
		int destination = Integer.parseInt(args[3]);//destination vertex passed as an argument from command line
		int minimumDistance = -1;

		//consider every vertex as a source and build RoutingTable using Tries.
		//Part-1 of the project used here for finding the previous vertex and distance from source to destination
		for(int i=0;i<grp.totalNodes();i++){	
			Map.Entry<Integer[], Integer[]> sspMap = ssp.dijkstraImplement(grp, i+""); 
			Integer[] sspDist = sspMap.getKey(); //contains the distance of nodes from source
			Integer[] parent = sspMap.getValue();//contains the parent vertex
 			for(int j=0;j<grp.totalNodes();j++){	
	 			if(i != j){	
	 				Integer index = j;
	 				//iterated till source-vertex is the parent.
	 				while(!parent[index].equals(grp.sourceNode())){
	 					index = parent[index];
	 				}
	 				try{
	 					grp.RoutingTblAdd(i, ipMap.get(j), index); //added into the routing table which uses tries
	 				}catch(Exception ex){
	 					ex.printStackTrace();
	 					System.exit(1);
	 				}
	 			}
	 			//shortest length between source and destination from ssp
	 			if(i ==  source && j == destination){
	 				minimumDistance = sspDist[destination];
	 			}
			}
 			grp.nodeRoute(i); 
		}
		
		//source to destination packet routing simulated
		System.out.println(minimumDistance);
		String route = "";
		Integer vertex = source;
		int flag=0;
		//iterated till the destination vertex is the next-hub
		while(vertex != destination){
			Map.Entry<Integer, String> sspMap  = grp.nextNodeHub(vertex, ipMap.get(destination)); //the ipAdr address of the nextHub is being stored.
			if(flag == 0)
			{System.out.print(sspMap.getValue());
			flag=1;}
			else
				System.out.print(" "+sspMap.getValue());
			vertex = sspMap.getKey(); //vertex Index of next-hub 
		}
		
	}
	 public static Map<Integer, String> retrieveIps(String pathFollowed) throws Exception{
	        
	        FileInputStream fIstream = new FileInputStream(pathFollowed);
	        BufferedReader br = new BufferedReader(new InputStreamReader(fIstream));
	        Map<Integer, String> ipValues = new HashMap<Integer, String>(); //creating hashmap of IP Values
	        String line;
	    
	        int l = 0;
	        while ((line = br.readLine()) != null)   { //read each line from buffer reader and then convert this IPAddress to binary format
	            String binaryIP = zeroAppendIp(BinaryOfIp(line));
	            ipValues.put(l, binaryIP); //put these binary values in the map
	            l += 1;    
	            br.readLine();
	        }
	        //Closing the input stream
	        br.close();
	        return ipValues; //return the map containg the IP Address values
	    }
	
	//appending sufficient zeros to make it a valid ipAdr.
	public static String zeroAppendIp(String ipAdr){
		while(ipAdr.length() < 32){
			ipAdr = "0"+ipAdr;
		}

		return ipAdr;
	}
	
	//converts ipAdr to binary.
	public static String BinaryOfIp(String ip) throws Exception{
		InetAddress ipAdr = InetAddress.getByName(ip);
		byte[] bytes = ipAdr.getAddress();	
		String output_string = new BigInteger(1, bytes).toString(2);
		return output_string;
	}
	
	
}
