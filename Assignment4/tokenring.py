import random

# Class represents a critical section request message in the network
class RequestMessage:
    def __init__(self, requester):
        self.requester = requester

# Class represents a critical section grant message in the network
class GrantMessage:
    def __init__(self, next: str, queue: list):
        self.next: str = next
        self.queue: list = queue

# Class represents a node in the network
class Node:
    def __init__(self, name: str):
        self.name: str = name
        self.queue: list = []
        self.holder: bool = False
        self.next: Node = None
        self.exec_critical: bool = False
    
    # Method accepts a request from the network
    def getRequestMessage(self, requesting_node: RequestMessage):
        # If this is the node having the token, add requesting node to queue
        if(self.holder == True):
            self.queue.append(requesting_node)
        # If this is not the node having the token, send it to the next node
        else:
            self.sendRequestMessage(requesting_node)
    
    # Method sends a request message to the network
    def sendRequestMessage(self, request_message: RequestMessage = None):
        # If this is the node forwarding a request message
        if(request_message != None):
            self.next.getRequestMessage(request_message)
        # If this is the node generating a request message
        else:
            message = RequestMessage(self.name)
            self.next.getRequestMessage(message)
        
    # Method accepts a critical section grant message from the network
    def getGrantMessage(self, grant_message: GrantMessage):
        # Forward to next node if it is not meant for this node
        if(grant_message.next != self.name):
            self.sendGrantMessage(grant_message)
        else:
            self.queue = grant_message.queue
            self.holder = True
            self.executeCriticalSection()
    
    # Method sends a critical section grant message to the network
    def sendGrantMessage(self, grant_message: GrantMessage = None):
        # Forward to next node if it is not meant for this node
        if(grant_message != None):
            self.next.getGrantMessage(grant_message)
        # If this is the node giving the grant message
        else:
            next_node = self.queue[0].requester
            self.queue.pop(0)
            message = GrantMessage(next_node, self.queue)
            self.queue = []
            print("Send Grant: " + message.next)
            self.next.getGrantMessage(message)
    
    # Execute critical section of this node
    def executeCriticalSection(self):
        self.exec_critical = True
        print(f"Entered Critical Section of Node {self.name}")
        self.exec_critical = False

        # If there are more requests in the queue, send grant message to the network
        if(len(self.queue) > 0):
            self.sendGrantMessage()


if(__name__ == "__main__"):
    try:
        nos_nodes = int(input("Enter Number of Nodes: "))
        nos_nodes_critical = int(input("Enter Number of Nodes wanting to enter Critical Section: "))
    except:
        print("Invalid Input")
        exit()
    
    # Initialize nodes in the network
    prev_node: Node = None
    nodes: "list[Node]" = []
    for i in range(nos_nodes):
        node_name: str = chr(ord('A') + i)
        new_node = Node(node_name)
        
        if(prev_node != None):
            prev_node.next = new_node
        prev_node = new_node

        nodes.append(new_node)
    nodes[-1].next = nodes[0]
    
    # Randomly generate a starting node
    starting_node = random.randint(0, (nos_nodes - 1))
    nodes[starting_node].holder = True
    nodes[starting_node].exec_critical = True

    # Randomly generate the required number of nodes wanting to enter critical section
    nodes_critical_section: "list[int]" = []
    counter: int = 0
    while(True):
        node = random.randint(0, (nos_nodes - 1))
        if(node != starting_node and node not in nodes_critical_section):
            nodes_critical_section.append(node)
            counter += 1
        if(counter == nos_nodes_critical):
            break
    
    print("Nodes willing to enter Critical Section: ", end = "")
    for node in nodes_critical_section:
        print(chr(ord("A") + node) + " ", end = "")
    print("\n")

    # It is assumed that this set of requesting nodes are already sending their requests while the starting
    # node is in its Critical Section. So, here, we send the required requests to do the same.
    for node in nodes_critical_section:
        nodes[node].sendRequestMessage()

    nodes[starting_node].executeCriticalSection()    
