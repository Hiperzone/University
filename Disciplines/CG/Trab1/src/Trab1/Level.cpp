#include "Level.h"

Level::Level(string filename)
{
	this->filename = filename;
	this->data = NULL;
	this->size_x = 0;
	this->size_y = 0;
	this->start_x = 0;
	this->start_y = 0;
}

Level::~Level(void)
{
	for(int i = 0; i < size_x; i++)
	{
		delete[] data[i];
	}
	delete[] data;
}

/**
 * Carrega os dados de um nivel caso seja possivel.
 *
 */
int Level::LoadLevel()
{
	std::ifstream stream;

	stream.open(filename, std::ifstream::in);
	if(!stream.is_open())
	{
		return ERROR_FAILED_TO_LOAD_LEVEL;
	}

	string buffer;
	//ler o tamanho
	getline(stream, buffer);
	string s_x = buffer.substr(0, buffer.find(" "));
	string s_y = buffer.substr(buffer.find(" "));
	string s_mode = buffer.substr(buffer.find(" ") + 3, buffer.find(" "));
	int l_mode = atoi(s_mode.c_str());
	this->levelMode = l_mode;

	size_x = atoi(s_x.c_str());
	size_y = atoi(s_y.c_str());

	data = new int*[size_x];

	for(int i = 0; i < size_x; i++)
	{
		data[i] = new int[size_y];
	}

	for(int i = 0; i < size_x; i++)
	{
		for(int j = 0; j < size_y; j++)
		{
			data[i][j] = 0;
		}
	}
	
	int line = 0;
	while(!stream.eof())
	{
		char bufferb[255];
		//ler os dados do nivel
		stream.getline(bufferb, 255);
		//sacar os dados
		ReadContent(bufferb, line);
		line++;
	}
	
	stream.close();

	return ERROR_NONE;
}

/**
 * Obtem a posicao x do jogador na array do tabuleiro.
 * Apenas util para operacoes de conversao para coordenadas
 * do mundo opengl ou para cameras.
 *
 */
int Level::getPlayerStartX()
{
	return this->start_x;
}

/**
 * Obtem a posicao y do jogador na array do tabuleiro.
 * Apenas util para operacoes de conversao para coordenadas
 * do mundo opengl ou para cameras.
 *
 */
int Level::getPlayerStartY()
{
	return this->start_y;
}

/**
 * Le o conteudo de cada linha do ficheiro.
 *
 */
void Level::ReadContent(char *buffer, int row)
{
	int column = 0;
	while(*buffer != '\0')
	{
		char c = *buffer;
		switch(c)
		{
		case 'B':
			data[column][row] = LEVEL_PATH_BLOCKED;
			break;
		case 'P':
			data[column][row] = LEVEL_PATH_PASSABLE;
			break;
		case 'R':
			data[column][row] = LEVEL_PATH_OBJECTIVE_RED;
			break;
		case 'V':
			data[column][row] = LEVEL_PATH_OBJECTIVE_GREEN;
			break;
		case 'A':
			data[column][row] = LEVEL_PATH_OBJECTIVE_BLUE;
			break;
		case 'S':
			data[column][row] = LEVEL_PATH_PLAYER_START;
			this->start_x = column;
			this->start_y = row;
			break;
		default:
			printf("dados desconhecidos ao ler o nivel\n");
			break;
		};
		column++;
		buffer++;
	}
}

/**
 * Obtem a largura do tabuleiro que sera usado para
 * criar arrays bidimensionais.
 *
 */
int Level::getTileSizeX()
{
	return this->size_x;
}

/**
 * Obtem a altura do tabuleiro que sera usado para
 * criar arrays bidimensionais.
 *
 */
int Level::getTileSizeY()
{
	return this->size_y;
}

/**
 * Desenha o nivel.
 *
 */
