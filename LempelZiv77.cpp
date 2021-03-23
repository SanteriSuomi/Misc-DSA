#include <string>
#include <iostream>
#include <unordered_set>
#include <vector>

using namespace std;

struct token {
    token(char *s) : symbol(s) {}
    token(int o, int l) : offset(o), length(l) {}
    int offset = 0, length = 0;
    char *symbol = nullptr;
};

const int WINDOW_LENGTH = 255;

vector<token>* encode(string &data) {
    vector<token> *out = new vector<token>;
    for(int i = 0; i < data.length(); i++) {
        bool match = false;
        int length = 0;
        token *t = nullptr;
        int start = i - WINDOW_LENGTH;
        for(int j = start < 0 ? 0 : start; j < i - 1; j++) {
            if (data[j] == data[i + length]) {
                length++;
                match = true;
                if (t == nullptr) {
                    token n(i - j, 1);
                    t = &n;
                } else {
                    t->length++;
                }
            } else if (t != nullptr) {
                break;
            }
        }
        if (!match) {
            out->emplace_back(token(&data[i]));
        } else {
            out->emplace_back(*t);
            if (t->length > 1) i += t->length;
        }
    }
    return out;
}

string* decode(vector<token> *tokens) {
    string *out = new string;
    for(auto t : *tokens) {
        if (t.offset == 0 && t.length == 0) {
            *out += *t.symbol;
        } else {
            int from = out->length() - t.offset;
            int to = from + t.length;
            for(int i = from; i < to; i++) {
                char c = (*out)[i];
                *out += c;
            }
        }
    }
    return out;
}

// Very simplified LZ77 algorithm

int main() {
    string data = "TOBEORNOTTOBE";
    auto *tokens = encode(data);
    cout << "Encoded (tokens): " << "\n";
    for(auto t : *tokens) {
        cout << t.offset << " " << t.length << " " << t.symbol << "\n";
    }
    auto *decoded = decode(tokens);
    cout << "Decoded (from tokens)" << "\n";
    for(auto c : *decoded) {
        cout << c;
    }
    delete tokens;
    delete decoded;
}