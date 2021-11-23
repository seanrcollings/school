#include <cstdint>
#include <iostream>
#include <ratio>

namespace usu
{

    template <typename TConversion, typename TStorage = std::uint64_t>
    class weight
    {
      public:
        using storage = TStorage;
        using conversion = TConversion;

        weight();

        weight(TStorage value);

        TStorage count() const;

      private:
        TConversion ratio;
        TStorage m_count;
    };

    template <typename TConversion, typename TStorage>
    weight<TConversion, TStorage>::weight() :
        m_count(0)
    {
    }

    template <typename TConversion, typename TStorage>
    weight<TConversion, TStorage>::weight(TStorage count) :
        m_count(count)
    {
    }

    template <typename TConversion, typename TStorage>
    TStorage weight<TConversion, TStorage>::count() const
    {
        return m_count;
    }

    template <typename TConversion, typename TStorage>
    weight<TConversion, TStorage> operator+(const weight<TConversion, TStorage> first, const weight<TConversion, TStorage> second)
    {
        return weight<TConversion, TStorage>(first.count() + second.count());
    }

    template <typename TConversion, typename TStorage>
    weight<TConversion, TStorage> operator-(const weight<TConversion, TStorage> first, const weight<TConversion, TStorage> second)
    {
        return weight<TConversion, TStorage>(first.count() - second.count());
    }

    template <typename TConversion, typename TStorage, typename TScalar>
    weight<TConversion, TStorage> operator*(const weight<TConversion, TStorage> first, const TScalar scalar)
    {
        return weight<TConversion, TStorage>(first.count() * scalar);
    }

    template <typename TConversion, typename TStorage, typename TScalar>
    weight<TConversion, TStorage> operator*(const TScalar scalar, const weight<TConversion, TStorage> first)
    {
        return weight<TConversion, TStorage>(first.count() * scalar);
    }

    template <typename F, typename S>
    bool operator==(const F first, const S second)
    {
        return first.count() == weight_cast<F>(second).count();
    }

    template <typename F, typename S>
    bool operator!=(const F first, const S second)
    {
        return first.count() != weight_cast<F>(second).count();
    }

    template <typename F, typename S>
    bool operator>(const F first, const S second)
    {
        return first.count() > weight_cast<F>(second).count();
    }

    template <typename F, typename S>
    bool operator<(const F first, const S second)
    {
        return first.count() < weight_cast<F>(second).count();
    }

    template <typename F, typename S>
    bool operator>=(const F first, const S second)
    {
        return first.count() >= weight_cast<F>(second).count();
    }

    template <typename F, typename S>
    bool operator<=(const F first, const S second)
    {
        return first.count() <= weight_cast<F>(second).count();
    }

    // End of weight implementation

    using microgram = weight<std::ratio<1, 1000000>, float>;
    using gram = weight<std::ratio<1, 1>>;
    using kilogram = weight<std::ratio<1000, 1>, float>;
    using ounce = weight<std::ratio<28349523125, 1000000000>, float>;
    using pound = weight<std::ratio<45359237, 100000>, float>;
    using ton = weight<std::ratio<90718474, 100>, float>;

    template <typename To, typename From>
    To weight_cast(const From& from)
    {
        // Converts from to grams
        double grams = from.count() * (static_cast<double>(From::conversion::num) / From::conversion::den);
        // Converts Grams to the target value
        double to = grams * (static_cast<double>(To::conversion::den) / To::conversion::num);

        return To(to);
    }

} // namespace usu