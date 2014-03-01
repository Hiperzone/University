#include "FileStream.h"
#include "LinkedList.h"
#include "Foto.h"
#include "tries.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <assert.h>


/************************************************************************/
/* Converte um inteiro para uma string                                  */
/************************************************************************/
void intToChar(int value, char *buffer)
{
    buffer[0] = value & 0xFF;
    buffer[1] = (value >> 8) & 0xFF;
    buffer[2] = (value >> 16) & 0xFF;
    buffer[3] = (value >> 24) & 0xFF;
}

/************************************************************************/
/* Guarda uma foto no disco                                             */
/************************************************************************/
void guardarFoto(Foto *foto)
{
    if(foto == NULL)
    {
        fprintf(stderr, "%s", "Erro ao guardar foto, foto invalida\n");
        return;
    }

    FILE *fp = fopen("data.dat", "ab");

    if(fp == NULL)
    {
        fprintf(stderr, "%s", "erro ao carregar o ficheiro de fotos\n");
        return;
    }

    //pular para o fim do ficheiro
    fseek(fp, 0, SEEK_END);

    //escrever os dados
    char buffer[4];
    memset(buffer, 0, sizeof(buffer));

    int size = strlen(foto->nome);
    intToChar(size, buffer);
    fwrite(buffer, 4, 1, fp);
    fwrite(foto->nome, strlen(foto->nome) + 1, 1, fp);

    size = strlen(foto->temas);
    intToChar(size, buffer);
    fwrite(buffer, 4, 1, fp);
    fwrite(foto->temas, strlen(foto->temas) + 1, 1, fp);
    fclose(fp);
}

/************************************************************************/
/* le uma string de um ficheiro                                         */
/************************************************************************/
char *readString(FILE * fp)
{
    int size = 255;
    char * sstring = malloc(sizeof(char)*size);
    char buffer;
    int indice = 0;
    char *dest = NULL;

    while(fread(&buffer,1, 1, fp) != 0)
    {
        if(buffer == '\0')
        {
            break;
        }
        sstring[indice++] = buffer;
        if(indice >= size)
        {
            sstring = realloc(sstring, size + 255);
            size = size + 255;
        }
    }
    dest = malloc(indice + 1);
    memset(dest, 0, indice + 1);
    strncpy(dest, sstring, indice);
    free(sstring);
    return dest;
}

/************************************************************************/
/*Permite carregar fotos a partir de um ficheiro                        */
/************************************************************************/
void carregarFotos(char *filename, Trie *trie, List *list)
{
    char tema[255];
    char *temas = NULL;
    int indice = 0;

    assert(trie != NULL && list != NULL);

    FILE *fp =  fopen(filename, "rb");

    if(fp == NULL)
    {
        return;
    }

    fseek(fp, 0, SEEK_END);
    int fsize = ftell(fp);

    fseek(fp, 0, SEEK_SET);
    int fcurrsize = 0;

    while(fcurrsize != fsize)
    {
        //alocar espaco na estructura
        Foto *foto = newFoto();

        //ler o conteudo
        char nomeSize[4];

        fread(nomeSize, 4, 1, fp);
        int nsize = (*(int *)&nomeSize);
        foto->nome = malloc(nsize + 1);
        fread(foto->nome, nsize + 1, 1, fp);

        char temasSize[4];

        fread(temasSize, 4, 1, fp);
        int tsize = (*(int *)&temasSize);

        foto->temas = malloc(tsize + 1);
        fread(foto->temas, tsize + 1, 1, fp);

        //ler os temas da foto
        temas = foto->temas;
        while(temas && *temas != '\0')
        {
            //detectar quando existe espacos
            if(temas[0] == '|')
            {
                tema[indice] = '\0';

                //inserir a foto na trie com o endereco da foto
                trie_insert(trie, tema);
                //inserir a foto
                trie_insert_element(trie, tema, foto);
                memset(tema, 0, sizeof(tema));
                indice = 0;
            }
            else
            {
                tema[indice] = temas[0];
                indice++;
            }
            temas++;
        }

        trie_insert(trie, tema);
        trie_insert_element(trie, tema, foto);

        //adicionar a foto a linked list
        list_insert(foto,list);
        memset(tema, 0, sizeof(tema));
        indice = 0;
        fcurrsize = ftell(fp);
    }
    fclose(fp);
}





