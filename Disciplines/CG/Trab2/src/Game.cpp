#include "Common.h"


Game::Game(int width, int height)
{
	this->currentPlayer = NULL;
	Game::SetupCameras();
	this->width = width;
	this->height = height;
	this->currentLevel = NULL;
	this->currentLevelId = 1;
	windowHandle = -1;
	currentShading = 0;
	menuScreen = 0;
	menuOption = 0;
	state = GAME_STATE_NOT_PLAYING;
	Game::setCamera(1);
}

Game::~Game(void)
{
	if(currentPlayer)
		delete currentPlayer;
	if(currentLevel)
		delete currentLevel;
	if(camera[0])
		delete camera[0];
	if(camera[1])
		delete camera[1];
	if(camera[2])
		delete camera[2];
	if(camera[3])
		delete camera[3];
}

/**
 * Desenha os eixos.
 */
void Game::drawAxis()
{
	glPushMatrix();
	glBegin(GL_LINES);

	//x
	glColor3f(1,0,0);
	glVertex3f(-10,0,0);
	glVertex3f(10,0,0);

	//y
	glColor3f(0,1,0);
	glVertex3f(0,10,0);
	glVertex3f(0,-10,0);

	//z
	glColor3f(0,0,1);
	glVertex3f(0,0,10);
	glVertex3f(0,0,-10);

	glEnd();
	glPopMatrix();
}

/**
 * Configuracao inicial do opengl.
 */
void Game::SetupGL(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(this->width, this->height);
	glutInitWindowPosition(this->width / 2, this->height / 2);
	windowHandle = glutCreateWindow("Trabalho 2 Computacao Grafica");
	glutDisplayFunc(Game::Draw);
	glutTimerFunc(1000/58, Game::Update, 1000/58);
	glutKeyboardFunc(Game::keyboardEvent);
	glutReshapeFunc(Game::reshape);
	// Define a localiza‹o do viewpoint como local
	glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE);

	glShadeModel(GL_SMOOTH);  
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);

	glDepthFunc(GL_LEQUAL);
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 

	//actualizar o viewport inicial
	Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
}

/**
 * Configura as cameras.
 */
void Game::SetupCameras()
{
	this->camera[0] = new MiniMapCamera();
	this->camera[1] = new IsometricCamera();
	this->camera[2] = new FirsPersonCamera();
	this->camera[3] = new ThirdPersonCamera();
}

/************************************************************************/
/* Prepara as luzes durante o carregamento do nivel                     */
/************************************************************************/
void Game::SetupLights()
{
	//desactivar todas as luzes primeiro
	for(int i = 0; i <= GL_LIGHT7; i++)
	{
		glDisable(GL_LIGHT0 + i);
	}

	//preparar as luzes presentes no nivel
	for(int j= 0; j < Common::getGame()->getLevel()->getNumLights(); j++)
	{
		Common::getGame()->getLevel()->getLight(j)->prepareLight();
	}
}

/**
 * Actualiza o viewport.
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
	glutTimerFunc(1000/58, Game::Update, 1000/58);
}

/**
 * Desenha a parte do interface grafico, como por exemplo
 * os objectivos do nivel.
 */
