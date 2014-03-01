#include "Common.h"

Game * Common::game;

Common::Common()
{
	Common::game = new Game(500,500);

}

Common::~Common()
{
	delete Common::game;

}

/**
 * Converte uma coordenada do mundo opengl para uma
 * coordenada de uma array.
 * Arredondado para compensar margens de erro.
 */
int Common::roundToTile(float position)
{
	int tile = floor(position*5 + 0.05);
	return tile;
}

/**
 * Obtem a instancia do jogo actual.
 *
 */
Game *Common::getGame()
{
	return game;
}

/**
 * Converte uma posicao na array em coordenadas do
 * mundo opengl.
 * E necessario fazer ajustamentos porque as coordenadas 
 * convertidas tem como origem a posicao (0,0).
 */
float Common::tileToPos(int tile)
{
	return tile * MAP_TILE_SIZE;
}

/**
 * Obtem as coordenadas "ideais" para o clipping que
 * sera usado no glortho baseado no tamanho do nivel.
 * Permite arredondar para o numero inteiro acima.
 */
float Common::getClippingFromTile(int tile, bool upperRound)
{
	if(upperRound)
		return ceil(tile * 0.1 + 0.5);
	else
		return tile * 0.1;
}
