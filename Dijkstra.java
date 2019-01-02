import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	public static class CustomObject {
		int vertex;
		int pathCost;
		CustomObject(int v, int p) {
			vertex = v;
			pathCost = p;
		}
	}
	public static class QueueObject {
		int cost;
		int vertex;
		int count;
		QueueObject(int v, int c, int cnt) {
			cost = c;
			vertex = v;
			count = cnt;
		}
	}
	public static class MyGraph {
		int vertex;
		LinkedList<CustomObject> adjList[];
		@SuppressWarnings("unchecked")
		MyGraph(int vertex) {
			this.vertex = vertex;
			adjList = new LinkedList[vertex];
			//For all the vertex allocate a linked list
			for(int i=0;i<vertex;i++) {
				adjList[i] = new LinkedList<>();
			}
		}
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int nVertices = sc.nextInt();
		int nEdges = sc.nextInt();
		sc.nextLine();
		MyGraph graph = new MyGraph(nVertices);
		//Create the Adjacency list based on the input
		for(int i=0;i<nEdges;i++) {
			String edge = sc.nextLine();
			String[] entries = edge.split(" ");
			int source = Integer.parseInt(entries[0]);
			int dest = Integer.parseInt(entries[1]);
			int pCost = Integer.parseInt(entries[2]);
			//Since it's an undirected graph add entry in adjList from source to dest and dest to source
			graph.adjList[source].add(new CustomObject(dest,pCost));
			graph.adjList[dest].add(new CustomObject(source,pCost));
		}
		int source = sc.nextInt();
		int dest = sc.nextInt();
		implementDijkstra(graph, nVertices, source, dest);
		sc.close();
	}
	
	public static class CustomComparator implements Comparator<QueueObject> {
		//Comparator logic explained in the assignment
		@Override
		public int compare(QueueObject o1, QueueObject o2) {
			if(o1.cost == o2.cost) {
				if(o1.count == o2.count)
					return (o1.vertex>o2.vertex)?1:-1;
				else
					return (o1.count>o2.count)?1:-1;
			}
			return (o1.cost>o2.cost)?1:-1;
		}
		
	}
	
	private static void implementDijkstra(MyGraph graph, int nVertices, int source, int dest) {
		PriorityQueue<QueueObject> queue = new PriorityQueue<QueueObject>(nVertices,new CustomComparator());
		//Initialize distance array & predecessor array
		int distance[] = new int[nVertices];
		int predecessor[] = new int[nVertices];
		
		for(int i=0;i<nVertices;i++) 
			distance[i] = Integer.MAX_VALUE;
		for(int i=0;i<nVertices;i++)
			predecessor[i] = -1;
		distance[source] = 0;
		List<Integer> exploredNodes = new ArrayList<Integer>();
		//Add objects in Queue
		for(int i=0;i<nVertices;i++) {
			queue.add(new QueueObject(i, distance[i], 0));
		}
		int count[] = new int[nVertices];
		for(int i=0;i<nVertices;i++) 
			count[i] = 0;
		while(!queue.isEmpty()) {
			int u = queue.poll().vertex;
			exploredNodes.add(u);
			for(CustomObject c : graph.adjList[u]) {
				int v = c.vertex;
				if(!exploredNodes.contains(v)) {
					//If distance is same, means multiple shortest paths
					//Now check the number of vertices and select the one with less number of vertices
					//If the number of vertices is also same, then select the path which is lexicographically small.
					if(distance[u]+c.pathCost==distance[v]) {
						if(count[u]+1 < count[v]) {
							predecessor[v] = u;
						}
						if(count[u]+1 == count[v]) {
							
							//Logic to check the path from source to the node and compare the lexicographic order.
							//Add the path in the list and then add it in string buffer. If the element is a single digit then pad it with 1 zero in the starting. 
							//Now, we get both the paths. We can now check the lexicographic order of both the paths and then compare the. Select the one which comes first.
							StringBuffer s1= new StringBuffer();
							StringBuffer s2= new StringBuffer();
							List<Integer> l1 = new ArrayList<Integer>();
							List<Integer> l2 = new ArrayList<Integer>();
							l2.add(u);
							int u1=u,v1=v;
							while(v1!=source) {
								l1.add(predecessor[v1]);
								v1 = predecessor[v1];
							}
							while(u1!=source) {
								l2.add(predecessor[u1]);
								u1 = predecessor[u1];
							}
							Collections.reverse(l1);
							Collections.reverse(l2);
							for(Integer i : l1) {
								if(i<10) {
									s1.append(String.format("%02d", i));
								}
								else {
									s1.append(i);
								}
								
							}
							for(Integer i : l2) {
								if(i<10) {
									s2.append(String.format("%02d", i));
								}
								else {
									s2.append(i);
								}
								
							}
							List<List<Integer>> l  = new ArrayList<List<Integer>>();
							List<Integer> a = new ArrayList<Integer>();
							if(!l.contains(a)) {
								
							}
							if(Integer.parseInt(s1.toString()) > Integer.parseInt(s2.toString())) {
								predecessor[v] = u;
								count[v] = 1+count[u];
							}
							/*if(u < v) {
								predecessor[v] = u;
								count[v] = 1+count[u];
							}*/
						}
					}
					//Perform relax operation
					 if(distance[u]+c.pathCost<distance[v]) {
						 count[v] = 1+count[u];
						for(CustomObject cObject : graph.adjList[v]) {
							if(exploredNodes.contains(cObject.vertex) && cObject.pathCost == (distance[u]+c.pathCost)) {
								
								distance[v] = distance[u]+c.pathCost;
								predecessor[v] = cObject.vertex;
								break;
							}
							else {
								
								distance[v] = distance[u]+c.pathCost;
								predecessor[v] = u;
								break;
							}
						}
						//Remove the object from queue and add it again to reflect the changes distance
						//The object will now be added on different position in queue based on the distance value.
						QueueObject toRemove = new QueueObject(0,0,0);
						for(QueueObject q : queue) {
							if(q.vertex == v) {
								toRemove = q;
								break;
							}
						}
						queue.remove(toRemove);
						queue.add(new QueueObject(v,distance[v], count[v]));
					}
				}
			}
		}
		/*for(int i=0;i<nVertices;i++) 
			System.out.println("vertex:"+i+",count:"+count[i]);*/
		List<Integer> path = new ArrayList<Integer>();
		//Print path by adding the node initially to destination and backtrack to it's predecessor until we get source, reverse the list and that will be the path.
		int node = dest;
		while(node!=source) {
			path.add(node);
			node = predecessor[node];
		}
		path.add(source);
		Collections.reverse(path);
		System.out.println(distance[dest]);
		int n = path.size();
		for(int i=0;i<n;i++) {
			if(i==n-1) 
				System.out.print(path.get(i));
			else
				System.out.print(path.get(i)+" ");
		}
		System.out.println();
	}
	
}
