The problem in the assignment is an example of a Producer-Consumer problem. The system has several machines (M) that occasionally need to print some text. It also has a small number of printers (N) that can print text given to them. There is, however, more machines wanting to print than printers available for use (M > N).

To enable printing, the system uses a print queue. When a machine has something to print, it creates a print request for the document to be printed, which gets inserted in the print queue. When a printer is free, it retrieves the print document at the head of the queue, prints the required text and removes that item from the queue.

Since resources in any given system are finite, the queues have limits and so is the case in the system. The size of the queue is limited and is equal to the number of printers in our system. This comes as a challenge because if the queue is already full and a machine creates a new print request, by default the new request will be appended to the end of the queue causing the head of the queue to move one position down. This is undesirable as it means information (i.e. the print request at the head of the queue) is lost.

This program addresses this problem and provides a solution to it.
