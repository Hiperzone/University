
#ifndef STRUCTS_H
#define STRUCTS_H
#include "Common.h"

typedef struct COLOR
{
	float r, g, b;

	COLOR() : r(0.0), g(0.0), b(0.0) {}
}color;



typedef struct Vector2
{
	float x, y;

	Vector2() : x(0.0f), y(0.0) {}
}VECTOR2;



typedef struct BoundingBox
{
	VECTOR2 p1, p2, p3, p4;
}BOUNDING_BOX;


typedef struct VECTOR3
{
	float x;
	float y;
	float z;
	VECTOR3() : x(0.0), y(0.0), z(0.0) {}

	float dot(VECTOR3 v)
	{
		return v.x*x + v.y*y + v.z*z;
	}

	VECTOR3 normalize() {
		float norma = norm();
		VECTOR3 v1;
		v1.x = x / norma;
		v1.y = y / norma;
		v1.z = z / norma;
		return v1;
	}

	VECTOR3 absolute()
	{
		VECTOR3 v1;
		v1.x = abs(x);
		v1.y = abs(y);
		v1.z = abs(z);
		return v1;
	}

	float norm() { return sqrt( x*x + y*y + z*z); }

	static VECTOR3 cross( VECTOR3 v1, VECTOR3 v2, VECTOR3 v3)
	{
		VECTOR3 v;
		float qx = v2.x-v1.x;
		float qy = v2.y-v1.y;
		float qz = v2.z-v1.z;

		float px = v3.x - v1.x;
		float py = v3.y - v1.y;
		float pz = v3.z - v1.z;

		v.x = py*qz - pz*qy;
		v.y = pz*qx - px*qz;
		v.z = px*qy - py*qx;
		return v;
	}
}vector3;

struct Quad
{
	string texture;
	color c;
	vector3 uv[4];
	vector3 normal;
	vector3 vertices[4];
	vector3 rotation;
	vector3 origin_translation;
	vector3 adjustment_translation;
	string material;
	float angle;

	Quad() : angle(0.0) {}
};


struct ModelAnimation
{
	int triggerEvent;
	int timeToLive;
	float incrementX;
	float incrementY;
	float incrementZ;
	float distanceToMoveX;
	float distanceToMoveY;
	float distanceToMoveZ;

	ModelAnimation() : triggerEvent(EVENT_NONE), timeToLive(0), 
		incrementX(0.0), incrementY(0.0), incrementZ(0.0),
		distanceToMoveX(0.0), distanceToMoveY(0.0), distanceToMoveZ(0.0) {}
};



struct Model
{
	int id;
	int baseId;
	string name;
	std::list<Quad*> quads;
	ModelAnimation *animation;
	int flags;


	Model() : id(0), baseId(0), flags(0), name("default"),
		 animation(NULL) {}

	~Model()
	{

		for( std::list<Quad*>::iterator itr = quads.begin(); itr != quads.end(); itr++)
		{
			Quad *quad = *itr;
			if(quad)
				delete quad;
		}
		quads.clear();
	}
};

struct Material
{
	string name;
	float shininess;
	float specular[4];
	float diffuse[4];
	float ambient[4];
};


#endif
