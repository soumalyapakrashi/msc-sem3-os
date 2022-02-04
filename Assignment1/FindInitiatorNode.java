import java.util.Scanner;
import java.util.Stack;
class FindInitiatorNode
{
    int no_vertex, adjacency_matrix[][], visited[];
    Stack<Integer> stack;

    public FindInitiatorNode(int n)
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
                System.out.print(adjacency_matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void FindInitiator()
    {
        int i,j,k;
        
        for(k=0;k<no_vertex;k++)
        {
            for(i=0;i<no_vertex;i++)
            {
                visited[i]=0;
            }
            i=k;
            stack.push(i);
            visited[i]=1;
            
            Integer removed = (Integer) stack.pop();
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
                if(this.checkVisitedArray()==true)
                {
                    System.out.print("V"+(k+1)+" ");
                    break;
                }
                if(stack.isEmpty()==true)
                {
                   break;
                }  
                removed=(Integer) stack.pop();
                i=removed; 
            }
        }
    }

    public boolean checkVisitedArray()
    {
    
        for(int i=0;i<no_vertex;i++)
        {
            if(visited[i]==0)
            {
                return false;
            }
        }
        return true;
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
        FindInitiatorNode obj = new  FindInitiatorNode(n);

        obj.InputAdjacencyMatrix();
        obj.DisplayAdjacencyMatrix();

        System.out.print("\nThe possible initiator nodes are/is : ");
        obj.FindInitiator();
        scanner.close();
    }
}