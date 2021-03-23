#include <algorithm>
#include <cstddef>
#include <initializer_list>
#include <iostream>
#include <iterator>

namespace vec {
template <typename T>
class Vector {
   private:
    T* data;
    std::size_t sz;
    std::size_t maxSz;
    static double constexpr SZ_MULT = 1.5;

    void expand(std::size_t newSize) {
        sz = newSize;
        maxSz = sz * SZ_MULT;
        data = new T[maxSz];
    }

   public:
    class Iterator : public std::iterator<std::random_access_iterator_tag, T> {
       private:
        T* ptr;

       public:
        using iterator_category = std::random_access_iterator_tag;

        Iterator() : ptr(nullptr) {}
        Iterator(T* rhs) : ptr(rhs) {}
        Iterator(const Iterator& rhs) : ptr(rhs.ptr) {}

        T& operator*() const { return *ptr; }
        T* operator->() const { return ptr; }
        T& operator[](std::ptrdiff_t rhs) const { return ptr[rhs]; }

        Iterator& operator+=(std::ptrdiff_t rhs) {
            ptr += rhs;
            return *this;
        }
        Iterator& operator-=(std::ptrdiff_t rhs) {
            ptr -= rhs;
            return *this;
        }
        Iterator& operator++() {
            ++ptr;
            return *this;
        }
        Iterator& operator--() {
            --ptr;
            return *this;
        }
        Iterator operator++(int) {
            Iterator tmp(*this);
            ++ptr;
            return tmp;
        }
        Iterator operator--(int) {
            Iterator tmp(*this);
            --ptr;
            return tmp;
        }
        std::ptrdiff_t operator-(const Iterator& rhs) const {
            return ptr - rhs.ptr;
        }
        Iterator operator+(std::ptrdiff_t rhs) const {
            return Iterator(ptr + rhs);
        }
        Iterator operator-(std::ptrdiff_t rhs) const {
            return Iterator(ptr - rhs);
        }

        friend Iterator operator+(std::ptrdiff_t lhs, const Iterator& rhs) {
            return Iterator(lhs + rhs.ptr);
        }
        friend Iterator operator-(std::ptrdiff_t lhs, const Iterator& rhs) {
            return Iterator(lhs - rhs.ptr);
        }

        bool operator==(const Iterator& rhs) const { return ptr == rhs.ptr; }
        bool operator!=(const Iterator& rhs) const { return ptr != rhs.ptr; }
        bool operator>(const Iterator& rhs) const { return ptr > rhs.ptr; }
        bool operator<(const Iterator& rhs) const { return ptr < rhs.ptr; }
        bool operator>=(const Iterator& rhs) const { return ptr >= rhs.ptr; }
        bool operator<=(const Iterator& rhs) const { return ptr <= rhs.ptr; }
    };

    Vector(std::initializer_list<T> args) {
        expand(args.size());
        for (auto i = args.begin(), j = 0; i != args.end(); i++, j++) {
            data[j] = *i;
        }
    }

    std::size_t size() { return sz; }
    std::size_t maxSize() { return maxSz; }

    bool empty() { return sz == 0; }

    void add(T item) {
        if (++sz > maxSz) {
            expand(sz);
        }
        data[sz - 1] = item;
    }

    void remove() { sz--; }

    T& at(size_t ind) {
        if (ind < 0 || ind >= sz) {
            throw std::invalid_argument("Index out of bounds");
        }
        return data[ind];
    }

    T& operator[](size_t ind) { return data[ind]; }

    Iterator begin() { return Iterator(&data[0]); }

    Iterator end() { return Iterator(&data[sz]); }

    ~Vector() { delete data; }
};
}  // namespace vec

int main() {
    auto v = vec::Vector<int>({1, 2, 3, 10, 4, 6});
    std::cout << v.size();
    v.add(5);
    std::cout << v.size();
    v.remove();
    std::cout << v.at(0);
    std::cout << v[v.size() - 1];
    std::cout << v[v.size() - 2] << "\n";
    for (const auto& i : v) {
        std::cout << i;
    }
    std::cout << "\n";
    std::sort(v.begin(), v.end());
    for (const auto& i : v) {
        std::cout << i;
    }
}