void Level::Update()
{
	float screenAdjustmentX = Common::getClippingFromTile(size_x, false); 
	float screenAdjustmentY = Common::getClippingFromTile(size_y, false);

	//draw the level
	int modColorPicker = 1;
	float squareSize = 0.2f;
	//desenhar os quadrados excepto os que estao bloqueados
	glPushMatrix();
	for(int i = 0; i < size_y; i++)
	{
		if( size_x % 2 == 0) { modColorPicker = (modColorPicker + 1) % 2; }
		for(int j = 0; j < size_x; j++)
		{
			
			//caminho onde se pode passar.
			switch(this->data[j][i])
			{
			case LEVEL_PATH_BLOCKED:
				
				//glColor3f(1, 1, 1);
				//glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			case LEVEL_PATH_PASSABLE:
				modColorPicker ? glColor3f(0.5, 0.5, 0.5) : glColor3f(0.3, 0.3, 0.3);
				glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			case LEVEL_PATH_OBJECTIVE_BLUE:
				glColor3f(0.0, 0.0, 1.0);
				glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			case LEVEL_PATH_OBJECTIVE_RED:
				glColor3f(1.0, 0.0, 0.0);
				glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			case LEVEL_PATH_OBJECTIVE_GREEN:
				glColor3f(0.0, 1.0, 0.0);
				glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			case LEVEL_PATH_PLAYER_START:
				glColor3f(1.0, 1.0, 0.0);
				glRectf( j * squareSize - screenAdjustmentX, i * -squareSize + screenAdjustmentY, j * squareSize + squareSize - screenAdjustmentX, i * -squareSize - squareSize + screenAdjustmentY);
				break;
			default:
				printf("Aviso: dados desconhecidos ao desenhar o nivel\n");
				break;
			};
			modColorPicker = (modColorPicker + 1) % 2;
		}
	}
	glPopMatrix();
}

/**
 * Retorna a array que contem os dados do nivel.
 *
 */
int ** Level::getLevelData()
{
	return this->data;
}

/**
 * Atribui o modo de jogo a este nivel.
 *
 */
void Level::setLevelMode(int m)
{
	this->levelMode = m;
}

/**
 * Retorna o modo de jogo para este nivel.
 *
 */
int Level::getLevelMode()
{
	return this->levelMode;

}

/**
 *Verifica se as coordenadas dadas colidem com um
 *tile bloqueado.
 *Usa pixel color colision pelo backbuffer.
 *Lanca tambem os eventos que sejam detectados.
 */
bool Level::Collides(float x, float y)
{
	float data[3];
	GLint viewport[4];
	GLdouble modelview[16];
	GLdouble projection[16];
	glGetIntegerv(GL_VIEWPORT, viewport);
	glGetDoublev( GL_MODELVIEW_MATRIX, modelview );
	glGetDoublev( GL_PROJECTION_MATRIX, projection );

	GLdouble winx, winy, winz;
	glutPostRedisplay();
	glReadBuffer(GL_BACK);
	gluProject(x + 0.1, y + 0.1, 0.0, modelview, projection,viewport, &winx, &winy, &winz);
	glReadPixels(winx, winy, 1, 1, GL_RGB, GL_FLOAT, data);

	if(data[0] == 0.0f && data[1] == 0.0f && data[2] == 0.0f)
	{
		return true;
	}
	else if(data[0] == 0.0f && data[1] == 0.0f && data[2] == 1.0f)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_BLUE);
	}
	else if(data[0] == 1.0f && data[1] == 0.0f && data[2] == 0.0f)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_RED);
	}
	else if( data[0] == 0.0f && data[1] == 1.0f && data[2] == 0.0f)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_GREEN);
	}
	return false;
}

/**
 * Permite mudar a jogabilidade do nivel baseado no seu modo
 * de jogo.
 *
 */
void Level::UpdateLevelModeAI(float px, float py)
{
	//elimina os quadrados onde o jogador ja passou
	if(Common::getGame()->getLevel()->getLevelMode() == LEVEL_MODE_USE_BRAIN)
	{
		float cX = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeX(), false);
		float cY = Common::getClippingFromTile( Common::getGame()->getLevel()->getTileSizeY(), false);
		int tx = abs(Common::roundToTile(px + cX));
		int ty = abs(Common::roundToTile(py - cY + 0.2));
		Common::getGame()->getLevel()->getLevelData()[tx][ty] = LEVEL_PATH_BLOCKED;
	}
}
