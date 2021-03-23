#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
#include <utility>

using namespace std;

void sort(vector<string> &s) {
    sort(s.begin(), s.end(), [](const auto &l, const auto &r) {
        return l[0] < r[0];
    });
}

vector<string>* permutations(string &s) {
    vector<string> *p = new vector<string>;
    p->emplace_back(s);
    string curr = s;
    for(size_t i = 0; i < s.length() - 1; i++) {
        char temp = curr[0];
        for(int j = 1; j < s.length(); j++) {
            swap(curr[j], temp);
            curr[j] = curr[j];
        }
        curr[0] = temp;
        p->emplace_back(curr);
    }
    sort(*p);
    return p;
}

pair<string*, int>* encode(string &s) {
    auto p = permutations(s);
    string *r = new string;
    int ind = 0;
    for(size_t i = 0; i < s.length(); i++) {
        (*r) += (*p)[i][0];
        if (s.compare((*p)[i]) == 0) ind = i;
    }
    for(size_t i = 0; i < r->length() / 2; i++) {
        swap((*r)[i], (*r)[r->length() - i - 1]);
    }
    delete p;
    auto *pr = new pair<string*, int>;
    pr->first = r;
    pr->second = ind;
    return pr;
}

string* decode(pair<string*, int>* pr) {
    string *d = new string;
    auto l = pr->first->length();
    vector<string> tbl;
    for(size_t i = 0; i < l; i++) {
        string s;
        tbl.emplace_back(s);
        tbl[i] += pr->first->at(i);
    }
    sort(tbl);
    int k = 0;
    for(size_t i = 0; i < l - 1; i++) {
        for(size_t j = 0; j < l; j++) {
            tbl[j].insert(0, 1, pr->first->at(k++));
        }
        k = 0;
        sort(tbl);
    }   
    *d = tbl[pr->second];
    return d;
}

int main() {
    string s = "BANANA";
    auto pr = encode(s);
    cout << *pr->first << "\n";
    cout << pr->second << "\n";
    cout << *decode(pr);
}