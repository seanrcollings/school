#include "./PatternAcorn.hpp"

#include <cstdint>

std::uint8_t PatternAcorn::getSizeX() const
{
    return m_sizeX;
}

std::uint8_t PatternAcorn::getSizeY() const
{
    return m_sizeY;
}

bool PatternAcorn::getCell(std::uint8_t x, std::uint8_t y) const
{
    return board[y][x];
}