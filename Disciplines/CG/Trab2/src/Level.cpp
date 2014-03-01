#include "Level.h"

Level::Level(string filename)
{
	this->filename = filename;
	this->colisionMap = NULL;
	this->modelMap = NULL;
	this->size_x = 0;
	this->size_y = 0;
	this->start_x = 0;
	this->start_y = 0;
	this->numLights = 0;

	memset(lights, 0, sizeof(int)*8);
	memset(models, 0, sizeof(int)*256);
}

Level::~Level(void)
{
	FreeResources();
}

/************************************************************************/
/* Liberta os recursos usado pelo nivel                                 */
/************************************************************************/
void Level::FreeResources()
{
	for(int i = 0; i < size_x; i++)
	{
		if(colisionMap[i])
			delete[] colisionMap[i];
	}
	if(colisionMap)
		delete[] colisionMap;

	for(int i = 0; i < size_x; i++)
	{
		for(int j = 0; j < size_y; j++)
		{
			if(modelMap[i][j])
				delete modelMap[i][j];
		}
		if(modelMap)
			delete[] modelMap[i];
	}
	if(modelMap)
		delete[] modelMap;

	for(int k = 0; k < 256; k++)
	{
		if(Level::models[k])
		{
			if(Level::models[k]->animation)
				delete Level::models[k]->animation;
			delete Level::models[k];
		}
	}

	for(int o = 0; o < numLights; o++)
	{
		delete lights[o];
	}
}

/************************************************************************/
/* Retorna a lista de texturas                                          */
/************************************************************************/
std::map<string, int>& Level::getTextureList()
{
	return textures;
}

/************************************************************************/
/* Retorna a lista de materiais                                         */
/************************************************************************/
std::map<string, Material*>& Level::getMaterials()
{
	return material;
}

/**
 * Carrega os dados de um nivel caso seja possivel.
 */
