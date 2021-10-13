#include "sortutils.hpp"

#include <algorithm>
#include <array>
#include <iostream>
#include <random>
#include <vector>

void printArray(SourceArray& array)
{
    for (size_t i = 0; i < array.size(); i++)
    {
        std::cout << array[i] << " ";
    }
}

int main()
{
    std::random_device device;
    std::mt19937 engine(device());
    std::uniform_int_distribution<int> random(-10'000'000, 10'000'000);

    SourceArray sourceArray;
    for (size_t i = 0; i < HOW_MANY_ELEMENTS; i++)
    {
        sourceArray[i] = random(engine);
    }

    auto randomArray = sourceArray;
    auto sorted = sourceArray;
    std::sort(sorted.begin(), sorted.end());
    auto reversed = sorted;
    std::reverse(reversed.begin(), reversed.end());
    auto organPipe = sorted;
    organPipeStdArray(organPipe);
    auto rotated = sorted;
    std::rotate(rotated.begin(), rotated.begin() + 1, rotated.end());

    evaluateRawArray(randomArray, sorted, reversed, organPipe, rotated);
    evaluateStdArray(randomArray, sorted, reversed, organPipe, rotated);
    evaluateStdVector(randomArray, sorted, reversed, organPipe, rotated);
    return 0;
}