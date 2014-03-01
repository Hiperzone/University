#include "Common.h"


Object::Object(void)
{
	this->textureId = -1;
	memset(&rectCoords, 0, sizeof(rectCoords));
	this->setPosition(0, 0, 0);
}


Object::~Object(void)
{
}

void Object::setColor( float r, float g, float b)
{
	color.r = r;
	color.g = g;
	color.b = b;
}

RGB_COLOR& Object::getColor()
{
	return this->color;
}


int Object::getTextureId()
{
	return this->textureId;
}

void Object::setTextureId(int tid)
{
	this->textureId = tid;
}

vector3& Object::getPosition()
{
	return this->position;
}

void Object::setPosition(vector3 v)
{
	this->position = v;
}

void Object::setPosition(float x, float y, float z)
{
	position.x = x;
	position.y = y;
	position.z = z;
}

void Object::setRect(float x1, float y1, float x2, float y2)
{
	rectCoords.x1 = x1;
	rectCoords.x2 = x2;
	rectCoords.y1 = y1;
	rectCoords.y2 = y2;
}

GLRECT& Object::getRect()
{
	return this->rectCoords;
}

void Object::Update()
{

}
