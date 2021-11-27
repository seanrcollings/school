#include <cstdint>
#include <iostream>
#include <utility>

namespace usu
{
    template <typename T>
    class shared_ptr
    {
      public:
        shared_ptr(T*);
        shared_ptr(const shared_ptr<T>& obj);
        shared_ptr(shared_ptr<T>&& obj);
        ~shared_ptr();

        T* get();
        std::uint64_t use_count();

        shared_ptr<T>& operator=(const shared_ptr<T>& obj);
        shared_ptr<T>& operator=(shared_ptr<T>&& obj);

        T* operator->();
        T operator*();

      private:
        T* m_ptr;
        std::uint64_t* m_refCount;

        // Performs shared cleanup
        void cleanup()
        {
            if (m_refCount != nullptr)
            {
                (*m_refCount)--;
                if (*m_refCount == 0)
                {
                    if (m_ptr != nullptr)
                        delete m_ptr;
                    if (m_refCount != nullptr)
                        delete m_refCount;
                }
            }
        }
    };

    template <typename T>
    shared_ptr<T>::shared_ptr(T* ptr) :
        m_ptr(ptr), m_refCount(new std::uint64_t(1))
    {
    }

    // Copy Constructor
    template <typename T>
    shared_ptr<T>::shared_ptr(const shared_ptr<T>& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        (*m_refCount)++;
    }

    // Move Constructor
    template <typename T>
    shared_ptr<T>::shared_ptr(shared_ptr<T>&& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        obj.m_ptr = nullptr;
        obj.m_refCount = nullptr;
    }

    // Destructor
    template <typename T>
    shared_ptr<T>::~shared_ptr()
    {
        cleanup();
    }

    // Copy Assignment
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(const shared_ptr<T>& obj)
    {
        cleanup();
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        (*m_refCount)++;
        return *this;
    }

    // Move Assignment
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(shared_ptr<T>&& obj)
    {
        cleanup();
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        obj.m_ptr = nullptr;
        obj.m_refCount = nullptr;
        return *this;
    }

    template <typename T>
    T* shared_ptr<T>::get()
    {
        return m_ptr;
    }

    template <typename T>
    std::uint64_t shared_ptr<T>::use_count()
    {
        return *m_refCount;
    }

    template <typename T>
    T* shared_ptr<T>::operator->()
    {
        return m_ptr;
    }

    template <typename T>
    T shared_ptr<T>::operator*()
    {
        return *m_ptr;
    }

    template <typename T, typename... Args>
    shared_ptr<T> make_shared(Args&&... args)
    {
        return shared_ptr<T>(new T(std::forward<Args>(args)...));
    }

    template <typename T>
    class shared_ptr<T[]>
    {
      public:
        shared_ptr(T*, std::size_t size);
        shared_ptr(const shared_ptr<T[]>& obj);
        shared_ptr(shared_ptr<T[]>&& obj);
        ~shared_ptr();

        std::uint64_t use_count();
        std::size_t size();

        shared_ptr<T[]>& operator=(const shared_ptr<T[]>& obj);
        shared_ptr<T[]>& operator=(shared_ptr<T[]>&& obj);
        T& operator[](int idx);

      private:
        T* m_ptr;
        std::size_t m_size;
        std::uint64_t* m_refCount;

        // Performs shared cleanup
        void cleanup()
        {
            if (m_refCount != nullptr)
            {
                (*m_refCount)--;
                if (*m_refCount == 0)
                {
                    if (m_ptr != nullptr)
                        delete[] m_ptr;
                    if (m_refCount != nullptr)
                        delete m_refCount;
                }
            }
        }
    };

    template <typename T>
    shared_ptr<T[]>::shared_ptr(T* ptr, std::size_t size) :
        m_ptr(ptr), m_size(size), m_refCount(new std::uint64_t(1))
    {
    }

    // Copy Constructor
    template <typename T>
    shared_ptr<T[]>::shared_ptr(const shared_ptr<T[]>& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        m_size = obj.m_size;
        (*m_refCount)++;
    }

    // Move Constructor
    template <typename T>
    shared_ptr<T[]>::shared_ptr(shared_ptr<T[]>&& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        m_size = obj.m_size;
        obj.m_ptr = nullptr;
        obj.m_refCount = nullptr;
    }

    // Destructor
    template <typename T>
    shared_ptr<T[]>::~shared_ptr()
    {
        cleanup();
    }

    // Copy Assignment
    template <typename T>
    shared_ptr<T[]>& shared_ptr<T[]>::operator=(const shared_ptr<T[]>& obj)
    {
        cleanup();
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        m_size = obj.m_size;
        (*m_refCount)++;
        return *this;
    }

    // Move Assignment
    template <typename T>
    shared_ptr<T[]>& shared_ptr<T[]>::operator=(shared_ptr<T[]>&& obj)
    {
        cleanup();
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        m_size = obj.m_size;
        obj.m_ptr = nullptr;
        obj.m_refCount = nullptr;
        return *this;
    }

    template <typename T>
    std::uint64_t shared_ptr<T[]>::use_count()
    {
        return *m_refCount;
    }

    template <typename T>
    std::size_t shared_ptr<T[]>::size()
    {
        return m_size;
    }

    template <typename T>
    T& shared_ptr<T[]>::operator[](int idx)
    {
        return m_ptr[idx];
    }

    template <typename T, unsigned int N>
    shared_ptr<T[]> make_shared_array()
    {
        return shared_ptr<T[]>(new T[N], N);
    }
} // namespace usu
