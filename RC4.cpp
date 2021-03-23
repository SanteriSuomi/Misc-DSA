#include <cstring>
#include <string>
#include <iostream>
#include <utility>

// Initialize internal state s[]
int* ksa(std::string &key) {
    int* s = new int[256];
    for(int i = 0; i < 256; i++) {
        s[i] = i;
    }
    int j = 0;
    for(int i = 0; i < 256; i++) {
        j = (j + s[i] + key[i % key.length()]) % 256;
        std::swap(s[i], s[j]);
    }
    return s;
}

// Generate pseudo-random char from internal state and given char
char prga(char c, int* s, int &i, int &j) {
    i = (i + 1) % 256;
    j = (j + s[i]) % 256;
    std::swap(s[i], s[j]);
    return s[(s[i] + s[j]) % 256];
}

std::string rc4(std::string &key, std::string &data) {
    int *s = ksa(key);
    int i = 0, j = 0;
    std::string ks; // Keystream
    ks.reserve(data.length());
    for(const auto &c : data) {
        ks += prga(c, s, i, j);
    }
    delete s;
    std::string res; // Encrypted/Decrypted result
    res.reserve(data.length());
    for(size_t t = 0; t < data.length(); t++) {
        res += (data[t] ^ ks[t]);
    }
    return res;
}

int main() {
    std::string key = "Key";
    std::string pt = "Plaintext";
    std::string ct = rc4(key, pt);
    std::cout << ct;
    std::string ptBack = rc4(key, ct);
    std::cout << ptBack;
}