int Level::LoadLevel()
{
	std::ifstream stream;
	int line = 0;

	stream.open(filename, std::ifstream::in);
	if(!stream.is_open())
	{
		return ERROR_FAILED_TO_LOAD_LEVEL;
	}

	string comando, s_x, s_y, s_mode;

	while(!stream.eof())
	{
		string buffer;

		stream >> comando;
		if(strcmp(comando.c_str(), "LIGHTS") == 0)
		{
			while (strcmp(comando.c_str(), "ENDLIGHTS"))
			{
				stream >> comando;
				if(strcmp(comando.c_str(), "LIGHT") == 0)
				{
					Light *light = new Light();
					while (strcmp(comando.c_str(), "ENDLIGHT")) 
					{
						stream >> comando;
						if(strcmp(comando.c_str(), "NAME:") == 0)
						{
							light->readName(stream);
						}
						if(strcmp(comando.c_str(), "EXPONENT:") == 0)
						{
							light->readExponent(stream);
						}
						if(strcmp(comando.c_str(), "CUTOFF:") == 0)
						{
							light->readCutOff(stream);
						}
						if(strcmp(comando.c_str(), "DIRECTION:") == 0)
						{
							light->readSpotDir(stream);
						}
						if(strcmp(comando.c_str(), "POSITION:") == 0)
						{
							light->readLightPos(stream);
						}
						if(strcmp(comando.c_str(), "LIGHT_ID:") == 0)
						{
							light->readId(stream);
						}
						if(strcmp(comando.c_str(), "TYPE:") == 0)
						{
							light->readType(stream);
						}
						if(strcmp(comando.c_str(), "SPECULAR:") == 0)
						{
							light->readSpecLight(stream);
						}
						if(strcmp(comando.c_str(), "DIFFUSE:") == 0)
						{
							light->readDiffLight(stream);
						}
						if(strcmp(comando.c_str(), "AMBIENT:") == 0)
						{
							light->readAmbLight(stream);
						}
					}
					lights[light->getId()] = light;
					numLights++;
				}
			}
		}

		if(strcmp(comando.c_str(), "MATERIALS") == 0)
		{
			while (strcmp(comando.c_str(), "ENDMATERIALS"))
			{
				stream >> comando;
				if(strcmp(comando.c_str(), "MATERIAL") == 0)
				{
					Material *material = new Material();
					while (strcmp(comando.c_str(), "ENDMATERIAL")) {
						stream >> comando;
						if(strcmp(comando.c_str(), "NAME:") == 0)
						{
							stream >> material->name;
						}
						if(strcmp(comando.c_str(), "SHININESS:") == 0)
						{
							stream >> material->shininess;
						}
						if(strcmp(comando.c_str(), "SPECULAR:") == 0)
						{
							stream >> material->specular[0];
							stream >> material->specular[1];
							stream >> material->specular[2];
							stream >> material->specular[3];
						}
						if(strcmp(comando.c_str(), "DIFFUSE:") == 0)
						{
							stream >> material->diffuse[0];
							stream >> material->diffuse[1];
							stream >> material->diffuse[2];
							stream >> material->diffuse[3];
						}
						if(strcmp(comando.c_str(), "AMBIENT:") == 0)
						{
							stream >> material->ambient[0];
							stream >> material->ambient[1];
							stream >> material->ambient[2];
							stream >> material->ambient[3];
						}
					}
					this->material[material->name] = material;
				}
			}
		}
		if(strcmp(comando.c_str(), "TEXTURES") == 0)
		{
			
			while(strcmp(comando.c_str(), "ENDTEXTURES") != 0)
			{
				stream >> comando;
				if(strcmp(comando.c_str(), "TEXTURE:") == 0)
				{
					string texture;
					stream >> texture;

					//carregar a textura
					string path = LEVEL_PATH;
					path += "/";
					path += texture.c_str();
					GLuint textura = Game::loadTexture((char*)path.c_str());
					textures[texture] = textura;
				}
			}
		}

		if(strcmp(comando.c_str(), "COLISION_MAP") == 0)
		{
			//ler o conteudo do mapa de colisao
			stream >> s_x >> s_y >> s_mode;

			int l_mode = atoi(s_mode.c_str());
			this->levelMode = l_mode;

			size_x = atoi(s_x.c_str());
			size_y = atoi(s_y.c_str());

			colisionMap = new int*[size_x];

			for(int i = 0; i < size_x; i++)
			{
				colisionMap[i] = new int[size_y];
			}

			for(int i = 0; i < size_x; i++)
			{
				for(int j = 0; j < size_y; j++)
				{
					colisionMap[i][j] = 0;
				}
			}

			//ler o mapa
			line = 0;
			stream >> comando;
			while(strcmp(comando.c_str(), "ENDCM") != 0)
			{
				ReadColisionContent((char *)comando.c_str(), line);
				line++;
				stream >> comando;
			}
		}

		if(strcmp(comando.c_str(), "MODEL_MAP") == 0)
		{
			modelMap = new Object**[size_x];
			
			for(int i = 0; i < size_x; i++)
			{
				modelMap[i] = new Object*[size_y];
			}

			for(int i = 0; i < size_x; i++)
			{
				for(int j = 0; j < size_y; j++)
				{
					modelMap[i][j] = NULL;
				}
			}

			//ler o mapa
			line = 0;
			stream >> comando;
			while(strcmp(comando.c_str(), "ENDMM") != 0)
			{
				ReadModelMapContent((char *)comando.c_str(), line);
				line++;
				stream >> comando;
			}
		}

		if(strcmp(comando.c_str(), "Model") == 0)
		{
			//ler o conteudo dos modelos/materiais
			Model *m  = new Model();

			while(strcmp(comando.c_str(), "ENDM") != 0)
			{
				stream >> comando;
				if(strcmp(comando.c_str(), "ID:") == 0)
				{
					char cc;
					stream >> cc;
					m->id = CHAR_TO_INT(cc);
				}
				if(strcmp(comando.c_str(), "FLAGS:") == 0)
				{
					stream >> m->flags;
				}

				if(strcmp(comando.c_str(), "BASE_MODEL_ID:") == 0)
				{
					string cc;
					stream >> cc;
					if(strcmp(cc.c_str(), "-1") == 0)
					{
						m->baseId = -1;
					}
					else
					{
						m->baseId = CHAR_TO_INT(cc.c_str()[0]);
					}
				}

				if(strcmp(comando.c_str(), "NAME:") == 0)
				{
					stream >> m->name;
				}

				if(strcmp(comando.c_str(), "ANIMATION") == 0)
				{
					m->animation = new ModelAnimation();
					while(strcmp(comando.c_str(), "ENDANIM") != 0)
					{
						stream >> comando;

						if(strcmp(comando.c_str(), "INCREMENT_X:") == 0)
						{
							stream >>m->animation->incrementX;
						}

						if(strcmp(comando.c_str(), "INCREMENT_Y:") == 0)
						{
							stream >>m->animation->incrementY;
						}

						if(strcmp(comando.c_str(), "INCREMENT_Z:") == 0)
						{
							stream >>m->animation->incrementZ;
						}
						if(strcmp(comando.c_str(), "DISTANCE_TO_MOVE_X:") == 0)
						{
							stream >>m->animation->distanceToMoveX;
						}

						if(strcmp(comando.c_str(), "DISTANCE_TO_MOVE_Y:") == 0)
						{
							stream >>m->animation->distanceToMoveY;
						
						}
						if(strcmp(comando.c_str(), "DISTANCE_TO_MOVE_Z:") == 0)
						{
							stream >>m->animation->distanceToMoveZ;
						}
					}
				}

				if(strcmp(comando.c_str(), "DATA") == 0)
				{
					while(strcmp(comando.c_str(), "ENDD") != 0)
					{
						stream >> comando;

						//modelo composto por n quads
						if(strcmp(comando.c_str(), "QUAD") == 0)
						{
							Quad *q = new Quad();
							while(strcmp(comando.c_str(), "ENDQ") != 0)
							{
								stream >> comando;
								if(strcmp(comando.c_str(), "MATERIAL:") == 0)
								{
									stream >> q->material;
								}
								if(strcmp(comando.c_str(), "TEXTURE:") == 0)
								{
									stream >> q->texture;
								}

								if(strcmp(comando.c_str(), "C:") == 0)
								{
									stream >> q->c.r >> q->c.g >> q->c.b;
								}

								if(strcmp(comando.c_str(), "UV1:") == 0)
								{
									stream >> q->uv[0].x >> q->uv[0].y;
								}

								if(strcmp(comando.c_str(), "UV2:") == 0)
								{
									stream >> q->uv[1].x >> q->uv[1].y;
								}

								if(strcmp(comando.c_str(), "UV3:") == 0)
								{
									stream >> q->uv[2].x >> q->uv[2].y;
								}

								if(strcmp(comando.c_str(), "UV4:") == 0)
								{
									stream >> q->uv[3].x >> q->uv[3].y;
								}

								if(strcmp(comando.c_str(), "N:") == 0)
								{
									stream >> q->normal.x >> q->normal.y >> q->normal.z;
								}

								if(strcmp(comando.c_str(), "TRANS_AJUSTADA:") == 0)
								{
									stream >> q->adjustment_translation.x >> q->adjustment_translation.y >> q->adjustment_translation.z;
								}

								if(strcmp(comando.c_str(), "R:") == 0)
								{
									stream >> q->angle >> q->rotation.x >> q->rotation.y >> q->rotation.z;
								}
								
								if(strcmp(comando.c_str(), "TRANS_ORIGEM:") == 0)
								{
									stream >> q->origin_translation.x >> q->origin_translation.y >> q->origin_translation.z;
								}

								if(strcmp(comando.c_str(), "V1:") == 0)
								{
									stream >> q->vertices[0].x >> q->vertices[0].y >> q->vertices[0].z;
								}

								if(strcmp(comando.c_str(), "V2:") == 0)
								{
									stream >> q->vertices[1].x >> q->vertices[1].y >> q->vertices[1].z;
								}

								if(strcmp(comando.c_str(), "V3:") == 0)
								{
									stream >> q->vertices[2].x >> q->vertices[2].y >> q->vertices[2].z;
								}

								if(strcmp(comando.c_str(), "V4:") == 0)
								{
									stream >> q->vertices[3].x >> q->vertices[3].y >> q->vertices[3].z;
								}
							}
							m->quads.push_back(q);
						}
					}
				}
			}
			//guardar  o modelo
			models[m->id] = m;
		}
	}

	stream.close();
	return ERROR_NONE;
}

