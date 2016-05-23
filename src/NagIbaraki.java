import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NagIbaraki {
	private static int nodeCount = 5;
	private static int[][] edge;		//edge matrix
	private static int[][] edgeCopy;	//edge matrix for counting critical edges
	private static int[] degree;		//degree of node
	
	/**
	 * Generates graph with the given number of edges*
	 * @param edgeCount number of the edges for the graph
	 */
	public static void generateGraph(int edgeCount){
		degree = new int[nodeCount];
		edge = new int[nodeCount][nodeCount];
		edgeCopy = new int[nodeCount][nodeCount];
		Random random = new Random();
		int node1 = 0;
		int node2 = 0;
		
		List<Integer> unConnectedNodes = new ArrayList<Integer>();
		for(int i=0; i<nodeCount; i++){
			unConnectedNodes.add(i);
		}
		
		//Initialize edge matrix and degree array
		for(int i=0; i<nodeCount; i++){
			for(int j=0; j<nodeCount; j++){
				edge[i][j] = 0;
				edgeCopy[i][j] = 0;
			}
		}
		
		for(int i=0; i<nodeCount; i++){
			degree[i] = 0;
		}
		
		//Adding edges to the graph by choosing 2 nodes randomly everytime
		do{
			for(int i=0; i<edgeCount; i++){
				do{
					node1 = random.nextInt(nodeCount);
					node2 = random.nextInt(nodeCount);
				}while(node1 == node2);
				
				edge[node1][node2] += 1;
				edge[node2][node1] += 1;
				edgeCopy[node1][node2] += 1;
				edgeCopy[node2][node1] += 1;
				degree[node1] += 1;
				degree[node2] += 1;
				
				if(unConnectedNodes.contains(node1)){
					unConnectedNodes.remove(new Integer(node1));
				}
				if(unConnectedNodes.contains(node2)){
					unConnectedNodes.remove(new Integer(node2));
				}
			}
			
		}while(unConnectedNodes.size()>0);				//To make sure the graph is connected.
		
	}
	
	/**
	 * Merges given 2 nodes*
	 * @param node1
	 * @param node2
	 */
	public static void mergeNodes(int node1, int node2){
		int temp1;
		int temp2;
		
		edge[node1][node2] = 0;
		edge[node2][node1] = 0;
		
		for(int i=node1; i<nodeCount; i++){
			if(i!=node2){
				temp1 = edge[node1][i];
				temp2 = edge[node2][i];
				
				edge[node1][i] += edge[node2][i];
				edge[node2][i] += temp1;
			}
		}
	}
	
	/**
	 * Computes min cut recursively using Nagamochi Ibaraki algo*
	 * @param remainingNodeCount number of nodes in the graph
	 * @return final mincut of the graph
	 */
	public static int computeMinCut(int remainingNodeCount){
		if(remainingNodeCount == 2){
			int temp = 0;
			for(int i=0; i<nodeCount; i++){
				if(degree[i]>temp){
					temp = degree[i];
				}
			}
			return temp;
		}
		else{
			
			List<Integer> MAOrder = getMAOrder();
			int minCut = 0;
			int minCutTemp = 0;
			int lastMANode = MAOrder.get(MAOrder.size()-1);
			int lastButOneMANode = MAOrder.get(MAOrder.size()-2);
			
			degree[lastButOneMANode] -= edge[lastButOneMANode][lastMANode];
			degree[lastMANode] = 0;
			
			//Merge the LAST TWO nodes of the MAOrder list
			mergeNodes(lastButOneMANode, lastMANode);
			
			//Compute connectivity/mincut of the new merged graph G(xy)
			if(remainingNodeCount > 2){
				minCutTemp = computeMinCut(remainingNodeCount-1);
				if(minCutTemp < minCut){
					minCut = minCutTemp;
				}
			}
			return minCut;	
		}
	}
	
	/**
	 * @return Maximal Adjacency Ordering list of the graph
	 */
	public static List<Integer> getMAOrder(){
		int startNode = new Random().nextInt(nodeCount);
		List<Integer> MAOrderList = new ArrayList<Integer>();
		int[] MAOrderDegree = new int[nodeCount];
		int maxDegree = 0; //Degree of the MAOrdering List i.e maxdegree from outer nodes to MAOrdering List
		int maxDegreeNode = 0;
		int remainingNodes = nodeCount;
		
		MAOrderList.add(new Integer(startNode));
		MAOrderDegree[startNode] = -1;
		remainingNodes--;
	
		int lastMANode = 0;
		
		do{
			lastMANode = MAOrderList.get(MAOrderList.size()-1);
			maxDegree = 0;
			
			for(int i=0; i<MAOrderDegree.length; i++){
				if(MAOrderDegree[i]!=-1){
					MAOrderDegree[i] += edge[lastMANode][i];
					
					if(edge[lastMANode][i] > maxDegree){
						maxDegree = edge[lastMANode][i];
						maxDegreeNode = i;
					}
				}
			}
			
			MAOrderList.add(new Integer(maxDegreeNode));
			MAOrderDegree[maxDegreeNode] = -1;
			remainingNodes--;
		}while(remainingNodes>1);	//to check if there are atleast 2 nodes 
		return MAOrderList;
	}
	
	/**Computes the number of Critical edges by 
	 * removing one edge at a time from the graph
	 * @returns number of critical edges 
	 * @param originalMinCut mincut of the original graph 
	 **
	 */
	public static int countCriticalEdges(int originalMinCut){
		int[][] edgeCritical = new int[nodeCount][nodeCount];
		int criticalEdgeCount = 0;
		
		for(int i=0; i<nodeCount; i++){
			for(int j=0; j<nodeCount; j++){
				edge[i][j] = edgeCopy[i][j];
			}
		}
		
		for(int i=0; i<nodeCount; i++){
			for(int j=0; j<nodeCount; j++){
				if(edge[i][j] > 0 && edgeCritical[i][j] != 1){
					edge[i][j] = 0;
					edge[j][i] = 0;
					
					//If the new mincut is smaller than the orignal mincut
					//count the edge as a critical edge
					if(computeMinCut(nodeCount)<originalMinCut){
						criticalEdgeCount++;
					}
				}
				for(int m=0; m<nodeCount; m++){
					for(int n=0; n<nodeCount; n++){
						edge[m][n] = edgeCopy[m][n];
					}
				}
				edgeCritical[i][j] = 1;
			}
		}
		return criticalEdgeCount;
	}
	
	public static void main(String args[]){
		int originalMinCut;
		int criticalEdgeCount;
		int avgDegree = 0;
		
		System.out.println("Edges  AverageDegree  Connectivity  CriticalEdgeCount");
		for(int edgeCount=40; edgeCount<=400; edgeCount=edgeCount+5){
			NagIbaraki obj = new NagIbaraki();
			obj.generateGraph(edgeCount);
			originalMinCut = computeMinCut(nodeCount);
			criticalEdgeCount = countCriticalEdges(originalMinCut);
			avgDegree = (2*edgeCount)/nodeCount;
			System.out.println(" "+ edgeCount + "  " + avgDegree + "  " + originalMinCut + "  " + criticalEdgeCount);
		}
	}
}
