#include "./LifeSimulator.hpp"

#include <iostream>
#include <numeric>

LifeSimulator::LifeSimulator(std::uint8_t sizeX, std::uint8_t sizeY) :
    m_sizeX(sizeX),
    m_sizeY(sizeY)
{
    board = {};
    board.resize(sizeY);
    for (auto i = board.begin(); i != board.end(); i++)
    {
        i->resize(sizeX);
    }
}

std::uint8_t LifeSimulator::getSizeX() const
{
    return m_sizeX;
}
std::uint8_t LifeSimulator::getSizeY() const
{
    return m_sizeY;
}

bool LifeSimulator::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}

std::vector<bool> LifeSimulator::getNeighbors(uint8_t x, std::uint8_t y) const
{
    // Will fail if x or y is on the edge of the board
    std::vector<bool> neighbors;
    // return {
    //     board[y - 1][x - 1],
    //     board[y - 1][x],
    //     board[y - 1][x + 1],
    //     board[y][x - 1],
    //     board[y][x + 1],
    //     board[y + 1][x - 1],
    //     board[y + 1][x],
    //     board[y + 1][x + 1],
    // };

    if (y > 0)
    {
        neighbors.push_back(board[y - 1][x]);
        if (x > 0)
        {
            neighbors.push_back(board[y - 1][x - 1]);
        }
        if (x < getSizeX() - 1)
        {
            neighbors.push_back(board[y - 1][x + 1]);
        }
    }

    if (x > 0)
        neighbors.push_back(board[y][x - 1]);
    if (x < getSizeX())
        neighbors.push_back(board[y][x + 1]);

    if (y < getSizeY() - 1)
    {
        neighbors.push_back(board[y + 1][x]);
        if (x > 0)
        {
            neighbors.push_back(board[y + 1][x - 1]);
        }
        if (x < getSizeX() - 1)
        {
            neighbors.push_back(board[y + 1][x + 1]);
        }
    }
    return neighbors;
}

void LifeSimulator::insertPattern(const Pattern& pattern, std::uint8_t startX, std::uint8_t startY)
{
    auto currY = startY;
    auto currX = startX;
    auto patternHeight = pattern.getSizeY();
    auto patternWidth = pattern.getSizeX();

    for (std::uint8_t i = 0; i < patternHeight; i++)
    {
        for (std::uint8_t j = 0; j < patternWidth; j++)
        {
            board[currY][currX] = pattern.getCell(j, i);
            currX++;
        }
        currX = startX;
        currY++;
    }
}

void LifeSimulator::update()
{
    // Makes a copy of the entire board
    auto nextState = board;
    auto simHeight = getSizeY();
    auto simWidth = getSizeX();

    for (std::uint8_t i = 0; i < simHeight; ++i)
    {
        for (std::uint8_t j = 0; j < simWidth; j++)
        {
            auto neighbors = getNeighbors(j, i);
            auto aliveNeighbors = std::accumulate(neighbors.begin(), neighbors.end(), 0);
            auto alive = board[i][j];

            if (alive && (aliveNeighbors < 2 || aliveNeighbors > 3))
            {
                nextState[i][j] = false;
            }
            // else if (alive && (aliveNeighbors == 2 || aliveNeighbors == 3))
            // {
            //     nextState[i][j] = true;
            // }
            else if (!alive && aliveNeighbors == 3)
            {
                nextState[i][j] = true;
            }
        }
    }
    board = nextState;
}