#include "LinkedList.h"
#include "stdlib.h"
#include "stdio.h"
#include <assert.h>
#include "tries.h"

/************************************************************************/
/* Cria uma nova lista                                                  */
/************************************************************************/
List *list_new()
{
    List *list = malloc(sizeof(List));

    list->head = malloc(sizeof(NodeLinkedList));
    list->head->element = NULL;
    list->head->next = NULL;

    list->current = list->head;
    list->size = 0;
    return list;
}

/************************************************************************/
/* Verifica se a lista esta vazia                                       */
/************************************************************************/
bool list_empty(List *list)
{
    assert(list->head != NULL);
    return (list->head->next == NULL);
}

/************************************************************************/
/* Insere um elemento na lista                                          */
/************************************************************************/
void list_insert(Foto *new_element, List *list)
{
    assert(list->head != NULL && new_element != NULL);
    NodeLinkedList *new_NodeLinkedList = malloc(sizeof(NodeLinkedList));

    new_NodeLinkedList->element = new_element;
    new_NodeLinkedList->next = NULL;

    list->size++;

    list->current->next = new_NodeLinkedList;
    list->current = new_NodeLinkedList;
}

/************************************************************************/
/* Remove um elemento da lista                                          */
/************************************************************************/
bool list_remove(Foto *value, List *list)
{
    assert(value != NULL && list->head != NULL);

    NodeLinkedList *current = list->head->next;
    NodeLinkedList *prev_NodeLinkedList = list->head;

    while(current != NULL && current->element != value)
    {
        prev_NodeLinkedList = prev_NodeLinkedList -> next;
        current =current->next;
    }

    if(current->element == value)
    {
        prev_NodeLinkedList->next = current->next;
        free(current);
        list->size--;
        return true;
    }
    return false;
}

/************************************************************************/
/* Imprime todos os elementos da lista                                  */
/************************************************************************/
void list_print(List *list)
{
    assert(list != NULL && list->head != NULL);
    NodeLinkedList *current = list->head->next;

    if(current == NULL)
    {
        return;
    }

    while(current != NULL)
    {
        fprintf(stdout, "%s\n", current->element->nome);
        current = current->next;
    }
}

/************************************************************************/
/* Adiciona as fotos a lista de resultados sem repeticoes               */
/* A Filtertrie e usada para filtrar os dados e compara-los com o       */
/* filtro anterior quando existe multiplos temas                        */
/************************************************************************/
FilterTrie *list_add_to_result(FilterTrie *filter, List *photos, List *result, bool save)
{
    assert(photos != NULL && photos->head != NULL && result != NULL);
    NodeLinkedList *current = photos->head->next;

    if(current == NULL)
    {
        return NULL;
    }

    FilterTrie *currentFilter = filtertrie_initialize();

    while(current != NULL)
    {
        //filtracao de resultados
        //obter o digito id da foto em formato string
        char * digito = convertDigitToChar(current->element->id);

        //o filtro nao foi aplicado e nao e para salvar os resultados
        //valido quando e o primeira tema a ser verificado
        if(!filter && !save)
        {
            filtertrie_insert(currentFilter, digito);
        }
        //existe filtro para ser comparado e nao e para salvar os resultados
        //valido quando existe um filtro para ser aplicado ao tema actual que nao
        //e o primeiro tema na pesquisa
        else if(filter && !save)
        {
            if(filtertrie_find(filter, digito))
            {
                filtertrie_insert(currentFilter, digito);
            }
        }
        //existe filtro e os resultados sao para ser salvar
        //valido quando existe uma pesquisa multi-tema e o tema actual e o ultimo
        else if(filter && save)
        {
            if(filtertrie_find(filter, digito))
            {
                list_insert(current->element, result);
            }
        }
        //nao existe um filtro e os resultados sao para salvar
        //valido quando existe uma pesquisa multi-tema ou de tema unico.
        else if(!filter && save)
        {
            list_insert(current->element, result);
        }

        free(digito);

        current = current->next;
    }
    filter ? filtertrie_destroy(filter) : NULL;
    return currentFilter;
}

/************************************************************************/
/* Apaga todo o conteudo da lista, mantendo o cabeçalho                 */
/************************************************************************/
List *list_destroy(List *list, bool destroyElements)
{
    assert(list != NULL);
    NodeLinkedList *current = NULL;
    NodeLinkedList *tmp = NULL;

    current = list->head;
    while(current != NULL)
    {
        tmp = current;
        current = current->next;
        if(destroyElements)
        {
            if(tmp->element)
            {
                free(tmp->element->nome);
                free(tmp->element->temas);
                free(tmp->element);
            }
        }
        free(tmp);
    }

    free(list);

    return NULL;
}

