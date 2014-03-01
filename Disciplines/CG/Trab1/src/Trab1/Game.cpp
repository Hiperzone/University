#include "Common.h"


Game::Game(int width, int height)
{
	this->currentPlayer = NULL;
	camera[0] = NULL;
	camera[1] = NULL;
	this->width = width;
	this->height = height;
	this->currentLevel = NULL;
	this->currentLevelId = 1;
	windowHandle = -1;
	state = GAME_STATE_NOT_PLAYING;
	currentCamera = 1;
}

Game::~Game(void)
{
	delete currentPlayer;
	delete currentLevel;
	delete camera[0];
	delete camera[1];
}

/**
 * Desenha os eixos.
 *
 */
void Game::drawAxis()
{
	glPushMatrix();
	glBegin(GL_LINES);

	//x
	glColor3f(1,0,0);
	glVertex3f(-0.8,0,0);
	glVertex3f(0.8,0,0);

	//y
	glColor3f(0,1,0);
	glVertex3f(0,0.8,0);
	glVertex3f(0,-0.8,0);

	//z
	glColor3f(0,0,1);
	glVertex3f(0,0,0.8);
	glVertex3f(0,0,-0.8);

	glEnd();
	glPopMatrix();
}

/**
 * Configuracao inicial do opengl.
 *
 */
void Game::SetupGL(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE );
	glutInitWindowSize(this->width, this->height);
	glutInitWindowPosition(this->width / 2, this->height / 2);
	windowHandle = glutCreateWindow("Trabalho 1 Computacao Grafica");
	glutDisplayFunc(Game::Draw);
	glutTimerFunc(10, Game::Update, 10);
	glutKeyboardFunc(Game::keyboardEvent);
	glutReshapeFunc(Game::reshape);
	//actualizar o viewport inicial
	Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
}

/**
 * Configura as cameras.
 *
 */
void Game::SetupCameras()
{
	this->camera[0] = new NormalCamera();
	this->camera[1] = new IsometricCamera();
}

/**
 * Actualiza o viewport.
 *
 */
void Game::UpdateViewport(GLint x, GLint y, GLsizei width, GLsizei height)
{
	glViewport(x, y, width, height);
}

/**
 * Funcao do glut que permite actualizar continuamente
 * o ecra.
 */
void Game::Update(int timeElapsed)
{
	glutPostRedisplay();
	glutTimerFunc(10, Game::Update, 10);
}

/**
 * Desenha a parte do interface grafico, como por exemplo
 * os objectivos do nivel.
 */
