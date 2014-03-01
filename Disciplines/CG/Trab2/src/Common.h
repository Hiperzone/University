
#ifndef COMMON_H
#define COMMON_H



#define PI 3.1415926535897932384626433832795f

//codigos permitidos da tabela ascii
// de ! a ~ 0x21 a 0x7E
#define CHAR_TO_INT(C) C - 0x21

//constantes temporarias
#define GAME_STATE_NOT_PLAYING 2
#define GAME_STATE_PLAYING 3
#define GAME_STATE_PLAYER_WON 4

#define MAP_TILE_SIZE 1

#define MENU_SCREEN_MAIN 0
#define MENU_SCREEN_INSTRUCTIONS 1

#define MENU_OPTION_1 0
#define MENU_OPTION_2 1
#define MENU_OPTION_3 2

enum EVENT_TRIGGER
{
	EVENT_TRIGGER_NO_ACTION = 0x00000000,
	EVENT_TRIGGER_OPEN      = 0x00000001,
};

//eventos

enum GameEvents
{
	EVENT_NONE = 0,
	EVENT_PLAYER_PLAYING = 1,
	EVENT_PLAYER_NOT_PLAYING,
	EVENT_PLAYER_REACHED_RED,
	EVENT_PLAYER_REACHED_GREEN,
	EVENT_PLAYER_REACHED_BLUE,
	EVENT_PLAYER_FINISHED_LEVEL,
	EVENT_PLAYER_WON
};

//erros
#define ERROR_NONE                 0
#define ERROR_FAILED_TO_LOAD_LEVEL 1

enum LevelPath
{
	LEVEL_PATH_PASSABLE = 0,
	LEVEL_PATH_BLOCKED,
	LEVEL_PATH_OBJECTIVE_RED,
	LEVEL_PATH_OBJECTIVE_BLUE,
	LEVEL_PATH_OBJECTIVE_GREEN,
	LEVEL_PATH_PLAYER_START
};

#include <stdio.h>
#include <stdlib.h>
#include <istream>
#include <fstream>
#include <iostream>
#include <string>
#include <list>
#include <assert.h>
#include <map>
using namespace std;

#ifdef __APPLE__

#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#include <cmath>

//#define LEVEL_PATH "Users/marcussantos/Dropbox/universidade/repositorios/git/universidade/Semestre 4/CG/Trab2/media/nivel"
#define LEVEL_PATH "/Users/marcussantos/Documents/CG - Trab 2/CG - Trab 2/media"

#endif

#ifdef WIN32
#include "windows.h"
#include <GL/GL.h>
#include <GL/glut.h>
#define LEVEL_PATH "media"

#endif

#include "Structs.h"
#include "Light.h"

#include "imageloader.h"
#include "Object.h"
#include "Player.h"
#include "Camera.h"

#include "Level.h"
#include "Game.h"
#include "Object.h"

#include "MiniMapCamera.h"
#include "IsometricCamera.h"
#include "FirsPersonCamera.h"
#include "ThirdPersonCamera.h"


/**
 * Acesso global a metodos e variaveis partilhadas pelo
 * projecto.
 */
class Common
{
public:
	Common();
	~Common();
	static float getClippingFromTile(int tile, bool upperRound);
	static int roundToTile(float position);
	static float tileToPos(int tile);
	static Game * getGame();
	static bool checkMapBounds(int tileX, int tileY);
private:
	static Game * game;
};

#endif
