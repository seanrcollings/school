#include "./PatternBlinker.hpp"

#include <cstdint>

std::uint8_t PatternBlinker::getSizeX() const
{
    return m_sizeX;
}

std::uint8_t PatternBlinker::getSizeY() const
{
    return m_sizeY;
}

bool PatternBlinker::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}