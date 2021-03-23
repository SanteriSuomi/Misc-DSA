#include <string>
#include <iostream>
#include <stack>

std::string decToBin(int n) {
    std::stack<char> st;
    while(n > 0) {
        if (n % 2 == 0) st.push('0');
        else st.push('1');
        n /= 2;
    }
    std::string r;
    while(!st.empty()) {
        r += st.top();
        st.pop();
    }
    return r;
}

int main() {
    std::cout << decToBin(294);
}