void Game::DrawUI()
{	
	glDisable(GL_LIGHTING);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	if(this->getState() == GAME_STATE_NOT_PLAYING)
	{
		if(menuScreen == MENU_SCREEN_MAIN)
		{
			glDisable(GL_TEXTURE_2D);
			if(menuOption % 3 == MENU_OPTION_1)
				glColor3f(1,0,0);
			else
				glColor3f(0,0,1);
			glRectf(-0.2, 0, 0.2, 0.2);
			drawText("Jogar",-0.055, 0.08, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);

			glDisable(GL_TEXTURE_2D);
			if(menuOption % 3 == MENU_OPTION_2)
				glColor3f(1,0,0);
			else
				glColor3f(0,0,1);
			glRectf(-0.2, -0.3, 0.2, -0.1);
			drawText("Instrucoes",-0.1, -0.211, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);

			glDisable(GL_TEXTURE_2D);
			if(menuOption % 3 == MENU_OPTION_3)
				glColor3f(1,0,0);
			else
				glColor3f(0,0,1);
			glRectf(-0.2, -0.6, 0.2, -0.4);
			drawText("Sair",-0.04, -0.512, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			glDisable(GL_TEXTURE_2D);
		}
		else if( menuScreen == MENU_SCREEN_INSTRUCTIONS)
		{
			drawText("Procure pela sequencia de cores, vermelho, verde",-0.45, 0.5, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			drawText("e azul para completar um nivel",-0.45, 0.45, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			drawText("Use a tecla C para mudar de camera", -0.45, 0.2, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			drawText("Use a tecla R para repetir o nivel", -0.45, 0.1, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			drawText("Use a tecla P para abrir portas", -0.45, 0, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
			drawText("Use a tecla O para mudar o shading", -0.45, -0.1, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);

			glDisable(GL_TEXTURE_2D);
			if(menuOption % 1 == MENU_OPTION_1)
				glColor3f(1,0,0);
			else
				glColor3f(0,0,1);
			glRectf(-0.2, -0.6, 0.2, -0.4);
			drawText("Voltar",-0.053, -0.52, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
		}
	}

	if(this->getState() == GAME_STATE_PLAYING)
	{
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_NONE)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
			drawText("vermelho", -0.5 , 0.9, 1, 0, 0, GLUT_BITMAP_TIMES_ROMAN_24);
		}
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_RED)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
			drawText("verde", -0.5 , 0.9, 0, 1, 0, GLUT_BITMAP_TIMES_ROMAN_24);
		}
		if(getPlayer() && getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_GREEN)
		{
			drawText("Procure o quadrado ", -1 , 0.9, 1, 1, 1, GLUT_BITMAP_TIMES_ROMAN_24);
			drawText("azul", -0.5 , 0.9, 0, 0, 1, GLUT_BITMAP_TIMES_ROMAN_24);
		}
	}
	else if(this->getState() == GAME_STATE_PLAYER_WON)
	{
		drawText("Parabens, chegaste ao fim do jogo!",  -0.4 , 0.1, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);

		glDisable(GL_TEXTURE_2D);
		if(menuOption % 1 == MENU_OPTION_1)
			glColor3f(1,0,0);
		else
			glColor3f(0,0,1);
		glRectf(-0.2, -0.6, 0.2, -0.4);
		drawText("Voltar",-0.053, -0.52, 1, 1, 1, GLUT_BITMAP_HELVETICA_18);
	}
	glMatrixMode(GL_MODELVIEW);
	glEnable(GL_LIGHTING);
	glEnable(GL_TEXTURE_2D);
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
 */
void Game::setWidth(int w)
{
	this->width = w;
}

/**
 * Atribui a altura do ecra.
 */
void Game::setHeight(int h)
{
	this->height = h;
}

/**
 * Atribui o id do nivel actual.
 */
void Game::setCurrentLevelId(int id)
{
	this->currentLevelId = id;
}

/**
 * Obtem o id do nivel actual.
 */
int Game::getCurrentLevelId()
{
	return this->currentLevelId;
}

/************************************************************************/
/* Retorna o shading activo                                              */
/************************************************************************/
int Game::getShading()
{
	return currentShading;
}

/************************************************************************/
/* Atribui o shading activo                                             */
/************************************************************************/
void Game::setShading(int shading)
{
	currentShading = shading;
}

/************************************************************************/
/* Eventos do keyboard relativos ao UI                                  */
/************************************************************************/
void Game::keyboardUIEvent(unsigned char c, int x, int y)
{
	if(c == '\x1b')
	{
		exit(0);
	}
	else if(c == 's')
	{
		if(Common::getGame()->getMenuScreen() == MENU_SCREEN_MAIN)
		{
			Common::getGame()->setMenuOption((Common::getGame()->getMenuOption() + 1) % 3);
		}
	}
	else if( c == 'w')
	{
		if(Common::getGame()->getMenuScreen() == MENU_SCREEN_MAIN)
		{
			Common::getGame()->setMenuOption( (Common::getGame()->getMenuOption() - 1 % 3) < 0 ? 2 : (Common::getGame()->getMenuOption() - 1 % 3) );
		}
	}
	else if(c =='\r')
	{
		//primir enter enquanto se esta no menu principal
		//executar a funcao adequada
		if(Common::getGame()->getMenuScreen() == MENU_SCREEN_MAIN)
		{
			if(Common::getGame()->getMenuOption() == MENU_OPTION_1)
			{
				Player * player = new Player();

				string cLevel = LEVEL_PATH;
				cLevel += "/nivel/nivel";
				cLevel += "1";
				cLevel += ".txt";

				Level *level = new Level((char*)cLevel.c_str());
				Common::getGame()->setLevel(level);
				if(level->LoadLevel() == ERROR_NONE)
				{
					float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
					float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);

					Common::getGame()->setPlayer(player);
					int xPlayerPosition = Common::tileToPos(level->getPlayerStartX());
					int yPlayerPosition = Common::tileToPos(level->getPlayerStartY());
					Common::getGame()->getPlayer()->setPosition( xPlayerPosition, yPlayerPosition , 0.0f);
					Common::getGame()->getPlayer()->setFront(DIRECTION_X_POSITIVE);

					Common::getGame()->setCamera(CAMERA_ISOMETRIC);
					Common::getGame()->getCamera()->prepareCamera();

					// Coloca a camera no jogador
					Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_NONE);
					// Coloca a luz no jogador
					if(Common::getGame()->getLevel()->getNumLights())
					{
						//existem luzes, preparar as luzes para o nivel
						Common::getGame()->SetupLights();
						//apenas activar a luz do jogador se estiver configurada.
						if(Common::getGame()->getLevel()->getLight(0))
						{
							Common::getGame()->getLevel()->getLight(0)->setLightPosition(xPlayerPosition, yPlayerPosition, 1);
						}
					}

					Common::getGame()->Event(EVENT_PLAYER_PLAYING);
				}
				else
				{
					//erro critico, nunca devera acontecer
					Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
				}

			}
			else if(Common::getGame()->getMenuOption() == MENU_OPTION_2)
			{
				Common::getGame()->setMenuScreen(MENU_SCREEN_INSTRUCTIONS);
				Common::getGame()->setMenuOption(MENU_OPTION_1);
			}
			else if(Common::getGame()->getMenuOption() == MENU_OPTION_3)
			{
				exit(0);
			}
		}
		else if(Common::getGame()->getMenuScreen() == MENU_SCREEN_INSTRUCTIONS)
		{
			Common::getGame()->setMenuScreen(MENU_SCREEN_MAIN);
			Common::getGame()->setMenuOption(MENU_OPTION_1);
		}
	}
}
/**
 * Gere os eventos do teclado.
 *
 */
void Game::keyboardEvent(unsigned char c, int x, int y)
{
	float delta = 0.2f;
	char esc = '\x1b';
	char enter = '\r';
	
	if(Common::getGame()->getState() == GAME_STATE_NOT_PLAYING)
	{
		keyboardUIEvent(c, x, y);
	}
	else if(c == esc && Common::getGame()->getState() == GAME_STATE_PLAYING)
	{
		Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
	}
	else if(c == enter && Common::getGame()->getState() == GAME_STATE_PLAYER_WON)
	{
		Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
	}
	else if(Common::getGame()->getState() == GAME_STATE_PLAYING)
	{	
		vector3 v =  Common::getGame()->getPlayer()->getPosition();
		if(c == 'r')
		{
			//recarregar o nivel actual
			string buffer;
			string cLevel = LEVEL_PATH;
			cLevel += "/nivel/nivel";
			buffer = std::to_string(Common::getGame()->getCurrentLevelId());
			cLevel += buffer;
			cLevel += ".txt";
			
			//apagar o nivel actual
			delete Common::getGame()->getLevel();
			Common::getGame()->setLevel(NULL);
			Level *level = new Level(cLevel);


			Common::getGame()->setLevel(level);
			//carregar o proximo nivel se existir.
			if(level->LoadLevel() == ERROR_NONE)
			{
				float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
				float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);

				float xPlayerPosition = Common::tileToPos(level->getPlayerStartX());
				float yPlayerPosition = Common::tileToPos(level->getPlayerStartY());

				//reset dos objectivos e posicao
				Common::getGame()->getPlayer()->setObjectiveState(EVENT_NONE);
				Common::getGame()->getPlayer()->setPosition( xPlayerPosition, yPlayerPosition , 0.0f);
		
				Common::getGame()->setCamera(CAMERA_ISOMETRIC);
				Common::getGame()->getCamera()->prepareCamera();
				// Coloca a camera no jogador
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_NONE);
				// Coloca a luz no jogador
				if(Common::getGame()->getLevel()->getNumLights())
				{
					//existem luzes, preparar as luzes para o nivel
					Common::getGame()->SetupLights();
					//apenas activar a luz do jogador se estiver configurada.
					if(Common::getGame()->getLevel()->getLight(0))
					{
						Common::getGame()->getLevel()->getLight(0)->setLightPosition(xPlayerPosition, yPlayerPosition, 1);
					}
				}
				Common::getGame()->Event(EVENT_PLAYER_PLAYING);
			}
			else
			{
				//erro critico, nunca devera acontecer
				Common::getGame()->Event(EVENT_PLAYER_NOT_PLAYING);
			}
		}
		else if(c == 'c')
		{
			Common::getGame()->setCamera( (Common::getGame()->getCurrentCameraId() + 1) % 4);
			int currentCamera = Common::getGame()->getCurrentCameraId();
			
			if(currentCamera == CAMERA_MINIMAP || currentCamera == CAMERA_ISOMETRIC)
				Common::getGame()->getCamera()->setPosition(currentCamera, DIRECTION_NONE);
			else
				Common::getGame()->getCamera()->setPosition(currentCamera, Common::getGame()->getPlayer()->getFront());
			
			Common::getGame()->getCamera()->prepareCamera();
		}
		else if(c == 'd')
		{
			if(Common::getGame()->getCurrentCameraId() == CAMERA_FIRST_PERSON
				|| Common::getGame()->getCurrentCameraId() == CAMERA_THIRD_PERSON)
			{
				int frontPlayer = Common::getGame()->getPlayer()->getFront();
				int newDirection = (frontPlayer + 3)%4;
				Common::getGame()->getPlayer()->setFront(newDirection);
				Common::getGame()->getPlayer()->UpdateZPosition(newDirection);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), newDirection);
			}
			else
			{
				if(!Common::getGame()->getPlayer()->Collides(v.x + delta, v.y))
				{
					v.x = v.x + delta;
					Common::getGame()->getPlayer()->setPosition(v);
					Common::getGame()->getPlayer()->UpdateZPosition(DIRECTION_X_POSITIVE);
					Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_X_POSITIVE);
				}
			}
			if(Common::getGame()->getLevel()->getLight(0))
			{
				Common::getGame()->getLevel()->getLight(0)->setLightPosition(v.x, v.y, v.z + 1);
			}
		}
		else if(c == 'a')
		{
				if(Common::getGame()->getCurrentCameraId() == CAMERA_FIRST_PERSON
				   || Common::getGame()->getCurrentCameraId() == CAMERA_THIRD_PERSON)
				{
					int frontPlayer = Common::getGame()->getPlayer()->getFront();
					int newDirection = (frontPlayer + 1)%4;
					Common::getGame()->getPlayer()->setFront(newDirection);
					Common::getGame()->getPlayer()->UpdateZPosition(newDirection);
					Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), newDirection);
				}
				else
				{
					if(!Common::getGame()->getPlayer()->Collides(v.x - delta, v.y))
					{
						v.x = v.x - delta;
						Common::getGame()->getPlayer()->setPosition(v);
						Common::getGame()->getPlayer()->UpdateZPosition(DIRECTION_X_NEGATIVE);
						Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_X_NEGATIVE);
					}
				}

				if(Common::getGame()->getLevel()->getLight(0))
				{
					Common::getGame()->getLevel()->getLight(0)->setLightPosition(v.x, v.y, v.z + 1);
				}
		}
		else if(c == 'w')
		{
			if(Common::getGame()->getCurrentCameraId() == CAMERA_FIRST_PERSON
				|| Common::getGame()->getCurrentCameraId() == CAMERA_THIRD_PERSON)
			{
				Common::getGame()->relativeMovementForward();
			}
			else
			{
				if(!Common::getGame()->getPlayer()->Collides(v.x, v.y + delta))
				{
					v.y = v.y + delta;
					Common::getGame()->getPlayer()->setPosition(v);
					Common::getGame()->getPlayer()->UpdateZPosition(DIRECTION_Y_POSITIVE);
					Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_Y_POSITIVE);
				}
			}
			if(Common::getGame()->getLevel()->getLight(0))
			{
				Common::getGame()->getLevel()->getLight(0)->setLightPosition(v.x, v.y, v.z + 1);
			}
		}
		else if(c == 's')
		{
			if(Common::getGame()->getCurrentCameraId() == CAMERA_FIRST_PERSON
				|| Common::getGame()->getCurrentCameraId() == CAMERA_THIRD_PERSON)
			{
				int frontPlayer = Common::getGame()->getPlayer()->getFront();
				int newDirection = (frontPlayer + 2)%4;
				Common::getGame()->getPlayer()->setFront(newDirection);
				Common::getGame()->getPlayer()->UpdateZPosition(newDirection);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), newDirection);
			}
			else
			{
				if(!Common::getGame()->getPlayer()->Collides(v.x, v.y - delta))
				{
					v.y = v.y - delta;
					Common::getGame()->getPlayer()->setPosition(v);
					Common::getGame()->getPlayer()->UpdateZPosition(DIRECTION_Y_NEGATIVE);
					Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_Y_NEGATIVE);
				}
			}
			if(Common::getGame()->getLevel()->getLight(0))
			{
				Common::getGame()->getLevel()->getLight(0)->setLightPosition(v.x, v.y, v.z + 1);
			}
		}
		//abrir portas
		else if(c == 'p')
		{
			//enviar evento para os objectos proximos do jogador
			if(Common::getGame()->getLevel())
			{
				//actualizar as coordenadas em Z caso seja necessario
				vector3 v =  Common::getGame()->getPlayer()->getPosition();
				Common::getGame()->getLevel()->signalEventToNearByNodes(v.x, v.y, EVENT_TRIGGER_OPEN);
			}
		}
		//shading
		else if(c == 'o')
		{
			if(Common::getGame()->getShading() == 0)
			{
				glShadeModel(GL_FLAT);
			}
			else
			{
				glShadeModel(GL_SMOOTH);
			}
			Common::getGame()->setShading((Common::getGame()->getShading() + 1) % 2);
		}
	}
}