/**
 * Obtem a posicao x do jogador na array do tabuleiro.
 * Apenas util para operacoes de conversao para coordenadas
 * do mundo opengl ou para cameras.
 */
int Level::getPlayerStartX()
{
	return this->start_x;
}

/**
 * Obtem a posicao y do jogador na array do tabuleiro.
 * Apenas util para operacoes de conversao para coordenadas
 * do mundo opengl ou para cameras.
 */
int Level::getPlayerStartY()
{
	return this->start_y;
}

Model *Level::getModel(int id)
{
	return models[id];
}

/**
 * Le o conteudo de cada linha do ficheiro.
 */
void Level::ReadColisionContent(char *buffer, int row)
{
	int column = 0;
	while(*buffer != '\0')
	{
		char c = *buffer;
		switch(c)
		{
		case 'B':
			colisionMap[column][row] = LEVEL_PATH_BLOCKED;
			break;
		case 'P':
			colisionMap[column][row] = LEVEL_PATH_PASSABLE;
			break;
		case 'R':
			colisionMap[column][row] = LEVEL_PATH_OBJECTIVE_RED;
			break;
		case 'V':
			colisionMap[column][row] = LEVEL_PATH_OBJECTIVE_GREEN;
			break;
		case 'A':
			colisionMap[column][row] = LEVEL_PATH_OBJECTIVE_BLUE;
			break;
		case 'S':
			colisionMap[column][row] = LEVEL_PATH_PLAYER_START;
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

/************************************************************************/
/* Cria objectos que estao associados a modelos em determinadas 
   posicoes do mapa                                                      */
/************************************************************************/
void Level::ReadModelMapContent(char *buffer, int row)
{
	int column = 0;
	while(*buffer != '\0')
	{
		char c = *buffer;

		if(this->models[ CHAR_TO_INT(c)])
		{
			modelMap[column][row] = new Object();
			modelMap[column][row]->setModel( this->models[ CHAR_TO_INT(c) ]);

			vector3 v;
			v.x = column * MAP_TILE_SIZE;
			v.y = row * MAP_TILE_SIZE;
			v.z = 0;
			modelMap[column][row]->setPosition(v);
			
			modelMap[column][row]->setFlags(this->models[ CHAR_TO_INT(c)]->flags);
			modelMap[column][row]->setOriginalPosition(v);
		}
		
		column++;
		buffer++;
	}
}

/**
 * Obtem a largura do tabuleiro que sera usado para
 * criar arrays bidimensionais.
 */
int Level::getTileSizeX()
{
	return this->size_x;
}

/**
 * Obtem a altura do tabuleiro que sera usado para
 * criar arrays bidimensionais.
 */
int Level::getTileSizeY()
{
	return this->size_y;
}

/**
 * Desenha o nivel.
 */
void Level::Update(int time)
{
	//draw dos modelos a partir do mapa de modelos
	for(int i = 0; i < size_y; i++)
	{
		for(int j = 0; j < size_x; j++)
		{
			if(this->modelMap[j][i])
			{
				modelMap[j][i]->Update(time);
			}
		}
	}
}

/**
 * Retorna a array que contem os dados do nivel.
 */
int ** Level::getColisionMap()
{
	return this->colisionMap;
}


/**
 *Verifica se as coordenadas dadas colidem com um
 *tile bloqueado.
 *Usa pixel color colision pelo backbuffer.
 *Lanca tambem os eventos que sejam detectados.
 */
bool Level::Collides(float x, float y)
{
	int tile_x = Common::roundToTile(x);
	int tile_y = Common::roundToTile(y);

	if(!Common::checkMapBounds(tile_x, tile_y)) return true;

	if(this->colisionMap[tile_x][tile_y] == LEVEL_PATH_BLOCKED)
	{
		return true;
	}
	else if(this->colisionMap[tile_x][tile_y] == LEVEL_PATH_OBJECTIVE_BLUE)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_BLUE);
	}
	else if(this->colisionMap[tile_x][tile_y] == LEVEL_PATH_OBJECTIVE_RED)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_RED);
	}
	else if( this->colisionMap[tile_x][tile_y] == LEVEL_PATH_OBJECTIVE_GREEN)
	{
		Common::getGame()->Event(EVENT_PLAYER_REACHED_GREEN);
	}
	return false;
}

