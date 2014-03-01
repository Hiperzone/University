#include "Common.h"

#ifndef PLAYER_H
#define PLAYER_H


class Player
{
public:
	Player(void);
	~Player(void);

	int getObjectiveState();
	void setObjectiveState(int state);
	void setPosition(float x, float y, float z);
	void setPosition(vector3 pos);
	void setColor(float r, float g, float b);
	vector3& getPosition();
	color& getColor();
	void Update();
private:
	int objectiveState;
	vector3 position;
	color color;
};

#endif
