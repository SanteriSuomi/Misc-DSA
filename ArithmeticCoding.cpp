#include <string>
#include <unordered_map>
#include <vector>
#include <utility>
#include <algorithm>
#include <iostream>
#include <cmath>

std::vector<std::pair<char, double>> getProbabilities(std::string &s) {
    std::unordered_map<char, int> map;
    for(auto &c : s) map[c]++;
    std::vector<std::pair<char, double>> probs;
    int n = s.size();
    for(auto it = map.begin(); it != map.end(); it++) {
        probs.emplace_back(std::make_pair(it->first, static_cast<double>(it->second) / n));
    }
    std::sort(probs.begin(), probs.end(), [](auto &first, auto &second) {
        return first.second < second.second;
    });
    return probs;
}

class range {
    public:
        range(double s, double e, char c) : start(s), end(e), character(c) {}
        double start, end;
        char character;
        void update(double s, double e, char c) {
            start = s;
            end = e;
            character = c;
        } 
};

std::vector<range> getRanges(std::vector<std::pair<char, double>> &probs) {
    double start = 0;
    std::vector<range> ranges;
    for(auto &p : probs) {
        double next = start + p.second;
        range r(start, next - 1e-15, p.first);
        ranges.emplace_back(r);
        start = next;
    }
    return ranges;
}

void updateRanges(std::vector<std::pair<char, double>> &probs, std::vector<range> &ranges, double start, double mult) {
    for(auto i = 0; i < probs.size(); i++) {
        probs[i].second *= mult;
        double next = start + probs[i].second;
        ranges[i].update(start, next, probs[i].first);
        start = next;
    }
}

double encode(std::vector<std::pair<char, double>> probs, std::string &s) {
    auto ranges = getRanges(probs);
    for(auto &c : s) {
        for(auto &r : ranges) {
            if (r.character == c) {
                updateRanges(probs, ranges, r.start, r.end - r.start);
                break;
            }
        }
    }
    return (ranges[0].start + ranges[ranges.size() - 1].end) / 2;
}

std::string decode(std::vector<std::pair<char, double>> probs, double n, int l) {
    auto ranges = getRanges(probs);
    std::string s;
    while(s.length() < l) {
        for(auto &r : ranges) {
            if (n >= r.start && n < r.end) {
                s += r.character;
                updateRanges(probs, ranges, r.start, r.end - r.start);
                break;
            }
        }
    }
    return s;
}

int main() {
    std::string s { "ABCCD" };
    auto probs = getProbabilities(s);
    double encoded = encode(probs, s);
    std::cout << "Encoded: " << encoded << "\n"; 
    std::cout << "Decoded: " << decode(probs, encoded, s.length());
} 