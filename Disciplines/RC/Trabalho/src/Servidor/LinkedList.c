#include "../shared/common.h"
#include "LinkedList.h"
#include <stdio.h>
#include <malloc.h>
#include <assert.h>
#include <string.h>

/************************************************************************/
/* Cria uma nova lista                                                  */
/************************************************************************/
LinkedList *list_new()
{
	LinkedList *list = malloc(sizeof(LinkedList));

	list->head = malloc(sizeof(LinkedListNode));
	list->head->element = NULL;
	list->head->next = NULL;

	list->current = list->head;
	list->size = 0;
	return list;
}

/************************************************************************/
/* Verifica se a lista esta vazia                                       */
/************************************************************************/
bool list_empty(LinkedList *list)
{
	assert(list->head != NULL);
	return (list->head->next == NULL);
}

/************************************************************************/
/* Insere um elemento na lista                                          */
/************************************************************************/
void list_insert(void *new_element, LinkedList *list)
{
	assert(list->head != NULL && new_element != NULL);
	LinkedListNode *new_NodeLinkedList = malloc(sizeof(LinkedListNode));

	new_NodeLinkedList->element = new_element;
	new_NodeLinkedList->next = NULL;

	list->size++;

	list->current->next = new_NodeLinkedList;
	list->current = new_NodeLinkedList;
}

/************************************************************************/
/* Remove um elemento da lista                                          */
/************************************************************************/
bool list_remove(void *value, LinkedList *list)
{
	assert(value != NULL && list->head != NULL);

	LinkedListNode *current = list->head->next;
	LinkedListNode *prev_NodeLinkedList = list->head;

	while (current != NULL && current->element != value)
	{
		prev_NodeLinkedList = prev_NodeLinkedList->next;
		current = current->next;
	}

	if (current && current->element == value)
	{
		prev_NodeLinkedList->next = current->next;
		free(current);
		list->size--;

		
		return true;
	}
	return false;
}

/************************************************************************/
/* Apaga todo o conteudo da lista, mantendo o cabeçalho                 */
/************************************************************************/
LinkedList *list_destroy(LinkedList *list, bool destroyElements)
{
	assert(list != NULL);
	LinkedListNode *current = NULL;
	LinkedListNode *tmp = NULL;

	current = list->head;
	while (current != NULL)
	{
		tmp = current;
		current = current->next;
		if (destroyElements)
		{
			if (tmp->element)
			{
				free(tmp->element);
			}
		}
		free(tmp);
	}

	free(list);

	return NULL;
}

/************************************************************************/
/* Iteradores para loops                                                */
/************************************************************************/
LinkedListNode *Begin(LinkedList *list)
{
	return list->head->next;
}

LinkedListNode *Next(LinkedListNode *node)
{
	return node->next;
}




