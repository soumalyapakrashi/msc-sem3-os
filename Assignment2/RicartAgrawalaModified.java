// To implement Ricart Agrawala Symmetric Algorithm
import java.util.Scanner;
import java.util.Random;

class RicartAgrawalaModified
{
    int no_of_nodes, timestamp[][];
    // timestamp is a matrix of size 2xN where the ith column of the second row is the node having a timestamp stored in the ith column of the first row
    // 0 values in the first row of the timestamp indicates that the corresponding node is not interested in entering into the critical section

    public RicartAgrawalaModified(int n)
    {
        no_of_nodes=n;
        timestamp=new int [2][no_of_nodes];
        for(int i=0;i<no_of_nodes;i++)
        {
            timestamp[1][i]=i+1;
        }
    }

    public void getRequest()
    {
        Scanner scanner=new Scanner(System.in);
        Random rand = new Random();
        int i,val,node_in;
        char ans;
        
        // Generate a random node which will be in its critical section from the beginning
        int rand_node = rand.nextInt(no_of_nodes);
        
        // Randomly generate its timestamp
        int rand_timestamp = rand.nextInt(5);
        if(rand_timestamp==0)
            rand_timestamp++;
        timestamp[1][rand_node] = rand_node+1;
        timestamp[0][rand_node] = rand_timestamp;

        for(i=0;i<no_of_nodes;i++)
        {
            System.out.println(timestamp[1][i]+"\t"+timestamp[0][i]);
        }

        while(true)
        {
            // Input should either be Y if the respective node wants to enter into critical section or N if the repective node does not want to enter into critical scetion
            System.out.print("\n Does any node want to enter into Critical Section (Y/N) ? ");
            ans=scanner.next().charAt(0);
            if(ans=='y' || ans=='Y')
            {
                //The node numbers range from 1 to n, where n is given by the user
                System.out.print("\nEnter the node which wants to enter into the Critical Section : ");
                node_in=scanner.nextInt();
                if(node_in>no_of_nodes)
                {
                    System.out.println("Invalid Input!");
                    scanner.close();
                    System.exit(0);
                }
                
                System.out.print("\nEnter the timestamp of the node"+node_in+" :");
                val=scanner.nextInt();
                if(val<1)
                {
                    System.out.println("Invalid Input!");
                    scanner.close();
                    System.exit(0);
                }
                for(i=0;i<no_of_nodes;i++)
                {
                    if(node_in==timestamp[1][i])
                    {
                        //If the timestamp of a particular node is greater than 1 then that node has requested to enter into CS. Hence further request is not allowed.
                        //If the timestamp is negative it means that, that particular node had requested for CS before which has been granted. Hence, the later timestamp will have to be greater than the previous one.

                        if(timestamp[0][i]>0 || (timestamp[0][i]!=0 && Math.abs(timestamp[0][i])>val) )
                        {
                            System.out.println("Invalid Input!");
                            scanner.close();
                            System.exit(0);
                        }
                        timestamp[0][i]=val;
                        
                    }
                }
                
            }
            else if(ans=='n' || ans=='N')
            {
                System.out.print("\n Do you want to exit from the program (Y/N) ? ");
                ans=scanner.next().charAt(0);
                if(ans=='y' || ans=='Y')
                {
                    while(true)
                    {
                        boolean res = giveResponse();
                        if(res == false)
                        {
                            scanner.close();
                            System.out.println("\n\nThank You!");
                            System.exit(0);
                        }
                    }
                    
                }
                else if(ans!='n' && ans!='N' && ans!='y' && ans!='Y') 
                {
                    System.out.println("Invalid Input!");
                    scanner.close();
                    System.exit(0);
                }
                this.giveResponse();
            }
            else
            {
                System.out.println("Invalid Input!");
                scanner.close();
                System.exit(0);
            }
        }
    }

    public boolean giveResponse()
    {
        int i,j,temp1,temp2;
        
        for (i = 0; i < no_of_nodes-1; i++)
        {
            for (j = 0; j < no_of_nodes-i-1; j++)
            {
                if (timestamp[0][j] > timestamp[0][j+1])
                {
                    // The matrix timestamp is sorted with respect to the first row.
                    temp1 = timestamp[0][j];
                    temp2 = timestamp[1][j];
                    timestamp[0][j] = timestamp[0][j+1];
                    timestamp[1][j] = timestamp[1][j+1];
                    timestamp[0][j+1] = temp1;
                    timestamp[1][j+1] = temp2;
                }
                else if(timestamp[0][j] == timestamp[0][j+1])
                {
                    if(timestamp[1][j]>timestamp[1][j+1])
                    {
                        temp1 = timestamp[0][j];
                        temp2 = timestamp[1][j];
                        timestamp[0][j] = timestamp[0][j+1];
                        timestamp[1][j] = timestamp[1][j+1];
                        timestamp[0][j+1] = temp1;
                        timestamp[1][j+1] = temp2;
                    }
                }
            }
        }

        i=0;
        while(i<no_of_nodes)
        {
            // To check whether the node wants to enter into Critical Section or not
            if(timestamp[0][i]<=0)
            {
                
                if(i==no_of_nodes-1) 
                {
                    System.out.print("No nodes are waiting to enter into Critical Section");
                    return false;
                }
                i++;
            }
            else
            {
                System.out.println("\nThe node"+timestamp[1][i]+" enters into the Critical Section having a timestamp of "+timestamp[0][i]);
                System.out.print("The list of node/nodes which is/are waiting to enter into Critical Section are: ");
                //The last node in the queue has entered into Critical Section, hence no node are waiting to enter into CS
                if(i==no_of_nodes-1)
                     System.out.print("None");
                else
                {
                    for(j=i+1;j<no_of_nodes;j++)
                    {
                        System.out.print("Node"+timestamp[1][j]+"\t");
                    }
                }
                System.out.println("\nThe node"+timestamp[1][i]+" comes out of the Critical Section");
                timestamp[0][i]*=-1;
                //After a node comes out of CS, the timestamp of that particular node is updated with a negative sign.
                return true;
            }
        } 
        return true;       
    }

    public static void main(String args[])
    {
        Scanner scanner=new Scanner(System.in);
        int n;   
        System.out.print("\nEnter the number of nodes : ");
        n=scanner.nextInt();
        if(n<2)
        {
            System.out.println("Invalid Input!");
            scanner.close();
            return;
        }
       
        RicartAgrawalaModified obj = new RicartAgrawalaModified(n);

        obj.getRequest();

        scanner.close();
    }
}