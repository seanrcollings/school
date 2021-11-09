#include "LifeSimulator.hpp"
#include "PatternAcorn.hpp"
#include "PatternBlinker.hpp"
#include "RenderConsole.hpp"
#include "rlutil.h"

#include <array>
#include <iostream>

int main()
{
    LifeSimulator simul{ rlutil::tcols(), rlutil::trows() };
    PatternBlinker pattern;
    PatternAcorn bpattern;
    // simul.insertPattern(pattern, 50, 30);
    simul.insertPattern(bpattern, 50, 30);
    RenderConsole renderer;
    renderer.render(simul);
    return 0;
}