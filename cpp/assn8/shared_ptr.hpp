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
        uint64_t use_count();

        shared_ptr<T>& operator=(const shared_ptr<T>& obj);
        shared_ptr<T>& operator=(shared_ptr<T>&& obj);

        T* operator->();
        T operator*();

      private:
        T* m_ptr;
        uint64_t* m_refCount;

        // Performs shared cleanup
        void cleanup()
        {
            (*m_refCount)--;
            if (*m_refCount == 0)
            {
                delete m_ptr;
                delete m_refCount;
            }
        }
    };

    template <typename T>
    shared_ptr<T>::shared_ptr(T* ptr) :
        m_ptr(ptr), m_refCount(new uint64_t(1))
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
        obj.m_ptr = obj.m_refCount = nullptr;
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
        obj.m_ptr = obj.m_refCount = nullptr;
        return *this;
    }

    template <typename T>
    T* shared_ptr<T>::get()
    {
        return m_ptr;
    }

    template <typename T>
    uint64_t shared_ptr<T>::use_count()
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

    // template <typename T, unsigned int N>
    // shared_ptr<T[]> make_shared_array()
    // {
    //     return shared_ptr<T[]>(new T[N], N);
    // }
} // namespace usu
