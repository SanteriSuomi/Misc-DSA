#include <string>
#include <unordered_map>
#include <utility>
#include <queue>
#include <iostream>
#include <bitset>

class VLE {
    private:
        // https://www3.nd.edu/~busiforc/handouts/cryptography/letterfrequencies.html
        std::unordered_map<char, std::string> codeEncode = {
            { 'A', "00" },
            { 'C', "11" },
            { 'D', "011" }, 
            { 'B', "101" }
        };
        std::unordered_map<std::string, char> codeDecode = {
            { "00", 'A' },
            { "11", 'C' },
            { "011", 'D' }, 
            { "101", 'B' }
        };

        // const int CODE_SIZE = 5;

        // void getCodes(std::vector<std::string> &v, std::string s, int i) {
        //     if (i > CODE_SIZE) return;
        //     v.push_back(s + '0');
        //     v.push_back(s + '1');
        //     getCodes(v, s + '0', i + 1);
        //     getCodes(v, s + '1', i + 1);
        // }

        // void generateCodeTable(std::string &data) {
        //     std::vector<std::string> v;
        //     std::string s;
        //     getCodes(v, s, 0);
        //     std::cout << "0";
        // }
    public:
        // Return pairs of symbol probabilities of a given string as a priority queue.
        std::priority_queue<std::pair<char, int>> probabilities(std::string &data) {
            std::unordered_map<char, int> map;
            for(const auto &x : data) map[x]++;
            std::priority_queue<std::pair<char, int>> pq;
            for(const auto &x : map) pq.push(x);
            return pq;
        }

        std::string encode(std::string &data) {
            std::string encoded;
            for(const auto &x : data) {
                encoded += codeEncode[x];
            }
            return encoded;
        }

        std::string decode(std::string &data) {
            std::string decoded;
            std::string s;
            for(const auto &x : data) {
                s += x;
                if (codeDecode.count(s) > 0) {
                    decoded += codeDecode[s];
                    s.clear();
                }
            }
            return decoded;
        }
};

std::string strToBin(std::string &data) {
    std::string bin;
    for(const auto &x : data) {
        bin += ' ' + std::bitset<8>(x).to_string();
    }
    return bin;
}

int main() {
    std::string data = "AAABBACDCCC";
    std::cout << "Data: " << data << "\n";
    VLE vle;
    std::string encoded = vle.encode(data);
    std::string notEncoded = strToBin(data);
    std::cout << "Without Encoding: " << notEncoded << "\n";
    std::cout << "Encoded: " << encoded << "\n";
    std::string decoded = vle.decode(encoded);
    std::cout << "Decoded: " << decoded << "\n";
    std::cout << "We saved approximately " << notEncoded.size() - encoded.size() << " bits." << "\n"; 
} 