/************************************************************************/
/* Permite mover  o jogador dependendo da direccao                      */
/************************************************************************/
void Game::relativeMovementForward()
{
	float delta = 0.2f;
	vector3 v =  Common::getGame()->getPlayer()->getPosition();
	int frontPlayer = Common::getGame()->getPlayer()->getFront();
	
	switch (frontPlayer) 
	{
		case DIRECTION_X_POSITIVE:
		{
			if(!Common::getGame()->getPlayer()->Collides(v.x + delta, v.y))
			{
				v.x = v.x + delta;
				Common::getGame()->getPlayer()->setPosition(v);
				Common::getGame()->getPlayer()->UpdateZPosition(frontPlayer);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_X_POSITIVE);
			}
		}break;
		case DIRECTION_X_NEGATIVE:
		{
			if(!Common::getGame()->getPlayer()->Collides(v.x - delta, v.y))
			{
				v.x = v.x - delta;
				Common::getGame()->getPlayer()->setPosition(v);
				Common::getGame()->getPlayer()->UpdateZPosition(frontPlayer);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_X_NEGATIVE);
			}
		}break;
		case DIRECTION_Y_POSITIVE:
		{
			if(!Common::getGame()->getPlayer()->Collides(v.x, v.y + delta))
			{
				v.y = v.y + delta;
				Common::getGame()->getPlayer()->setPosition(v);
				Common::getGame()->getPlayer()->UpdateZPosition(frontPlayer);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_Y_POSITIVE);
			}
		}break;
		case DIRECTION_Y_NEGATIVE:
		{
			if(!Common::getGame()->getPlayer()->Collides(v.x, v.y - delta))
			{
				v.y = v.y - delta;
				Common::getGame()->getPlayer()->setPosition(v);
				Common::getGame()->getPlayer()->UpdateZPosition(frontPlayer);
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_Y_NEGATIVE);
			}
		}break;
		default:break;
	}
}


