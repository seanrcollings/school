#include "./LifeSimulator.hpp"

class Renderer
{
  public:
    virtual void render(LifeSimulator& simulation) = 0;
};