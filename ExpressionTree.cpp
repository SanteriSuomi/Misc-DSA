#include <string>
#include <stack>
#include <cctype>
#include <iostream>

using namespace std;

class ETNode {
    public:
        char op;
        ETNode *left, *right;

        ETNode(char op) {
            this->op = op;
            left = nullptr;
            right = nullptr;
        }

        ETNode(char op, ETNode *left, ETNode *right) {
            this->op = op;
            this->left = left;
            this->right = right;
        }
};

class ETNStack : public stack<ETNode*> {
    public:
        ETNode* toppop() {
            ETNode* top = this->top();
            pop();
            return top;
        }
};

class ExpressionTree {
    private:
        ETNode *root;

        bool isoperator(char c) {
            return c == '+' || c == '-' || c == '*' || c == '/'; 
        }

        void infix(ETNode *root, string *exp) {
            if (!root) return;
            if (isoperator(root->op)) {
                exp->append("(");
            } 
            infix(root->left, exp);
            (*exp) += root->op;
            infix(root->right, exp);
            if (isoperator(root->op)) {
                exp->append(")");
            }
        }
    public:
        ExpressionTree(string &expression) {
            ETNStack st;
            for(const auto &c : expression) {
                if (isalpha(c)) {
                    ETNode* n = new ETNode(c);
                    st.push(n);
                } else if (isoperator(c)) {
                    ETNode *r = st.toppop();
                    ETNode *l = st.toppop();
                    ETNode *newNode = new ETNode(c, l, r);
                    st.push(newNode);
                }
            }
            root = st.toppop();
        } 

        string* infix() {
            string *inf = new string();
            infix(root, inf);
            return inf;
        }
};

int main() {
    string expression {"a b + c d e + * *"};
    ExpressionTree et(expression);
    string *inf = et.infix();
    cout << *inf;
}