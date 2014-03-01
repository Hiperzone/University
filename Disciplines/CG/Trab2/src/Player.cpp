#include "Common.h"


Player::Player(void)
{
	this->objectiveState = EVENT_NONE;

	//bounding box
	bBox.p1.x = 0;
	bBox.p1.y = 0;

	bBox.p2.x = 0;
	bBox.p2.y = 0.2;

	bBox.p3.x = 0.2;
	bBox.p3.y = 0;

	bBox.p4.x = 0.2;
	bBox.p4.y = 0.2;
	front = 0;
}

Player::~Player(void)
{
}

/************************************************************************/
/* Testa se existe colisoes usando a Bounding box                       */
/************************************************************************/
bool Player::Collides(float x, float y)
{
	if(Common::getGame()->getLevel())
	{
		if(Common::getGame()->getLevel()->Collides(x + bBox.p1.x, y + bBox.p1.y))
			return true;
		else if(Common::getGame()->getLevel()->Collides(x + bBox.p2.x, y + bBox.p2.y))
			return true;
		else if(Common::getGame()->getLevel()->Collides(x + bBox.p3.x, y + bBox.p3.y))
			return true;
		else if(Common::getGame()->getLevel()->Collides(x + bBox.p4.x, y + bBox.p4.y))
			return true;

		return false;
	}
	return false;
}

/**
 * Retorna o estado dos objectivos alcancados pelo
 * jogador.
 *
 */
int Player::getObjectiveState()
{
	return this->objectiveState;
}

/**
 * Actualiza os objectivos alcancados pelo jogador.
 *
 */
void Player::setObjectiveState(int state)
{
	this->objectiveState = state;
}

/**
 * Retorna a posicao actual do jogador.
 *
 */
vector3& Player::getPosition()
{
	return position;
}

/**
 * Retorna a cor do jogador.
 *
 */
color& Player::getColor()
{
	return color;
}

/**
 * Atribui a posicao do jogador.
 *
 */
void Player::setPosition(vector3 pos)
{
	position.x = pos.x;
	position.y = pos.y;
}

/**
 * Atribui a posicao do jogador.
 *
 */
void Player::setPosition(float x, float y, float z)
{
	position.x = x;
	position.y = y;
	position.z = z;
}

/**
 * Atribui a cor do jogador.
 *
 */
void Player::setColor(float r, float g, float b)
{
	color.r = r;
	color.g = g;
	color.b = b;
}

/**
 * Define qual é a frente do jogador
 */
void Player::setFront(int front)
{
    this->front = front;
}

/**
 * Retorna para onde o jogador está virado
 */
int Player::getFront()
{
    return front;
}

/**
 * Actualiza os dados do jogador.
 * Permite desenhar o jogador no mundo.
 *
 */
void Player::Update()
{
	//cores do material
	static GLfloat A[] = {0, 0.0, 0.0, 1.0};
	static GLfloat D[] = {1, 1.0, 1.0, 1.0};
	static GLfloat S[] = {1.0, 1.0, 1.0, 1.0};

	glDisable(GL_TEXTURE_2D);
	// Reflexo da cor ambiente
	glMaterialfv(GL_FRONT, GL_AMBIENT, A);
	// Reflexo da cor difusa
	glMaterialfv(GL_FRONT, GL_DIFFUSE, D);
	// Reflexo da cor espcular
	glMaterialfv(GL_FRONT, GL_SPECULAR, S);

	glMaterialf(GL_FRONT, GL_SHININESS, 60.0);
	glPushMatrix();
	glTranslatef(this->position.x, this->position.y, this->position.z);
	Common::getGame()->drawCube(0.1, 0.1, 0.1, 0.1);
	glPopMatrix();
	glEnable(GL_TEXTURE_2D);
}

/************************************************************************/
/* Detecta rampas e actualiza a posicao em Z automaticamente            */
/************************************************************************/
void Player::UpdateZPosition(int dir)
{
	static float Z_CALC = 0.1f;

	//detectar se o jogador esta numa rampa
	int tile_x = Common::roundToTile(position.x);
	int tile_y = Common::roundToTile(position.y);

	if(!Common::checkMapBounds(tile_x, tile_y))
	{
		return;
	}

	Object *m = Common::getGame()->getLevel()->getModelMap()[tile_x][tile_y];
	if(m)
	{
		if (m->getModel())
		{
			if((m->getModel()->id == 40) && dir == DIRECTION_Y_NEGATIVE)
			{
				position.z+= Z_CALC;
			}
			if((m->getModel()->id == 40) && dir == DIRECTION_Y_POSITIVE)
			{
				position.z-= Z_CALC;
			}
				
		}
	}
	//printf("%f %f %f %i %i\n", position.x, position.y, position.z, tile_x, tile_y);
}
