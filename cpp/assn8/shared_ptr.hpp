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
        obj.m_ptr = nullptr;
        obj.m_refCount = nullptr;
    }

    // Destructor
    template <typename T>
    shared_ptr<T>::~shared_ptr()
    {
        (*m_refCount)--;

        if (*m_refCount == 0)
        {
            delete m_ptr;
            delete m_refCount;
        }
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

    // Copy Assignment
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(const shared_ptr<T>& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        (*m_refCount)++;
        return *this;
    }

    // Move Assignment
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(shared_ptr<T>&& obj)
    {
        m_ptr = obj.m_ptr;
        m_refCount = obj.m_refCount;
        return *this;
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

// #include <cstdint>
// #include <iostream>
// #include <utility>

// namespace usu
// {

//     template <class T>
//     class shared_ptr
//     {
//       private:
//         T* ptr = nullptr;
//         uint64_t* refCount = nullptr;

//       public:
//         shared_ptr() :
//             ptr(nullptr), refCount(new uint64_t(0)) // default constructor
//         {
//         }

//         shared_ptr(T* ptr) :
//             ptr(ptr), refCount(new uint64_t(1)) // constructor
//         {
//         }

//         /*** Copy Semantics ***/
//         shared_ptr(const shared_ptr& obj) // copy constructor
//         {
//             this->ptr = obj.ptr; // share the underlying pointer
//             this->refCount = obj.refCount;
//             if (nullptr != obj.ptr)
//             {
//                 (*this->refCount)++; // if the pointer is not null, increment the refCount
//             }
//         }

//         shared_ptr& operator=(const shared_ptr& obj) // copy assignment
//         {
//             __cleanup__(); // cleanup any existing data

//             // Assign incoming object's data to this object
//             this->ptr = obj.ptr; // share the underlying pointer
//             this->refCount = obj.refCount;
//             if (nullptr != obj.ptr)
//             {
//                 (*this->refCount)++; // if the pointer is not null, increment the refCount
//             }
//             return *this;
//         }

//         /*** Move Semantics ***/
//         shared_ptr(shared_ptr&& dyingObj) // move constructor
//         {
//             this->ptr = dyingObj.ptr; // share the underlying pointer
//             this->refCount = dyingObj.refCount;

//             dyingObj.ptr = dyingObj.refCount = nullptr; // clean the dying object
//         }

//         shared_ptr& operator=(shared_ptr&& dyingObj) // move assignment
//         {
//             __cleanup__(); // cleanup any existing data

//             this->ptr = dyingObj.ptr; // share the underlying pointer
//             this->refCount = dyingObj.refCount;

//             dyingObj.ptr = dyingObj.refCount = nullptr; // clean the dying object
//         }

//         uint64_t use_count() const
//         {
//             return *refCount; // *this->refCount
//         }

//         T* get() const
//         {
//             return this->ptr;
//         }

//         T* operator->() const
//         {
//             return this->ptr;
//         }

//         T& operator*() const
//         {
//             return *ptr;
//         }

//         ~shared_ptr() // destructor
//         {
//             __cleanup__();
//         }

//       private:
//         void __cleanup__()
//         {
//             (*refCount)--;
//             if (*refCount == 0)
//             {
//                 if (nullptr != ptr)
//                     delete ptr;
//                 delete refCount;
//             }
//         }
//     };

//     template <typename T, typename... Args>
//     shared_ptr<T> make_shared(Args&&... args)
//     {
//         return shared_ptr<T>(new T(std::forward<Args>(args)...));
//     }

//     // template <typename T, unsigned int N>
//     // shared_ptr<T[]> make_shared_array()
//     // {
//     //     return shared_ptr<T[]>(new T[N], N);
//     // }
// } // namespace usu