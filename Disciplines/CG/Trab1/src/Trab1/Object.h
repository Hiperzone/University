#include "Common.h"

#ifndef OBJECT_H
#define OBJECT_H



class Object
{
public:
	Object(void);
	~Object(void);

	bool collidesWith(Object * o);
	vector3& getPosition();
	void setPosition(vector3 v);
	void setPosition(float x, float y, float z);

	int getTextureId();
	void setTextureId(int tid);

	void setRect(float x1, float y1, float x2, float y2);
	GLRECT& getRect();

	void setColor( float r, float g, float b);
	RGB_COLOR& getColor();

	virtual void Update();

protected:
	RGB_COLOR color;
	GLRECT rectCoords;
	vector3 position;
	int textureId;
};

#endif
