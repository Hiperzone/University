#include "Common.h"

#ifndef GAME_H
#define GAME_H

/**
 *Classe que contem os métodos necessários para que o jogo se torne jogável,
 *seja possível mostrar o tabuleiro no ecrã, configurar o OpenGL, Glut 
 * e camerasentre outras tarefas.
 *
 */
class Game
{
public:
	Game(int width, int height);
	~Game(void);

	void SetupGL(int argc, char **argv);
	void SetupCameras();

	static void Draw();
	static void reshape (int width, int height);
	static void keyboardEvent(unsigned char c, int x, int y);
	void DrawUI();
	static void Update(int value);
	void Event(int event);
	int getState();

	void drawAxis();

	int getWidth();
	int getHeight();

	void setWidth(int w);
	void setHeight(int h);

	Level * getLevel();
	void setLevel(Level *level);
	Player *getPlayer();

	void setPlayer(Player * player);

	Camera *getCamera();
	Camera *getCamera(int i);
	void setCamera(int i);

	void drawText(char *text, float x, float y, float r, float g, float b, void * font);

	void UpdateViewport(GLint x, GLint y, GLsizei width, GLsizei height);
	int getCurrentCameraId();

	void setCurrentLevelId(int id);
	int getCurrentLevelId();

private:
	Level * currentLevel;
	Player * currentPlayer;
	Camera * camera[2];
	int     currentCamera;
	int     width;
	int     height;
	int     windowHandle;
	int     state;
	int     currentLevelId;
};

#endif