/**
 * Obtem o estado do jogo actual.

 */
int Game::getState()
{
	return state;
}

/**
 * Gere os eventos lancados no jogo.
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
			string cLevel = LEVEL_PATH;
			cLevel += "/nivel/nivel";
			//char buffer[100];
			string buffer;
			buffer = std::to_string(Common::getGame()->getCurrentLevelId() + 1);
			//_itoa_s( Common::getGame()->getCurrentLevelId() + 1,buffer, 10);
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
		
				float xPlayerPosition = Common::tileToPos(level->getPlayerStartX());
				float yPlayerPosition = Common::tileToPos(level->getPlayerStartY());
				Common::getGame()->getPlayer()->setPosition( xPlayerPosition, yPlayerPosition , 0.0f);
				Common::getGame()->getPlayer()->setFront(DIRECTION_X_POSITIVE);

				Common::getGame()->setCamera(CAMERA_ISOMETRIC);
				Common::getGame()->getCamera()->prepareCamera();
				Common::getGame()->getPlayer()->setObjectiveState(EVENT_NONE);

				// Coloca a camera no jogador
				Common::getGame()->getCamera()->setPosition(Common::getGame()->getCurrentCameraId(), DIRECTION_NONE);
				// Coloca a luz no jogador
				if(Common::getGame()->getLevel()->getNumLights())
				{
					//existem luzes, preparar as luzes para o nivel
					Common::getGame()->SetupLights();
					//apenas activar a luz do jogador se estiver configurada.
					if(Common::getGame()->getLevel()->getLight(0))
					{
						Common::getGame()->getLevel()->getLight(0)->setLightPosition(xPlayerPosition, yPlayerPosition, 1);
					}
				}
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

	///////////////////////////GAME LOGIC
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
		
		Common::getGame()->UpdateViewport(0, 0, Common::getGame()->getWidth(), Common::getGame()->getHeight());
		Common::getGame()->getCamera()->prepareCamera();
		//Common::getGame()->drawAxis();
		Common::getGame()->getLevel()->Update(1000/58);
		Common::getGame()->getPlayer()->Update();
		Common::getGame()->DrawUI();
		
		//minimapa
		if(Common::getGame()->getCurrentCameraId() == CAMERA_FIRST_PERSON || Common::getGame()->getCurrentCameraId() == CAMERA_ISOMETRIC || 
			Common::getGame()->getCurrentCameraId() == CAMERA_THIRD_PERSON)
		{
			Common::getGame()->UpdateViewport(0, 0, 200, 200);
			Common::getGame()->getCamera(CAMERA_MINIMAP)->prepareCamera();
			Common::getGame()->getLevel()->Update(1000/58);
			Common::getGame()->getPlayer()->Update();
		}
		
		glutSwapBuffers();
	}
}

/**
 * Actualiza o viewport com a nova largura e altura do ecra.
 */
