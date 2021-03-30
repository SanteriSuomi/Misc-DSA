#include <cstring>
#include <iostream>
#include <string>

// Barebones implementation of a string class
namespace str {
class string {
   private:
    char *arr;

    void clear() {
        if (!arr) delete arr;
    }

    void moveToThis(const char *newArr) {
        length = std::strlen(newArr);
        arr = new char[length];
        for (int ind = 0; ind < length; ind++, newArr++) {
            arr[ind] = *newArr;
        }
    }

    template <typename T>
    void fill(T newArr, char *temp, int newLength) {
        int ind = 0;
        int newInd = 0;
        while (ind < newLength) {
            if (ind < length) {
                temp[ind] = arr[ind];
            } else {
                temp[ind] = newArr[newInd++];
            }
            ind++;
        }
        length = newLength;
        clear();
        arr = temp;
    }

   public:
    int length;

    string(const char *newArr) { moveToThis(newArr); }

    string() {
        arr = new char[0];
        length = 0;
    }

    char &at(int ind) {
        if (ind < 0 || ind > length) {
            throw "Accessed out of bounds index!";
        }
        return arr[ind];
    }

    str::string substr(int pos, int length) {
        char *temp = new char[length];
        for (int i = pos, j = 0; i < pos + length && i < length; i++, j++) {
            temp[j] = arr[i];
        }
        return str::string(temp);
    }

    char &operator[](int ind) {
        if (ind < 0 || ind > length) {
            throw "Accessed out of bounds index!";
        }
        return arr[ind];
    }

    bool operator==(str::string &str) {
        if (length != str.length) return false;
        for (int i = 0; i < length; i++) {
            if (at(i) != str.at(i)) return false;
        }
        return true;
    }

    void operator+=(str::string &str) {
        int newLength = length + str.length;
        char *temp = new char[newLength];
        fill(str, temp, newLength);
    }

    void operator+=(const char *newArr) {
        int newLength = length + std::strlen(newArr);
        char *temp = new char[newLength];
        fill(newArr, temp, newLength);
    }

    string &operator=(const char *newArr) {
        clear();
        moveToThis(newArr);
        return *this;
    }

    friend std::ostream &operator<<(std::ostream &os, str::string &str) {
        for (int i = 0; i < str.length; i++) {
            os << str.at(i);
        }
        return os;
    }

    ~string() { clear(); }
};
}  // namespace str

int main() {
    str::string s = "string";
    str::string s2 = "string";
    std::cout << (s == s2);
    s2 = "sr";
    std::cout << (s == s2);
    s += s2;
    for (int i = 0; i < s.length; i++) {
        std::cout << s.at(i);
    }
    s += "bb";
    for (int i = 0; i < s.length; i++) {
        std::cout << s.at(i);
    }
    std::cout << "\n";
    str::string substr = s.substr(0, 6);
    for (int i = 0; i < substr.length; i++) {
        std::cout << substr.at(i);
    }
    std::cout << s2;
}