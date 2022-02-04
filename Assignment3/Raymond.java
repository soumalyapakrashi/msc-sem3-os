/* It is assumed that the network which is given by the user is connected. */

import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

class Raymond
{
    // The total number of nodes in the network is stored in no_of_nodes
    // Each node is pointing to another node (parent) which is stored in holder
    // Each node is assigned a node number. (Eg : Node P1 has a node_number of 1, Node P2 has a node_number of 2, etc)
    int no_of_nodes, holder, node_number;
    // Each node is assigned a queue.
    Queue<Integer> q;

    public Raymond(int n)
    {
        this.no_of_nodes = n;
        this.q = new LinkedList<Integer>();
        this.holder = 0;
        this.node_number = 0;
    }

    public void inputHolder(int node)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the holder of the node P"+node+" : ");
        this.holder=scanner.nextInt();
    }

    public void inputNodeNumber(int node_number)
    {
        this.node_number = node_number;
    }

    public void displayHolder()
    {
        System.out.print(this.holder);
    }

    public static void displayQueue(Raymond obj[], int no_of_nodes)
    {
        System.out.println("The contents of the queue of each node are : ");
        for(int i=0; i<no_of_nodes; i++)
        {
            System.out.println("Node P"+(i+1)+" : "+obj[i].q);
        }
    }

    public void getRequest(Raymond obj[], int node_in)
    {
        //The request of entering into the CS has been informed to the node which is currently in CS
        if(this.node_number == this.holder)
        {
            this.q.add(node_in);
            return;
        }

        //This node is not currently in CS. However a message has been sent to the priviledged node before as it is seen that the respective queue is non empty.
        else if(this.q.size() != 0)
        {
            this.q.add(node_in);
            return;
        }

        // This node is not currently in CS. A message will have to be transfered to the holder node as it is seen that the respective queue is empty.
        else 
        {
            this.q.add(node_in);
            obj[this.holder-1].getRequest(obj, this.node_number);            
        }      
    }

    public void givePriviledge(Raymond obj[])
    {
        int node;
        node = this.q.remove();

        //This node will be given the priviledge of entering into the CS.
        if(node == this.node_number)
        {
            System.out.println("\nThe node P"+node+" enters into the Critical Section");
            displayQueue(obj, no_of_nodes);
            System.out.println("The node P"+node+" comes out of the Critical Section");
            
            //All the nodes wanting to enter into CS have been given the priviledge.
            if(obj[node-1].q.size() == 0)
                return;

            //Some nodes are waiting to enter into CS.
            else
                obj[node-1].givePriviledge(obj);
        }

        //As the queue of the respective node is non-empty, a message/request will have to be send to the next node.
        else if(this.q.size() !=0 )
        {
            obj[node-1].q.add(this.node_number);
            obj[node-1].givePriviledge(obj);
        }

        //As the queue of the respective node is empty, no message/request will have to be send to the next node.
        else
        {
            obj[node-1].givePriviledge(obj);
        }
    }

    public static void main(String args[])
    {
        int n,i,node_in_cs=0,cs_counter=0;
        Raymond []obj;
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the number of nodes : ");
        n=scanner.nextInt();
        // Creating an array on n objects where each object represents each node.
        obj = new Raymond[n]; 
        for(i=0;i<n;i++)
        {
            obj[i] = new Raymond(n);
        }

        // To input the holders of each node which is given by the user and to generate the node number for each node. 
        for(i=0;i<n;i++)
        {
            obj[i].inputHolder(i+1);
            obj[i].inputNodeNumber(i+1);
        }

        //To check which node is in Critical Section.
        for(i=0; i<n; i++)
        {
            if(obj[i].holder == obj[i].node_number)
            {
                node_in_cs = i;
                cs_counter++;
                break;
            }
        }

        if(cs_counter != 1)
        {
            System.out.println("The given network is invalid as no node is in Critical Section!");
            scanner.close();
            return;
        }

        //To display the holders of each node
        for(i=0;i<n;i++)
        {
            System.out.print("\nThe holder of node P"+(i+1)+" is ");
            obj[i].displayHolder();
        }
        
        Random rand = new Random();
        int rand_cs=0, node_in, no_of_inputs=0, rand_node;     
        char ans;
        Set<Integer> hs = new HashSet<Integer>();

        System.out.print("\n\nEnter the number of nodes which wants to enter into Critical Section : ");
        no_of_inputs = scanner.nextInt();
        
        // Since one node is already in CS, the maximum number of nodes wanting to enter into CS can be n-1
        if(no_of_inputs>n-1 || no_of_inputs<1)
        {
            System.out.println("Invalid Input!");
            scanner.close();
            System.exit(0);
        }
        
        // Since a set stores unique values, we will generate nodes wanting to enter into CS, till the size of the hashset becomes the size of the no of inputs
        while(hs.size()!=no_of_inputs)
        {
            rand_node=0;
            // The node number can neither be 0 nor the node which is already in CS.
            
            while(rand_node==0 || rand_node==node_in_cs+1)
            {
                rand_node = rand.nextInt(n);
            }
            hs.add(rand_node);
        }

        // Each node from the set requests for CS using the getRequest() funtion.
        System.out.print("The nodes which are wanting to enter into the Critical Section are : ");
        for (int node : hs)
        {
            System.out.print("P"+node+" ");
            obj[node-1].getRequest(obj, node);
        }
        System.out.println();
        System.out.println("The node currently in Critical Section is P"+obj[node_in_cs].node_number);
        displayQueue(obj,n);
        System.out.println("The node P"+obj[node_in_cs].node_number+" comes out of the Critical Section");
        //Each node wanting to be in CS is given priviledge using the function givePriviledge()
        obj[node_in_cs].givePriviledge(obj);
    }
}