#include "WordTree.hpp"

#include <iostream>
#include <memory>
#include <queue>
#include <utility>
#include <vector>

using NodePair = std::pair<std::shared_ptr<Node>, std::string>;
using NodeQueue = std::queue<NodePair>;

// Character 'a' is ASCII char 97
// By subtracting 97 from the given char
// We can get it's position in the children array
const int ALPHA_OFFSET = 97;

void WordTree::add(std::string word)
{
    auto curr = m_root;
    bool addedWord = false;
    for (char c : word)
    {
        int pos = c - ALPHA_OFFSET;
        auto& next = curr->children[pos];

        // Character already inserted into the tree
        if (next)
        {
            curr = next;
        }
        // Character needs to be inserted into the tree
        else
        {
            next = std::make_shared<Node>();
            curr = next;
            addedWord = true;
        }
    }

    curr->endOfWord = true;
    if (addedWord)
    {
        m_size++;
    }
}

std::shared_ptr<Node> WordTree::traverse(std::string str)
{
    auto curr = m_root;

    for (char const& c : str)
    {
        // Early return if we hit a nullptr
        if (!curr)
        {
            return curr;
        }
        int pos = c - ALPHA_OFFSET;
        curr = curr->children[pos];
    }

    return curr;
}

bool WordTree::find(std::string word)
{
    auto curr = traverse(word);
    return curr ? curr->endOfWord : false;
}

std::vector<std::string> WordTree::predict(std::string partial, std::uint8_t howMany)
{
    auto words = std::vector<std::string>();
    auto root = traverse(partial);
    if (partial.size() == 0 || !root)
    {
        return words;
    }

    // Breadth First Search
    NodeQueue queue;
    queue.push(NodePair{ root, partial });

    while (!queue.empty() && words.size() < howMany)
    {
        auto pair = queue.front();
        auto node = pair.first;
        auto partial = pair.second;
        queue.pop();

        for (std::size_t i = 0; i < node->children.size(); i++)
        {
            auto child = node->children[i];
            if (child)
            {
                if (child->endOfWord)
                {
                    words.push_back(partial + static_cast<char>(i + ALPHA_OFFSET));
                }
                queue.push(NodePair{ child, partial + static_cast<char>(i + ALPHA_OFFSET) });
            }
        }
    }

    return words;
}

std::size_t WordTree::size()
{
    return m_size;
}