void Game::DrawUI()
{	
	if(this->getState() == GAME_STATE_NOT_PLAYING)
	{
		drawText("Prima enter para jogar ou escape para sair", -0.7, 0, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
		drawText("Use a tecla C para mudar de camera", -0.7, -0.1, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
		drawText("Use a tecla R para repetir o nivel", -0.7, -0.2, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
	}

	if(this->getState() == GAME_STATE_PLAYING)
	{
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_NONE)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
		    drawText("vermelho", -0.2 , 0.9, 1, 0, 0, GLUT_BITMAP_TIMES_ROMAN_24);
		}
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_RED)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
		    drawText("verde", -0.2 , 0.9, 0, 1, 0, GLUT_BITMAP_TIMES_ROMAN_24);
		}
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_GREEN)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
		    drawText("azul", -0.2 , 0.9, 0, 0, 1, GLUT_BITMAP_TIMES_ROMAN_24);
		}
	}
	else if(this->getState() == GAME_STATE_PLAYER_WON)
	{
		drawText("Parabens, chegaste ao fim do jogo!",  -0.7 , 0.1, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
		drawText("Prime enter para voltares ao ecra principal",  -0.7 , 0, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
	}
}

/**
 * Obtem a instancia actual do jogador.
 *
 */
Player *Game::getPlayer()
{
	return this->currentPlayer;
}

/**
 * Atribui a instancia actual do jogador para
 * acesso posterior.
 */
void Game::setPlayer(Player * player)
{
	this->currentPlayer = player;
}

/**
 * Atribui a instancia actual do nivel para
 * acesso posterior.
 */
void Game::setLevel(Level *level)
{
	this->currentLevel = level;
}

/**
 * Atribui a largura do ecra.
 *
 */
void Game::setWidth(int w)
{
	this->width = w;
}

/**
 * Atribui a altura do ecra.
 *
 */
void Game::setHeight(int h)
{
	this->height = h;
}

/**
 * Atribui o id do nivel actual.
 *
 */
void Game::setCurrentLevelId(int id)
{
	this->currentLevelId = id;
}

/**
 * Obtem o id do nivel actual.
 *
 */
int Game::getCurrentLevelId()
{
	return this->currentLevelId;
}


/**
 * Gere os eventos do teclado.
 *
 */
void Game::keyboardEvent(unsigned char c, int x, int y)
{
	float delta = 0.2f;
	if(c == '\x1b' && Common::getGame()->getState() == GAME_STATE_NOT_PLAYING)
	{
		exit(0);
	}
	else if(c == '\x1b' && Common::getGame()->getState() == GAME_STATE_PLAYING)
	{
		Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
	}

	else if(c == '\r' && Common::getGame()->getState() == GAME_STATE_PLAYER_WON)
	{
		Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
	}
	//start the game
	else if(c == '\r' && Common::getGame()->getState() == GAME_STATE_NOT_PLAYING)
	{
		Player * player = new Player();

		string cLevel = "media/nivel/nivel";
		cLevel += "1";
		cLevel += ".txt";

		Level *level = new Level((char*)cLevel.c_str());
		Common::getGame()->setLevel(level);
		level->LoadLevel();
		
		float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
		float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);

		
		Common::getGame()->setPlayer(player);
		Common::getGame()->getPlayer()->setPosition( Common::tileToPos(level->getPlayerStartX()) - cX,   (cY - 0.2) - Common::tileToPos(level->getPlayerStartY()), 0.0f);
		Common::getGame()->setCamera(1);
		Common::getGame()->getCamera()->prepareCamera();
		player->setColor(0.0, 0.0, 0.0);

		Common::getGame()->Event(EVENT_PLAYER_PLAYING);
	}
	else if(Common::getGame()->getState() == GAME_STATE_PLAYING)
	{
		vector3 v =  Common::getGame()->getPlayer()->getPosition();
		
		if(c == 'r')
		{
			//recarregar o nivel actual
			string cLevel = "media/nivel/nivel";
			char buffer[100];
			_itoa_s( Common::getGame()->getCurrentLevelId(),buffer, 10);
			cLevel += buffer;
			cLevel += ".txt";
			
			//apagar o nivel actual
			delete Common::getGame()->getLevel();
			Common::getGame()->setLevel(NULL);
			Level *level = new Level(cLevel);
		
			//carregar o proximo nivel se existir.
			if(level->LoadLevel() == ERROR_NONE)
			{
				Common::getGame()->setCurrentLevelId(  Common::getGame()->getCurrentLevelId());
				Common::getGame()->setLevel(level);
		
				float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
				float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);
				
				//reset dos objectivos e posicao
				Common::getGame()->getPlayer()->setObjectiveState(EVENT_NONE);
				Common::getGame()->getPlayer()->setPosition( Common::tileToPos(level->getPlayerStartX()) - cX,   (cY - 0.2) - Common::tileToPos(level->getPlayerStartY()), 0.0f);
			}
			else
			{
				//erro critico, nunca devera acontecer
				Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
			}
		}

		if(c == 'c')
		{
			Common::getGame()->setCamera( (Common::getGame()->getCurrentCameraId() + 1) % 2);
			Common::getGame()->getCamera()->prepareCamera();				
		}

		if(c == 'd')
		{
			if(!Common::getGame()->getLevel()->Collides(v.x + delta, v.y))
			{
				Common::getGame()->getLevel()->UpdateLevelModeAI(v.x, v.y);
				v.x = v.x + delta;
				Common::getGame()->getPlayer()->setPosition(v);
			}
		}
		if(c == 'a')
		{
			if(!Common::getGame()->getLevel()->Collides(v.x - delta, v.y))
			{
				Common::getGame()->getLevel()->UpdateLevelModeAI(v.x, v.y);
				v.x = v.x - delta;
				Common::getGame()->getPlayer()->setPosition(v);
			}
		}
		if(c == 'w')
		{
			if(!Common::getGame()->getLevel()->Collides(v.x, v.y + delta))
			{
				Common::getGame()->getLevel()->UpdateLevelModeAI(v.x, v.y);
				v.y = v.y + delta;
				Common::getGame()->getPlayer()->setPosition(v);
			}
		}
		if(c == 's')
		{
			if(!Common::getGame()->getLevel()->Collides(v.x, v.y - delta))
			{
				Common::getGame()->getLevel()->UpdateLevelModeAI(v.x, v.y);
				v.y = v.y - delta;
				Common::getGame()->getPlayer()->setPosition(v);
			}
		}
		//debugging
		//printf("px: %f py: %f\n",  v.x, v.y);
	}
}