/************************************************************************/
/* Lanca um evento aos nos mais proximos do jogador.                    */
/************************************************************************/
void Level::signalEventToNearByNodes(float x, float y, EVENT_TRIGGER event)
{
	int tile_x = Common::roundToTile(x);
	int tile_y = Common::roundToTile(y);

	//enviar o sinal
	//X + 1
	if(Common::checkMapBounds(tile_x + 1, tile_y))
	{
		if(modelMap[tile_x+1][tile_y])
			modelMap[tile_x+1][tile_y]->EventTrigger(event);
	}

	//X - 1
	if(Common::checkMapBounds(tile_x-1, tile_y))
	{
		if(modelMap[tile_x-1][tile_y])
			modelMap[tile_x-1][tile_y]->EventTrigger(event);
	}

	//Y + 1
	if(Common::checkMapBounds(tile_x, tile_y + 1))
	{
		if(modelMap[tile_x][tile_y+1])
			modelMap[tile_x][tile_y+1]->EventTrigger(event);
	}

	//Y - 1
	if(Common::checkMapBounds(tile_x , tile_y - 1))
	{
		if(modelMap[tile_x+1][tile_y-1])
			modelMap[tile_x+1][tile_y-1]->EventTrigger(event);
	}
}


/************************************************************************/
/* Retorna o mapa de modelos.                                           */
/************************************************************************/
Object ***Level::getModelMap()
{
	return modelMap;
}

int Level::getNumLights()
{
	return numLights;
}

Light * Level::getLight( int l )
{
	return lights[l];
}
