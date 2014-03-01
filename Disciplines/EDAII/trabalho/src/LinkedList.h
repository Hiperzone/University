#include "stdbool.h"
#include "Foto.h"
#include "filtertrie.h"

#ifndef LINKEDLIST_H
#define	LINKEDLIST_H

#define SIZE(x) (sizeof(x)/sizeof(x[0]))

typedef struct NodeLinkedList NodeLinkedList;

struct NodeLinkedList
{
    NodeLinkedList *next;
    Foto *element;
};

typedef struct List List;
struct List
{
    NodeLinkedList *head;
    NodeLinkedList *current;
    int size;
};

List *list_new();
void list_insert(Foto *new_element, List *list);
FilterTrie *list_add_to_result(FilterTrie *filter, List *photos, List *result, bool save);
bool list_remove(Foto  *value, List *list);
void list_print(List *list);
List *list_destroy(List *list, bool destroyElements);
bool list_empty(List *list);

#endif	/* LINKEDLIST_H */

