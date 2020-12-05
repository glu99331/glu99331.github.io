#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/**************************************************
 * Author: Gordon Lu                              *
 *                                                *
 * Description: Linked Lists and the Heap         *
 * - Creating a linked list via malloc.           *
 *                                                *
**************************************************/

//TODO: Create a Node struct for the Linked List.

Node* create_node(int value)
{
    //TODO: Fill in the code to create a node.
    return NULL;
}
void list_print(Node* head)
{
    //TODO: Fill in the code to print out all of the elements of a Linked List.
}
Node* list_append(Node* head, int value)
{
    //TODO: Fill in the code to append a new node into a Linked List.
    return NULL;
}

Node* list_prepend(Node* head, int value)
{
    //TODO: Fill in the code to append a new head in a Linked List.
}

void list_free(Node* head)
{
    //TODO: Fill in the code to free all elements in a Linked List.
}

Node* list_remove(Node* head, int value)
{
    //TODO: Fill in the code to remove a Node from a Linked List.
    return NULL;
}


int main() {
	// The comments at the ends of the lines show what list_print should output.
	Node* head = create_node(1);
	list_print(head);                  // 1
	Node* end = list_append(head, 2);
	list_print(head);                  // 1 -> 2
	end->next = create_node(3);
	list_print(head);                  // 1 -> 2 -> 3
	head = list_prepend(head, 0);
	list_print(head);                  // 0 -> 1 -> 2 -> 3
	list_append(head, 4);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4
	list_append(head, 5);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4 -> 5

	head = list_remove(head, 5);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4
	head = list_remove(head, 3);
	list_print(head);                  // 0 -> 1 -> 2 -> 4
	head = list_remove(head, 0);
	list_print(head);                  // 1 -> 2 -> 4

    list_free(head);
	return 0;
}