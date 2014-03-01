#include "Common.h"


Player::Player(void)
{
	this->objectiveState = EVENT_NONE;
}


Player::~Player(void)
{
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
	position.z = pos.z;
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
 * Actualiza os dados do jogador.
 * Permite desenhar o jogador no mundo.
 *
 */
void Player::Update()
{
	float radius = 0.1f;
	
	//draw aqui
	//draw do circulo
	glPushMatrix();
	glColor3f(1.0, 1.0, 1.0);
	glTranslatef(this->position.x, this->position.y, 0.0f);
	glBegin(GL_POLYGON);
	for(float i = 0; i < (2 * PI); i += PI / 24)
		glVertex3f(cos(i) * radius + 0.1,  sin(i) * radius + 0.1, 0.0);
	glEnd();
	glPopMatrix();
}