void Game::reshape (int width, int height)
{
	Common::getGame()->setHeight(height);
	Common::getGame()->setWidth(width);
}

void Game::drawCube(float x, float y, float z, float width)
{
	glBegin(GL_QUADS);
	glColor3f(   0.5,  0.5,  0.0 );
	glVertex3f( -width + x, -width + y, -width + z); 
	glVertex3f( -width + x,  width + y, -width + z); 
	glVertex3f(  width + x,  width + y, -width + z); 
	glVertex3f(  width + x, -width + y, -width + z);

	glColor3f(   1.0,  1.0, 1.0 );
	glVertex3f(  width + x, -width + y, width + z );
	glVertex3f(  width + x,  width + y, width + z );
	glVertex3f( -width + x,  width + y, width + z );
	glVertex3f( -width + x, -width + y, width + z );


	//// Purple side - RIGHT

	glColor3f(  1.0,  0.0,  1.0 );
	glVertex3f( width + x, -width + y, -width + z );
	glVertex3f( width + x,  width + y, -width + z );
	glVertex3f( width + x,  width + y,  width + z );
	glVertex3f( width + x, -width + y,  width + z );


	//// Green side - LEFT
	glColor3f(   0.0,  1.0,  0.0 );
	glVertex3f( -width + x, -width + y,  width + z );
	glVertex3f( -width + x,  width + y,  width + z );
	glVertex3f( -width + x,  width + y, -width + z );
	glVertex3f( -width + x, -width + y, -width + z );


	//// Blue side - TOP
	glColor3f(   0.0,  0.0,  1.0 );
	glVertex3f(  width + x,  width + y,  width + z );
	glVertex3f(  width + x,  width + y, -width + z );
	glVertex3f( -width + x,  width + y, -width + z );
	glVertex3f( -width + x,  width + y,  width + z );


	//// Red side - BOTTOM
	glColor3f(   1.0,  0.0,  0.0 );
	glVertex3f(  width + x, -width + y, -width + z );
	glVertex3f(  width + x, -width + y,  width + z );
	glVertex3f( -width + x, -width + y,  width + z );
	glVertex3f( -width + x, -width + y, -width + z );
	glEnd();
}


