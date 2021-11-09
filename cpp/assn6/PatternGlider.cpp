#include "./PatternGlider.hpp"

#include <cstdint>

std::uint8_t PatternGlider::getSizeX() const
{
    return m_sizeX;
}

std::uint8_t PatternGlider::getSizeY() const
{
    return m_sizeY;
}

bool PatternGlider::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}