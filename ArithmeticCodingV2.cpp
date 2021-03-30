#include <string>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <iostream>
#include "Entropy.cpp"
#include <utility>

using namespace std;

struct range {
    range(long double l, long double h, char s) : low(l), high(h), symbol(s) {}
    long double low, high;
    char symbol;
};

struct probability {
    probability(long double p, char s) : prob(p), symbol(s) {}
    long double prob;
    char symbol;
};

vector<range> get_ranges(string &data) {
    unordered_map<char, long double> map;
    for(const auto &c : data) {
        map[c]++;
    }
    vector<probability> probs;
    for(const auto &p : map) {
        probs.emplace_back(p.second / data.size(), p.first);
    }
    vector<range> ranges;
    long double start = 0;
    for(const auto &p : probs) {
        long double next = start + p.prob;
        ranges.emplace_back(start, next, p.symbol);
        start = next;
    }
    return ranges;
}

long double encode(string &data, vector<range> ranges) {
    long double low = 0.0;
    long double high = 1.0;
    long double curr = 0.0;
    for(const auto &c : data) {
        curr = high - low;
        for(const auto &r : ranges) {
            if (r.symbol == c) {
                high = low + (curr * r.high);
                low = low + (curr * r.low);
                break;
            }
        }
    }
    return (low + high) / 2.0;
}

void decode(long double encoded, int length, vector<range> ranges, string &decoded) {
    long double curr = 0.0;
    while(decoded.length() < length) {
        for(const auto &r : ranges) {
            if (encoded >= r.low && encoded < r.high) {
                decoded += r.symbol;
                curr = r.high - r.low;
                encoded = (encoded - r.low) / curr;
                break;
            }
        }
    }
}

int main() {
    string data { "BABCDDDGGAFFJJJJ" };
    auto ranges = get_ranges(data);
    long double encoded = encode(data, ranges);
    cout << encoded << endl;
    string decoded;
    decode(encoded, data.length(), ranges, decoded);
    cout << decoded << endl;
    cout << (data == decoded);
}