//Makes the image into a texture, and returns the id of the texture
// alt by José Duarte
GLuint Game::loadTexture(char *fname) {
	GLuint textureId;
	Image* image = loadBMP(fname);
	glGenTextures(1, &textureId); //Make room for our texture
	glBindTexture(GL_TEXTURE_2D, textureId); //Tell OpenGL which texture to edit
	//Map the image to the texture
	glTexImage2D(GL_TEXTURE_2D, //Always GL_TEXTURE_2D
		0, //0 for now
		GL_RGB, //Format OpenGL uses for image
		image->width, image->height, //Width and height
		0, //The border of the image
		GL_RGB, //GL_RGB, because pixels are stored in RGB format
		GL_UNSIGNED_BYTE, //GL_UNSIGNED_BYTE, because pixels are stored
		//as unsigned numbers
		image->pixels); //The actual pixel data

	delete image;
	return textureId; //Returns the id of the texture
}

/************************************************************************/
/* Retorna o id do ecra actual do UI                                    */
/************************************************************************/
int Game::getMenuScreen()
{
	return menuScreen;
}

/************************************************************************/
/* Retorna a opcao selecionada no UI                                    */
/************************************************************************/
int Game::getMenuOption()
{
	return menuOption;
}

/************************************************************************/
/* Atribui a opcao selecionada no UI                                    */
/************************************************************************/
void Game::setMenuOption( int option )
{
	menuOption = option;
}

/************************************************************************/
/* Atribui o ecra activo no UI                                          */
/************************************************************************/
void Game::setMenuScreen( int screen )
{
	menuScreen = screen;
}
