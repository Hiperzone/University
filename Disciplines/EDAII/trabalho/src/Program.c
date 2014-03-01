#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "Foto.h"
#include "tries.h"
#include "LinkedList.h"
#include "FileStream.h"
#include "filtertrie.h"

#define BUFFER_TEMAS_SIZE 210

/************************************************************************/
/* Aplica algumas verificacoes a uma string                             */
/************************************************************************/
bool verifyWord(char *str)
{
    assert(str != NULL);
    bool valid = false;

    //string nao pode conter espacos antes e no fim da palavra
    if(str[0] == 0x20 || str[strlen(str) - 1] == 0x20 )
    {
        return false;
    }

    //String so aceita minusculas
    while(*str != '\0')
    {
        if((str[0] >= 0x61 && str[0] <= 0x7A) || str[0] == 0x20)
        {
            valid = true;
        }
        else
        {
            valid = false;
        }
        str++;
    }
    return valid;
}

/************************************************************************/
/* Obtem uma lista de temas do input                                    */
/************************************************************************/
char *obterListaDeTemas()
{
    int s = 0;
    char s_tema[22];
    char *input = malloc(sizeof(char)*255);
    char *buffer = malloc(sizeof(char)*BUFFER_TEMAS_SIZE);
    int bufferSize = BUFFER_TEMAS_SIZE;

    memset(buffer, 0, sizeof(char)*BUFFER_TEMAS_SIZE);
    memset(input, 0, sizeof(char)*255);
    memset(s_tema, 0, sizeof(s_tema));

    while(fgets(input, 255, stdin) != NULL)
    {
        //ler um tema ate ao fim da linha
        s = sscanf(input, "%20[^\n]", s_tema);

        if(strlen(input) > 1)
        {
            if(input[0] == '.')
            {
                break;
            }

            if(strlen(input) > 21)
            {
                fputs("Insira um tema que tenha no maximo 20 caracteres\n", stderr);
                continue;
            }
            else if(!verifyWord(s_tema))
            {
                fputs("so e permitido inserir temas com letras minusculas\n", stderr);
                continue;
            }

             //concatenar o tema
            if(s == 1)
            {
                if(strlen(buffer) + 2 >= bufferSize)
                {
                    buffer = realloc(buffer, bufferSize + BUFFER_TEMAS_SIZE);
                    bufferSize = bufferSize + BUFFER_TEMAS_SIZE;
                }

                //separa os temas por barras "|"
                strcat(s_tema, "|");
                strcat(buffer, s_tema);
            }
        }
    }
    free(input);

    //retirar a ultima barra do ultimo tema
    buffer[strlen(buffer) - 1] = '\0';
    return buffer;
}

/************************************************************************/
/* Procura uma foto dado n temas                                        */
/************************************************************************/
void procurarTemas(Trie *trie, char *temas)
{
    List *result = list_new();

    trie_search_theme_sequence(trie, temas, result);

    char buffer[50];

    memset(buffer, 0 , sizeof(buffer));
    sprintf(buffer, "+ encontrada(s) %d fotografia(s)\n", result->size);
    fputs( buffer, stdout);
    list_print(result);

    result = list_destroy(result, false);
}

/************************************************************************/
/* Insere n temas na trie                                               */
/************************************************************************/
void inserirTemas(Trie *trie, char *temas, Foto *foto)
{
    int indice = 0;
    char tema[21];
    memset(tema, 0, sizeof(tema));

    while(temas && *temas != '\0')
    {
        if(temas[0] == '|')
        {
            tema[indice] = '\0';

            trie_insert(trie, tema);
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
}

/************************************************************************/
/* Faz o parsing dos comandos introduzidos na consola                   */
/************************************************************************/
void parseCommand(char *input, List *fotos, Trie *trie)
{
    int insertedPhotos = 0;
    char cmd[2];
    char tema[21];
    char nomeFoto[51];

    memset(cmd, 0, sizeof(cmd));
    memset(tema, 0, sizeof(tema));
    memset(nomeFoto, 0 , sizeof(nomeFoto));

    sscanf(input, "%1[IPX- ^ ]", cmd);
    int s = 0;

    switch(*cmd)
    {
        case 'I':
        {
            if(strlen(input) > 53)
            {
                fputs("O titulo da foto nao pode ter mais que 50 caracteres\n", stderr);
                break;
            }
            s = sscanf(input, "%1[IPX.] %50[^\n]", cmd, nomeFoto);
            if(s == 2)
            {
                    //criar a foto
                    Foto *nova_foto = newFoto();
                    nova_foto->nome = malloc(strlen(nomeFoto) + 1);
                    nova_foto->nome = strcpy(nova_foto->nome, nomeFoto);
                    nova_foto->temas = obterListaDeTemas();

                    list_insert(nova_foto, fotos);
                    guardarFoto(nova_foto);

                    //inserir os temas na trie, escolheu-se inserir no fim para evitar lixo
                    inserirTemas(trie, nova_foto->temas, nova_foto);

                    insertedPhotos = fotos->size;
                    char buffer[10];
                    memset(buffer, 0, sizeof(buffer));
                    sprintf(buffer, "+ fotografia %d introduzida\n", insertedPhotos);
                    fputs( buffer, stdout );
            }
            else
            {
                fputs("formato invalido ao inserir fotografia\n", stderr);
            }
        }break;
        case 'P':
        {
            memset(cmd, 0 , sizeof(cmd));
            memset(tema, 0, sizeof(tema));
            memset(nomeFoto, 0 , sizeof(nomeFoto));

            //aguardar pela conclusao dos temas
            char * listaTemas = obterListaDeTemas();
            //processar os temas

            procurarTemas(trie, listaTemas);
            free(listaTemas);

        }break;
        default:
        {
            fputs("Comando invalido\n", stderr);
        }break;

    }
}
// Tema 20 caracteres
// Foto 30 caracteres
int main(int argvc, char **argc)
{
    char *input = malloc(sizeof(char)*60);

    Trie * temas = NULL;
    List *fotos = NULL;

    temas = trie_initialize();
    fotos = list_new();

    carregarFotos("data.dat", temas, fotos);

    while(fgets(input, 60, stdin) != NULL && input[0] != 'X')
    {
        //scanf("%s", &input);
        //int s = sscanf(input, "%[IPX.] %50[a-z ]", str, strb);
        if(strlen(input) > 1)
        {
            if(input[0] == 'I' && input[1] != ' ')
            {
                fputs("O comando I e seguido de espaco mais a fotografia\n", stderr);
            }
            else
            {
                parseCommand(input, fotos, temas);
            }
        }
        else
        {
            fputs("Entrada invalida\n", stderr);
        }
    }

    free(input);
    trie_destroy(temas);
    list_destroy(fotos, true);
    return 0;
}