/**
 * Obtem o estado do jogo actual.
 *
 */
int Game::getState()
{
	return state;
}

/**
 * Gere os eventos lancados no jogo.
 *
 */
void Game::Event(int event)
{
	switch(event)
	{
	case EVENT_PLAYER_NOT_PLAYING:
		{
			Common::getGame()->setCurrentLevelId(1);
			delete this->currentPlayer;
			this->currentPlayer = NULL;
			delete this->currentLevel;
			this->currentLevel = NULL;
			state = GAME_STATE_NOT_PLAYING;
		} break;
	case EVENT_PLAYER_PLAYING:
		{
			state = GAME_STATE_PLAYING;
		} break;
		//FSM muita feia, era preferivel ter 1 flag com os bits necessarios para transitar de estado zzzzz.
	case EVENT_PLAYER_REACHED_BLUE:
		{
			if(getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_GREEN)
				getPlayer()->setObjectiveState(EVENT_PLAYER_REACHED_BLUE);
		}
		break;
	case EVENT_PLAYER_REACHED_GREEN:
		{
			if(getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_RED)
				getPlayer()->setObjectiveState(EVENT_PLAYER_REACHED_GREEN);
		}
		break;
	case EVENT_PLAYER_REACHED_RED:
		{
			if(getPlayer()->getObjectiveState() == 0)
				getPlayer()->setObjectiveState(EVENT_PLAYER_REACHED_RED);
		}
		break;
	case EVENT_PLAYER_FINISHED_LEVEL:
		{
			//load next level ou vitoria
			string cLevel = "media/nivel/nivel";
			char buffer[100];
			_itoa_s( Common::getGame()->getCurrentLevelId() + 1,buffer, 10);
			cLevel += buffer;
			cLevel += ".txt";
			
			//apagar o nivel actual
			delete Common::getGame()->getLevel();
			this->currentLevel = NULL;
			Level *level = new Level(cLevel);
		
			//carregar o proximo nivel se existir.
			if(level->LoadLevel() == ERROR_NONE)
			{
				Common::getGame()->setCurrentLevelId(  Common::getGame()->getCurrentLevelId() + 1);
				Common::getGame()->setLevel(level);
		
				float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
				float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);
				
				Common::getGame()->getPlayer()->setObjectiveState(EVENT_NONE);
				Common::getGame()->getPlayer()->setPosition( Common::tileToPos(level->getPlayerStartX()) - cX,   (cY - 0.2) - Common::tileToPos(level->getPlayerStartY()), 0.0f);
				Common::getGame()->setCamera(1);
				Common::getGame()->getCamera()->prepareCamera();

				state = GAME_STATE_PLAYING;
			}
			else
			{
				//fim de jogo.
				state = GAME_STATE_PLAYER_WON;
			}
		}
		break;

	default:break;
	};
}

