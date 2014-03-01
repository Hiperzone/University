#include "Common.h"

#ifndef OBJECT_H
#define OBJECT_H


enum OBJECT_FLAGS
{
	OBJECT_FLAG_NONE = 0x00,
	OBJECT_FLAG_BASE_MODEL_ORIGINAL_POSITION = 0x01
};

/************************************************************************/
/* Classe que contem os dados de cada modelo 3D no mundo                */
/************************************************************************/
class Object
{
public:
	Object(void);
	~Object(void) throw();

	virtual void Update(int time);

	void setModel(Model *m);
	Model *getModel();

	vector3 &getPosition();
	void setPosition(vector3 v);

	void setOriginalPosition(vector3 v);
	vector3 getOriginalPosition();

	void DrawModel(Model * m);
	void EventTrigger(EVENT_TRIGGER event);

	void setFlags(int flags);
	int getFlags();
	
private:
	Model *model;
	vector3 position;
	vector3 originalPosition;
	int flags;
	bool enabled;
	int currentEvent;
	float eventDistanceXRemaining;
	float eventDistanceYRemaining;
	float eventDistanceZRemaining;
}; 

#endif
