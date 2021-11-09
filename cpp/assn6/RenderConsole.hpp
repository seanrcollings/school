#include "./Renderer.hpp"

#include <array>

class RenderConsole : public Renderer
{
  public:
    void render(const LifeSimulator& simulation);

  private:
    bool firstRender = true;
    std::vector<std::vector<bool>> lastState;

    void setup(const LifeSimulator& simulation);
};