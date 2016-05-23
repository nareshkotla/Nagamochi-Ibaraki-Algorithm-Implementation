Implementation of Nagamochi Ibaraki Algorithm for finding minimum cut in an undirected graph
--------------------------------------------------------------------------------------------
The objective of this project is to implement Nagamochi-Ibaraki algorithm for finding a minimum cut in an undirected graph, and experiment with it.

The program has been run for 20 nodes and different number of edges varying between 40 and 400, increasing in steps of 5. Each time the edges are added to the graph by randomly choosing 2 nodes from the graph.

Nagamochi-Ibaraki algorithm has been applied over the edge matrix to calculate the connectivity. Then the total number of critical edges are computed by deleting an edge each time and counting the edge deleted as the critical edge if newly obtained connectivity is less than the original connectivity of the graph.



Platform/Compiler: Windows, javac
------------------


Files:
------
NagIbaraki.java - The main program of the project from where the execution starts.


Compilation:
------------
- Save the code copied in the report as "NagIbaraki.java"
- Navigate to the folder through the command prompt.
- Type the below command for compiling all the java files in the folder.

javac *.java


Run:
----
- Type the below command

java NagIbaraki


Output:
-------
For each value of edgeCount 
- Prints Edges, Average Degree, Connectivity, Critical Edge Count