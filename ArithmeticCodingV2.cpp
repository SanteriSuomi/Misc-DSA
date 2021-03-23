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

// vector<range> update_ranges(unordered_map<char, long double> &map, int length) {
//     vector<probability> probs;
//     for(const auto &p : map) {
//         probs.emplace_back(p.second / length, p.first);
//     }
//     vector<range> ranges;
//     long double start = 0;
//     for(const auto &p : probs) {
//         long double next = start + p.prob;
//         ranges.emplace_back(start, next, p.symbol);
//         start = next;
//     }
//     return ranges;
// }

// enum token {
//     literal,
//     reset
// };

// unordered_map<token, char> tm {
//     { literal, '!' }, 
//     { reset, '?' }
// };

// long double adaptive_encode(string &data) {
//     unordered_map<char, long double> map;
//     vector<range> ranges;

//     int length; // Number of symbols read so far

//     long double threshold = calc_entropy(data); // Threshold to reset probabilities
//     long double aobp = 0.0; // Average output bits per symbol

//     long double low = 0.0;
//     long double high = 1.0;
//     long double curr = 0.0;
//     char c;
//     for(const auto &ch : data) {
//         aobp = calc_entropy(map, length);
//         if (aobp > threshold) {
//             aobp = 0;
//             length = 0;
//             map.clear();
//             ranges.clear();
//         }
//         c = ch;
//         length++;
//         curr = high - low;
//         if (map.count(c) == 0) {
//             map[c]++;
//             c = tm[literal];
//             length++;
//         } else {
//             for(const auto &r : ranges) {
//                 if (r.symbol == c) {
//                     high = low + (curr * r.high);
//                     low = low + (curr * r.low);
//                     break;
//                 }
//             }
//         }
//         map[c]++;
//         ranges = update_ranges(map, length);
//     return (low + high) / 2.0;
// } 

// pair<long double, unordered_map<char, long double>> adaptive_encode(string &data) {
//     unordered_map<char, long double> map;
//     vector<range> ranges;
//     int length = 0; // Number of symbols read so far
//     long double low = 0.0;
//     long double high = 1.0;
//     long double curr = 0.0;
//     for(const auto &c : data) {
//         curr = high - low;
//         if (map.count(c) != 0) {
//             for(const auto &r : ranges) {
//                 if (r.symbol == c) {
//                     high = low + (curr * r.high);
//                     low = low + (curr * r.low);
//                     break;
//                 }
//             }
//         }
//         map[c]++;
//         length++;
//         ranges = update_ranges(map, length);
//     }
//     return make_pair((low + high) / 2.0, map);
// } 

// string adaptive_decode(long double encoded, unordered_map<char, long double> map) {
//     int length = 0;
//     for(const auto &p : map) {
//         length += p.second;
//     }
//     vector<range> ranges = update_ranges(map, length);
//     string decoded;
//     long double curr = 0.0;
//     while(decoded.length() < length) {
//         for(const auto &r : ranges) {
//             if (encoded >= r.low && encoded < r.high) {
//                 decoded += r.symbol;
//                 map[r.symbol]--;
//                 ranges = update_ranges(map, length);
//                 curr = r.high - r.low;
//                 encoded = (encoded - r.low) / curr;
//                 break;
//             }
//         }
//     }
//     return decoded;
// } 

int main() {
    string data { "BABCDDDGGAFFJJJJ" };
    auto ranges = get_ranges(data);
    long double encoded = encode(data, ranges);
    cout << encoded << endl;
    string decoded;
    decode(encoded, data.length(), ranges, decoded);
    cout << decoded << endl;
    cout << (data == decoded);

    // auto encoded = adaptive_encode(data);
    // cout << encoded.first;
    // cout << adaptive_decode(encoded.first, encoded.second);
}