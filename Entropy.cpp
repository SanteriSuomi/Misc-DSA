#include <string>
#include <unordered_map>
#include <cmath>
#include <iostream>

using namespace std;

long double LOG2(long double n) {
    return log(n) / log(2);
}

// Entropy is the minimum number of bits needed to represent one symbol of a set (according to information theory)
long double calc_entropy(string s) {
    unordered_map<char, long double> map;
    for(const auto &c : s) map[c]++;
    long double ent = 0;
    for(const auto &p : map) {
        long double freq = p.second / s.size();
        ent -= freq * LOG2(freq);
    }
    return ent;
}

long double calc_entropy(unordered_map<char, long double> map, int length) {
    long double ent = 0;
    for(const auto &p : map) {
        long double freq = p.second / length;
        ent -= freq * LOG2(freq);
    }
    return ent;
}

int main() {
    std::string s = "ABBCCCDDDD";
    double ent = calc_entropy(s);
    std::cout << "Average entropy per symbol of this string is: " << ent << "\n";
    std::cout << "Total entropy is: " << ceil(ent) * s.size();
}