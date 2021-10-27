#include "WordTree.hpp"

#include <iostream>
#include <memory>
#include <queue>
#include <vector>

// Character 'a' is ASCII char 97
// By subtracting 97 from the given char
// We can get it's position in the children array
const int ALPHA_OFFSET = 97;

void WordTree::add(std::string word)
{
    auto curr = m_root;

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
            next = std::make_shared<Node>(c);
            curr = next;
        }
    }

    curr->endOfWord = true;
    m_size++;
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
    // Should this even check endOfWord, or just that
    // it exists?
    return curr ? curr->endOfWord : false;
}

void bfs(std::queue<std::shared_ptr<Node>> queue, std::string partial, std::vector<std::string>& words)
{
    if (queue.empty())
    {
        return;
    }

    auto node = queue.front();
    queue.pop();

    for (std::shared_ptr<Node> child : node->children)
    {
        if (child)
        {
            if (child->endOfWord)
            {
                words.push_back(partial + child->character);
            }
            queue.push(child);
        }
    }

    bfs(queue, partial + node->character, words);
}

std::vector<std::string> WordTree::predict(std::string partial, std::uint8_t howMany)
{
    auto vec = std::vector<std::string>();
    auto curr = traverse(partial);

    std::queue<std::shared_ptr<Node>> queue;
    queue.push(curr);

    bfs(queue, partial, vec);

    return vec;
}

std::size_t WordTree::size()
{
    return m_size;
}