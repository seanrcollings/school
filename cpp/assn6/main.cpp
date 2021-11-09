#include "LifeSimulator.hpp"
#include "PatternAcorn.hpp"
#include "PatternBlinker.hpp"
#include "PatternBlock.hpp"
#include "PatternGlider.hpp"
#include "PatternGosperGliderGun.hpp"
#include "RenderConsole.hpp"
#include "rlutil.h"

#include <array>
#include <iostream>

int main()
{
    LifeSimulator simul{ rlutil::tcols(), rlutil::trows() };
    PatternBlinker pattern;
    PatternAcorn aPattern;
    PatternGlider gPattern;
    PatternGosperGliderGun ggPattern;
    PatternBlock bPattern;
    simul.insertPattern(bPattern, 50, 30);
    RenderConsole renderer;
    renderer.render(simul);
    return 0;
}