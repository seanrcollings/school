#pragma once
#include <algorithm>
#include <array>
#include <cstdint>
#include <memory>
#include <string>
#include <vector>

struct Node
{
    bool endOfWord;
    std::array<std::shared_ptr<Node>, 26> children;

    Node()
    {
        endOfWord = false;
        std::fill(children.begin(), children.end(), nullptr);
    }
};

class WordTree
{
  public:
    void add(std::string word);
    bool find(std::string word);
    std::vector<std::string> predict(std::string partial, std::uint8_t howMany);
    std::size_t size();

    WordTree()
    {
        m_size = 0;
        m_root = std::make_shared<Node>();
    }

  private:
    std::size_t m_size;
    std::shared_ptr<Node> m_root;

    std::shared_ptr<Node> traverse(std::string str);
};