/**
 * Obtem a altura do ecra.
 *
 */
int Game::getHeight()
{
	return this->height;
}

/**
 * Obtem a largura do ecra.
 *
 */
int Game::getWidth()
{
	return this->width;
}

/**
 * Obtem a instancia do nivel actual.
 *
 */
Level * Game::getLevel()
{
	return currentLevel;
}

/**
 * Obtem a camera actual activa.
 *
 */
Camera *Game::getCamera()
{
	return this->camera[currentCamera];
}

/**
 * Atribui o id de uma das cameras disponiveis.
 *
 */
void Game::setCamera(int i)
{
	this->currentCamera = i;
}

/**
 * Obtem o id da camera actual.
 *
 */
int Game::getCurrentCameraId()
{
	return this->currentCamera;
}

/**
 * Obtem a camera actual activa dado o seu índice.
 *
 */
Camera *Game::getCamera(int i)
{
	return this->camera[i];
}

/**
 * Permite desenhar texto no ecra.
 *
 */
void Game::drawText(char *text, float x, float y, float r, float g, float b, void * font)
{
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();
	glDisable(GL_TEXTURE_2D);
	glColor3f(r,g,b);
	glRasterPos2f(x, y);
	for(unsigned int i = 0; i < strlen(text); i++)
		glutBitmapCharacter(font, text[i]);
	glEnable(GL_TEXTURE_2D);
	glPopMatrix();
	glMatrixMode(GL_MODELVIEW);
}

/**
 * Faz o draw do nivel.
 * Desenha a parte visivel e iteractiva no front buffer.
 * Desenha o nivel no backbuffer para deteccao de colisoes.
 */
void Game::Draw()
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	//game logic
	//jogador atingiu os objectivos.
	

	if(Common::getGame()->getState() == GAME_STATE_NOT_PLAYING)
	{
		Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
		Common::getGame()->DrawUI();
		glutSwapBuffers();

	}
	else if(Common::getGame()->getState() == GAME_STATE_PLAYER_WON)
	{
		Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
		Common::getGame()->DrawUI();
		glutSwapBuffers();

	}
	else if(Common::getGame()->getState() == GAME_STATE_PLAYING)
	{
		if(Common::getGame()->getPlayer() && Common::getGame()->getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_BLUE)
		{
			Common::getGame()->Event(EVENT_PLAYER_FINISHED_LEVEL);
			glutSwapBuffers();
			return;
		}

		assert(Common::getGame() && Common::getGame()->getLevel() && Common::getGame()->getPlayer());
		Common::getGame()->getCamera()->prepareCamera();
		Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
		//Common::getGame()->drawAxis();
		//desenhar o nivel
		Common::getGame()->currentLevel->Update();
		//desenhar o jogador
		Common::getGame()->getPlayer()->Update();
		Common::getGame()->DrawUI();

		// minimap apenas para a segunda camera
		if(Common::getGame()->getCurrentCameraId() == 1)
		{
			Common::getGame()->getCamera(0)->prepareCamera();
			Common::getGame()->UpdateViewport(0, 0, 150, 150);
			Common::getGame()->currentLevel->Update();
			Common::getGame()->getPlayer()->Update();
		}

		glutSwapBuffers();
		//desenhar no backbuffer.
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//desenhar o nivel
		Common::getGame()->currentLevel->Update();
	}
}

/**
 * Actualiza o viewport com a nova largura e altura do ecra.
 *
 */
void Game::reshape (int width, int height)
{
	Common::getGame()->setHeight(height);
	Common::getGame()->setWidth(width);
}
