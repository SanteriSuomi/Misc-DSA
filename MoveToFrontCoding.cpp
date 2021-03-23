#include <string>
#include <unordered_set>
#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

class MTF {
    private:
        string *input;
        string *sorted;

        void createSorted() {
            unordered_set<char> set;
            for(auto &c : *input) {
               set.insert(c);
            }
            sorted = new string;
            for(auto &c : set) {
                *sorted += c;
            }
            sort(sorted->begin(), sorted->end());
        }

        void refreshInput(string &in) {
            input = new string(in);
            createSorted();
            encodeOutput = new vector<int>;
            encodeOutput->reserve(in.length());
        }

        void refreshOutput() {
            decodeOutput = new string;
            createSorted();
        }

        void swap(string *s, int i, int j) {
            char temp = (*s)[i];
            (*s)[i] = (*s)[j];
            (*s)[j] = temp;
        }

        template <typename List, typename Value>
        void move(List *l, int i, int j) {
            if (i <= 0) return;
            Value temp = (*l)[i];
            (*l)[i] = (*l)[j];
            (*l)[j] = temp;
        }
    public:
        vector<int> *encodeOutput;
        string *decodeOutput;

        void encode(string &in) {
            refreshInput(in);
            for(const auto &c : *input) {
                auto ind = sorted->find(c);
                if (ind == string::npos) cout << "ERROR, COULD NOT FIND INDEX." << "\n";
                encodeOutput->emplace_back(ind);
                move<string, char>(sorted, ind - 1, ind);
            }
        }

        void decode() {
            refreshOutput();
            for(const auto &ind : *encodeOutput) {
                (*decodeOutput) += (*sorted)[ind];
                move<string, char>(sorted, ind - 1, ind);
            }
        }

        ~MTF() {
            delete input;
            delete sorted;
            delete encodeOutput;
            delete decodeOutput;
        }
};

int main() {
    string s = "banana";
    MTF mtf;
    mtf.encode(s);
    for(auto &i : *mtf.encodeOutput) {
        cout << i << " ";
    }
    mtf.decode();
    for(auto &i : *mtf.decodeOutput) {
        cout << i;
    }
}