"""MODULO MODO
   Descricao: Implementa e controla todos os modos de jogo disponiveis
   Autores: Daniel Ramos(29423), Eduardo Martins(29035)
"""

import jogo

def main():
    cMy_Game = jogo.DotsAndBoxes()
    while(True):
        if(not cMy_Game.Run()):
            break;
main()
