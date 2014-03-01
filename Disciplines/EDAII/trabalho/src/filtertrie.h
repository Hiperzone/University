
#include "stdbool.h"

#include <assert.h>
#ifndef FILTERTRIE_H
#define FILTERTRIE_H


#define FILTER_TRIE_TABLE_SIZE 10
typedef struct FilterTrie FilterTrie;

struct FilterTrie
{
    bool word;
    FilterTrie *table[FILTER_TRIE_TABLE_SIZE];
};


char * convertDigitToChar(int digit);
FilterTrie * filtertrie_initialize();
void filtertrie_insert(FilterTrie * node, char *id);
bool filtertrie_find(FilterTrie  * node, char *id);
FilterTrie * filtertrie_destroy(FilterTrie * node);

#endif
