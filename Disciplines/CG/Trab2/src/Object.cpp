#include "Common.h"

Object::Object(void)
{
	model = NULL;
	currentEvent = EVENT_TRIGGER_NO_ACTION;
	eventDistanceXRemaining = 0.0f;
	eventDistanceYRemaining = 0.0f;
	eventDistanceZRemaining = 0.0f;
	enabled = true;
	flags = 0;
}

Object::~Object(void)
{
}

/************************************************************************/
/* Retorna o modelo associado a este objecto                            */
/************************************************************************/
Model * Object::getModel()
{
	return model;
}

/************************************************************************/
/* Atribui o modelo associado a este objecto                            */
/************************************************************************/
void Object::setModel(Model *m)
{
	this->model = m;
}

/************************************************************************/
/* Retorna a posicao do objecto                                         */
/************************************************************************/
vector3 &Object::getPosition()
{
	return position;
}

/************************************************************************/
/* Atribui a posicao do objecto                                         */
/************************************************************************/
void Object::setPosition(vector3 v)
{
	position = v;
}

/************************************************************************/
/* Evoca um evento                                                      */
/************************************************************************/
void Object::EventTrigger(EVENT_TRIGGER event)
{
	if(enabled && Common::getGame()->getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_GREEN)
	{
		currentEvent = event;
	}
}

/************************************************************************/
/* Atribui a posicao original do objecto                                */
/************************************************************************/
void Object::setOriginalPosition(vector3 v)
{
	originalPosition = v;
}

/************************************************************************/
/* Retorna a posicao original do objecto                                */
/************************************************************************/
vector3 Object::getOriginalPosition()
{
	return originalPosition;
}

/************************************************************************/
/* Faz o draw dos modelos associados a este objecto                     */
/************************************************************************/
void Object::DrawModel( Model * m)
{
	assert(m != NULL);
	if(m->baseId >= 0)
	{
		DrawModel(Common::getGame()->getLevel()->getModel(m->baseId));
	}

	//trigger de eventos
	//EVENTO: ABRIR PORT
	if(currentEvent == EVENT_TRIGGER_OPEN && Common::getGame()->getPlayer()->getObjectiveState() == EVENT_PLAYER_REACHED_GREEN)
	{
		//este objecto tem modelo.
		if(m->animation && enabled )
		{
			//existe movimento
			//mover o objecto se ainda nao alcancou o destino
			if(this->eventDistanceXRemaining < m->animation->distanceToMoveX)
			{
				position.x += m->animation->incrementX;
				eventDistanceXRemaining += abs(m->animation->incrementX);
			}
			else if(this->eventDistanceYRemaining < m->animation->distanceToMoveY)
			{
				position.y += m->animation->incrementY;
				eventDistanceYRemaining += abs(m->animation->incrementY);
			}
			else if(this->eventDistanceZRemaining < m->animation->distanceToMoveZ)
			{
				position.z += m->animation->incrementZ;
				eventDistanceZRemaining += abs(m->animation->incrementZ);
			}
			else
			{
				//objecto alcancou o destino
				//remover o evento
				currentEvent = EVENT_TRIGGER::EVENT_TRIGGER_NO_ACTION;
				enabled = false;
				int tile_x = Common::roundToTile(originalPosition.x);
				int tile_y = Common::roundToTile(originalPosition.y);

				Common::getGame()->getLevel()->getColisionMap()[tile_x][tile_y] = LEVEL_PATH_PASSABLE;
			}
		}
	}

	//http://support.microsoft.com/kb/131130  13-6-2013
	for( std::list<Quad*>::iterator itr = m->quads.begin(); itr != m->quads.end(); itr++)
	{
		Quad *quad = *itr;
		
		glPushMatrix();
		if(!m->animation && flags & OBJECT_FLAG_BASE_MODEL_ORIGINAL_POSITION)
			glTranslatef(originalPosition.x, originalPosition.y, originalPosition.z);
		else
			glTranslatef(position.x, position.y, position.z);
		
		//translacao para a posicao no mapa
		VECTOR3 v = quad->origin_translation.absolute();
		glTranslatef(v.x, v.y, v.z);
		
		//stranslacao para ajustamentos apos a rotacao
		glTranslatef(quad->adjustment_translation.x, quad->adjustment_translation.y, quad->adjustment_translation.z);
		//rotacao em torno da origem
		glRotatef(quad->angle, quad->rotation.x, quad->rotation.y, quad->rotation.z);
		//translacao para a origem
		glTranslatef(quad->origin_translation.x, quad->origin_translation.y, quad->origin_translation.z);


		if(strcmp(quad->material.c_str(), "") != 0)
		{
			Material *material = Common::getGame()->getLevel()->getMaterials()[quad->material];
			// Reflexo da cor ambiente
			glMaterialfv(GL_FRONT, GL_AMBIENT, material->ambient);
			// Reflexo da cor difusa
			glMaterialfv(GL_FRONT, GL_DIFFUSE, material->diffuse);
			// Reflexo da cor espcular
			glMaterialfv(GL_FRONT, GL_SPECULAR, material->specular);
			// Intensidade da cor especular [0..128]
			glMaterialf(GL_FRONT, GL_SHININESS, material->shininess);
		}

		if(strcmp(quad->texture.c_str(), "") != 0)
		{
			glBindTexture(GL_TEXTURE_2D, Common::getGame()->getLevel()->getTextureList()[quad->texture]);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); //por defeito GL_NEAREST_MIPMAP_LINEAR
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); //por defeito GL_LINEAR
		}

		glColor3f(quad->c.r, quad->c.g, quad->c.b);
		glBegin(GL_QUADS);
			glNormal3f(quad->normal.x, quad->normal.y, quad->normal.z);

			glTexCoord2f(quad->uv[0].x, quad->uv[0].y);
			glVertex3f(quad->vertices[0].x, quad->vertices[0].y, quad->vertices[0].z);
			glTexCoord2f(quad->uv[1].x, quad->uv[1].y);
			glVertex3f(quad->vertices[1].x, quad->vertices[1].y, quad->vertices[1].z);
			glTexCoord2f(quad->uv[2].x, quad->uv[2].y);
			glVertex3f(quad->vertices[2].x, quad->vertices[2].y, quad->vertices[2].z);
			glTexCoord2f(quad->uv[3].x, quad->uv[3].y);
			glVertex3f(quad->vertices[3].x, quad->vertices[3].y, quad->vertices[3].z);
		glEnd();
		
		glPopMatrix();
	}
}

/************************************************************************/
/* Actualiza o objecto                                                  */
/************************************************************************/
void Object::Update(int time)
{
	if(model)
		DrawModel(model);
}

void Object::setFlags(int flags)
{
	this->flags = flags;
}
int Object::getFlags()
{
	return flags;
}
