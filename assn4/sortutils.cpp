#include "sortutils.hpp"

#include <algorithm>
#include <chrono>
#include <cstdint>
#include <execution>
#include <iomanip>
#include <iostream>
#include <string>
#include <vector>

enum class Policy
{
    SEQ,
    PAR,
};

enum class ArrayKind
{
    RAW,
    STD,
    VEC,
};

void printPerformance(std::string name, std::chrono::milliseconds duration)
{
    using namespace std::chrono;
    std::cout << '\t' << std::setw(11) << std::left << name
              << ": " << std::setw(3) << duration.count() << " ms" << std::endl;
}

void initializeRawArrayFromStdArray(const SourceArray& source, int dest[])
{
    for (std::size_t i = 0; i < source.size(); i++)
    {
        dest[i] = source[i];
    }
}

void organPipeStdArray(SourceArray& data)
{
    for (std::size_t first = 0ul, second = data.size() - 1; first < second; first++, second--)
    {
        data[second] = data[first];
    }
}

template <typename Iter>
std::chrono::nanoseconds sort(Iter begin, Iter end, Policy policy)
{
    using namespace std::chrono;
    auto startTime = steady_clock::now();
    if (policy == Policy::SEQ)
    {
        std::sort(std::execution::seq, begin, end);
    }
    else
    {
        std::sort(std::execution::par, begin, end);
    }
    auto endTime = steady_clock::now();
    return endTime - startTime;
}

template <typename T>
void evaluateSingleArrangement(std::string name, T array, Policy policy, ArrayKind kind)
{
    using namespace std::chrono;
    auto total = nanoseconds(0);

    for (std::uint8_t i = 0; i < HOW_MANY_TIMES; i++)
    {
        switch (kind)
        {
            case ArrayKind::RAW:
            {
                int rawArray[HOW_MANY_ELEMENTS];
                initializeRawArrayFromStdArray(array, rawArray);
                total += sort(rawArray, rawArray + HOW_MANY_ELEMENTS, policy);
                break;
            }
            case ArrayKind::STD:
            {
                SourceArray copy = array;
                total += sort(copy.begin(), copy.end(), policy);
                break;
            }
            case ArrayKind::VEC:
            {
                std::vector<int> vec(array.begin(), array.end());
                total += sort(vec.begin(), vec.end(), policy);
                break;
            }
        }
    }

    std::cout << '\t' << std::setw(11) << std::left << name
              << ": " << std::setw(3) << duration_cast<milliseconds>(total).count() << " ms" << std::endl;
}

void evaluateRawArray(const SourceArray& random, const SourceArray& sorted,
                      const SourceArray& reversed, const SourceArray& organPipe,
                      const SourceArray& rotated)
{
    std::cout << "--- Raw Array Performance ---\n\n"
              << "Sequential\n";
    evaluateSingleArrangement("Random", random, Policy::SEQ, ArrayKind::RAW);
    evaluateSingleArrangement("Sorted", sorted, Policy::SEQ, ArrayKind::RAW);
    evaluateSingleArrangement("Reversed", reversed, Policy::SEQ, ArrayKind::RAW);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::SEQ, ArrayKind::RAW);
    evaluateSingleArrangement("Rotated", rotated, Policy::SEQ, ArrayKind::RAW);

    std::cout << "\nParallel\n";
    evaluateSingleArrangement("Random", random, Policy::PAR, ArrayKind::RAW);
    evaluateSingleArrangement("Sorted", sorted, Policy::PAR, ArrayKind::RAW);
    evaluateSingleArrangement("Reversed", reversed, Policy::PAR, ArrayKind::RAW);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::PAR, ArrayKind::RAW);
    evaluateSingleArrangement("Rotated", rotated, Policy::PAR, ArrayKind::RAW);
    std::cout << "\n";
}

void evaluateStdArray(const SourceArray& random, const SourceArray& sorted,
                      const SourceArray& reversed, const SourceArray& organPipe,
                      const SourceArray& rotated)
{
    std::cout << "--- std::array Performance ---\n\n"
              << "Sequential\n";
    evaluateSingleArrangement("Random", random, Policy::SEQ, ArrayKind::STD);
    evaluateSingleArrangement("Sorted", sorted, Policy::SEQ, ArrayKind::STD);
    evaluateSingleArrangement("Reversed", reversed, Policy::SEQ, ArrayKind::STD);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::SEQ, ArrayKind::STD);
    evaluateSingleArrangement("Rotated", rotated, Policy::SEQ, ArrayKind::STD);

    std::cout << "\nParallel\n";
    evaluateSingleArrangement("Random", random, Policy::PAR, ArrayKind::STD);
    evaluateSingleArrangement("Sorted", sorted, Policy::PAR, ArrayKind::STD);
    evaluateSingleArrangement("Reversed", reversed, Policy::PAR, ArrayKind::STD);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::PAR, ArrayKind::STD);
    evaluateSingleArrangement("Rotated", rotated, Policy::PAR, ArrayKind::STD);
    std::cout << "\n";
}

void evaluateStdVector(const SourceArray& random, const SourceArray& sorted,
                       const SourceArray& reversed,
                       const SourceArray& organPipe,
                       const SourceArray& rotated)
{

    std::cout
        << "--- std::vector Performance ---\n\n"
        << "Sequential\n";
    evaluateSingleArrangement("Random", random, Policy::SEQ, ArrayKind::VEC);
    evaluateSingleArrangement("Sorted", sorted, Policy::SEQ, ArrayKind::VEC);
    evaluateSingleArrangement("Reversed", reversed, Policy::SEQ, ArrayKind::VEC);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::SEQ, ArrayKind::VEC);
    evaluateSingleArrangement("Rotated", rotated, Policy::SEQ, ArrayKind::VEC);

    std::cout << "\nParallel\n";
    evaluateSingleArrangement("Random", random, Policy::PAR, ArrayKind::VEC);
    evaluateSingleArrangement("Sorted", sorted, Policy::PAR, ArrayKind::VEC);
    evaluateSingleArrangement("Reversed", reversed, Policy::PAR, ArrayKind::VEC);
    evaluateSingleArrangement("Organ Pipe", organPipe, Policy::PAR, ArrayKind::VEC);
    evaluateSingleArrangement("Rotated", rotated, Policy::PAR, ArrayKind::VEC);
    std::cout << "\n";
}
