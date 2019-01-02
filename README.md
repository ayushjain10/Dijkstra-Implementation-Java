# Dijkstra-Implementation-Java
Implemented Dijkstra's algorithm for Single Source Shortest path using Java

Dijkstra Algorithm is one of the popular algorithm to find the shortest path between a source node and the destination. 

The time complexity of the algorithm is O(|E| + |V|logV).

A custom comparator is created which is responsible for placing the node object at the correct position in the queue.

Algorithm :

Select the least cost node object from the queue and traverse through it's adjacency list. For each of those nodes, perform Relax operation. Relax operation will compare the current path cost with the newly found path cost and assign the one with low cost. Accordingly, update the parent pointer. 

The algorithm runs untill queue has objects. Once, all the node objects has been relaxed, the path cost at the destination has the lowest cost with correct parent pointer.

The main logic of this algorithm lies in the Relax operation.

Special Scenarios :

  1) Handled the scenarios when multiple paths has the same cost, then select the path with fewer nodes.
  
  2) Handled the scenarios when multiple path with same cost and same number of nodes, then select the node which comes            lexicographically first.
  
