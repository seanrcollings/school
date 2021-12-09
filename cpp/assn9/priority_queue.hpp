#include <cstddef>
#include <cstdint>
#include <exception>
#include <initializer_list>
#include <iostream>
#include <iterator>
#include <string>
#include <utility>
#include <vector>

#define LCHILD(x) 2 * x + 1
#define RCHILD(x) 2 * x + 2
#define PARENT(x) (x - 1) / 2

namespace usu
{

    template <typename V, typename P = unsigned int>
    class priority_queue
    {
        using priority_type = P;
        using value_type = V;
        using size_type = size_t;

      public:
        priority_queue() :
            m_size(0)
        {
        }

        priority_queue(std::initializer_list<std::pair<V, P>> list) :
            m_size(0)
        {
            for (auto& p : list)
            {
                enqueue(p.first, p.second);
            }
        }

        struct Item
        {
            V value;
            P priority;
            Item()
            {
            }

            Item(V value, P priority)
            {
                this->value = value;
                this->priority = priority;
            }
        };

        // According to https://internalpointers.com/post/writing-custom-iterators-modern-cpp
        // This is all the iterator needs to be valid.
        class iterator : public std::iterator<std::forward_iterator_tag, priority_queue>
        {

          public:
            iterator() :
                m_ptr(nullptr) {}

            iterator(Item* ptr) :
                m_ptr(ptr) {}

            Item& operator*() const
            {
                return *m_ptr;
            }

            Item* operator->() const { return m_ptr; }

            iterator& operator++()
            {
                m_ptr++;
                return *this;
            }

            iterator operator++(int)
            {
                iterator tmp = *this;
                ++(*this);
                return tmp;
            }

            friend bool operator==(const iterator& a, const iterator& b) { return a.m_ptr == b.m_ptr; };
            friend bool operator!=(const iterator& a, const iterator& b) { return a.m_ptr != b.m_ptr; };
            friend int operator-(const iterator& a, const iterator& b) { return a.m_ptr - b.m_ptr; }

          private:
            Item* m_ptr;
        };

        void enqueue(V value, P priority);
        Item dequeue();
        iterator begin();
        iterator end();
        iterator find(V value);
        void update(iterator i, P priority);
        bool empty() const;
        size_type size() const;

      private:
        std::vector<Item> heap;
        size_type m_size;

        void heapify(int i);
        void swap(Item* i1, Item* i2);
    };

    // Heap Actions ------------------------------------------------
    template <typename V, typename P>
    void priority_queue<V, P>::enqueue(V value, P priority)
    {

        if (m_size == heap.capacity())
        {
            heap.resize(m_size + (m_size * 1.25) + 1);
        }

        Item item{ value, priority };
        int i = m_size++;
        while (i && item.priority > heap[PARENT(i)].priority)
        {
            heap[i] = heap[PARENT(i)];
            i = PARENT(i);
        }

        heap[i] = item;
    }

    template <typename V, typename P>
    priority_queue<V, P>::Item priority_queue<V, P>::dequeue()
    {
        if (m_size == 0)
        {
            throw std::runtime_error("queue empty");
        }

        Item first = heap[0];
        heap[0] = heap[--m_size];
        heapify(0);

        return first;
    }

    template <typename V, typename P>
    void priority_queue<V, P>::heapify(int i)
    {
        int largest = i;

        if (static_cast<size_type>(LCHILD(i)) < m_size &&
            heap[LCHILD(i)].priority > heap[i].priority)
        {
            largest = LCHILD(i);
        }

        if (static_cast<size_type>(RCHILD(i)) < m_size &&
            heap[RCHILD(i)].priority > heap[largest].priority)
        {
            largest = RCHILD(i);
        }
        if (largest != i)
        {
            swap(&heap[i], &heap[largest]);
            heapify(largest);
        }
    }

    template <typename V, typename P>
    void priority_queue<V, P>::swap(Item* i1, Item* i2)
    {
        Item temp = *i1;
        *i1 = *i2;
        *i2 = temp;
    }

    // Iterators ------------------------------------------------
    template <typename V, typename P>
    priority_queue<V, P>::iterator priority_queue<V, P>::begin()
    {
        return iterator(heap.data());
    }

    template <typename V, typename P>
    priority_queue<V, P>::iterator priority_queue<V, P>::end()
    {
        return iterator(&heap.data()[m_size]);
    }

    template <typename V, typename P>
    priority_queue<V, P>::iterator priority_queue<V, P>::find(V value)
    {
        auto iter = begin();
        for (auto endIter = end(); iter != endIter && iter->value != value; iter++)
        {
        }
        return iter;
    }

    // Utilities ---------------------------------------------------
    template <typename V, typename P>
    void priority_queue<V, P>::update(priority_queue<V, P>::iterator iter, P priority)
    {
        auto item = *iter;
        item.priority = priority;

        int i = iter - begin(); // Index of iter
        while (i && item.priority > heap[PARENT(i)].priority)
        {
            heap[i] = heap[PARENT(i)];
            i = PARENT(i);
        }

        heap[i] = item;
        heapify(0);
    }

    template <typename V, typename P>
    priority_queue<V, P>::size_type priority_queue<V, P>::size() const
    {
        return m_size;
    }

    template <typename V, typename P>
    bool priority_queue<V, P>::empty() const
    {
        return m_size == 0;
    }
} // namespace usu
