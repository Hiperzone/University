
#ifdef WIN32_
#include "stdBool.h"
#endif

#ifndef LINKEDLIST_H
#define	LINKEDLIST_H

typedef struct LinkedListNode
{
	struct LinkedListNode *next;
	int index;
	void *element;
}LinkedListNode;

typedef struct LinkedList
{
	LinkedListNode *head;
	LinkedListNode *current;
	int size;
}LinkedList;

LinkedList *list_new();
void list_insert(void *new_element, LinkedList *list);
bool list_remove(void *value, LinkedList *list);
LinkedList *list_destroy(LinkedList *list, bool destroyElements);
bool list_empty(LinkedList *list);
LinkedListNode *Begin(LinkedList *list);
LinkedListNode *Next(LinkedListNode *node);

#endif	/* LINKEDLIST_H */
