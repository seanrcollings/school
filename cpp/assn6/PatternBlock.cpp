#include "./PatternBlock.hpp"

#include <cstdint>

std::uint8_t PatternBlock::getSizeX() const
{
    return m_sizeX;
}

std::uint8_t PatternBlock::getSizeY() const
{
    return m_sizeY;
}

bool PatternBlock::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}