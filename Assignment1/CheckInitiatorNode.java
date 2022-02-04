import java.util.Scanner;
import java.util.Stack;
class CheckInitiatorNode
{
    int no_vertex, adjacency_matrix[][], visited[];
    Stack<Integer> stack;

    public CheckInitiatorNode(int n)
    {
        no_vertex=n;
        adjacency_matrix=new int[no_vertex][no_vertex];
        visited=new int[no_vertex];
        stack = new Stack<Integer>();
    }

    public void InputAdjacencyMatrix()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Adjacency Matrix:");
        int i,j,input;
        for(i=0;i<no_vertex;i++)
        {
            for(j=0;j<no_vertex;j++)
            {
                // Input should either be 0 indicating no edge or 1 indicating existence of an edge
                System.out.print("Enter the directed edge (0/1) between vertices V"+(i+1)+" and V"+(j+1)+" : ");
                input=scanner.nextInt();
                if(input!=1 && input!=0)
                {
                    System.out.println("Invalid Input!");
                    System.exit(0);
                }
                adjacency_matrix[i][j]=input;
            }
        }
    }

    public void DisplayAdjacencyMatrix()
    {
        System.out.println("The given Adjacency Matrix:");
        int i,j;
        for(i=0;i<no_vertex;i++)
        {
            for(j=0;j<no_vertex;j++)
            {
                System.out.print(adjacency_matrix[i][j]+"  ");
            }
            System.out.println();
        }
    }

    public void CheckInitiator()
    {
        Scanner scanner = new Scanner(System.in);
        int node,i,j;
        // Input should be V1/V2/... in this format
        System.out.print("\nEnter the node to be checked (V1/V2/..): ");
        String n=scanner.nextLine();
        node=Character.getNumericValue(n.charAt(1));
        if(node>no_vertex)     
        {
            System.out.println("Invalid Input!");
            System.exit(0);
        }  
        
        stack.push(node-1);
        visited[node-1]=1;
        Integer removed = (Integer) stack.pop();
        i=removed;
        while(true)
        {
            for(j=0;j<no_vertex;j++)
            {
                if(adjacency_matrix[i][j]==1 && visited[j]==0)
                {
                    stack.push(j);
                    visited[j]=1;
                }
            }
            //If the stack is empty then we have to check whether the all the elements in the visited array are 1
            if(stack.isEmpty()==true) 
                break;
            removed=(Integer) stack.pop();
            i=removed;
        }
        for(i=0;i<no_vertex;i++)
        {
            // If all the elements in the visited array are 1 then that corresponding node is an initiator node, otherwise not
            if(visited[i]==0)
            {
                System.out.println("The given node V"+node+" cannot be an initiator node");
                return;
            }
        }
        System.out.println("The given node V"+node+" can be an initiator node");
    }

    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the number of vertices: ");
        int n=scanner.nextInt();
        if(n<1)
        {
            System.out.println("Invalid Input!");
            scanner.close();
            return;
        }
        CheckInitiatorNode obj = new CheckInitiatorNode(n);

        obj.InputAdjacencyMatrix();
        obj.DisplayAdjacencyMatrix();
        obj.CheckInitiator();
        scanner.close();
    }
}