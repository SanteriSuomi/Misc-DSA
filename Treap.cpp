#include <stdlib.h>
#include <time.h>
// #include <stack>
#include <utility>
#include <iostream>
#include <utility>
#include <limits>
#include <functional>

template <typename T>
class TNode {
    public:
        T key;
        int priority;
        TNode *left, *right;

        TNode(T key, int priority) {
            this->key = key;
            this->priority = priority;
            this->left = nullptr;
            this->right = nullptr;
        }
};

template <typename T>
class Treap {
    private:
        TNode<T> *root;
        size_t sz;

        void insert_rec(TNode<T>* &root, T key, int priority) {
            if (!root) {
                root = new TNode<T>(key, priority);
                return;
            }
            insert_rec(key < root->key ? root->left : root->right, key, priority);
            if (root->left && root->left->priority > root->priority) {
                right_rotate(root);
            }
            if (root->right && root->right->priority > root->priority) {
                left_rotate(root);
            }
        }

        void right_rotate(TNode<T>* &x) {
            TNode<T> *y = x->left;
            TNode<T> *r = y->left;
            TNode<T> *g = y->right;
            TNode<T> *b = x->right;
            x->left = g;
            y->right = x;
            x = y;
        }

        void left_rotate(TNode<T>* &y) {
            TNode<T> *x = y->right;
            TNode<T> *r = y->left;
            TNode<T> *g = x->left;
            TNode<T> *b = x->right;
            x->left = y;
            y->right = g;
            y = x;
        }

        TNode<T>* build(T data[], int left, int right) {
            if (left > right) return nullptr;
            int middle = (left + right) / 2;
            TNode<T> *newNode = new TNode<T>(data[middle], rand());
            newNode->left = build(data, left, middle - 1);
            newNode->right = build(data, middle + 1, right);
            heapify(newNode);
            return newNode;
        }

        void heapify(TNode<T> *node) {
            if (!node) return;
            TNode<T> *max = node;
            if (node->left && node->left->priority > max->priority) {
                max = node->left;
            }
            if (node->right && node->right->priority > max->priority) {
                max = node->right;
            }
            if (max != node) {
                std::swap(max->priority, node->priority);
                heapify(max);
            }
        }

        bool remove_rec(TNode<T>* &root, T key) {
            if (!root) return false;
            if (root->key != key) {
                return remove_rec(key < root->key ? root->left : root->right, key);
            } 
            if(!root->left && !root->right) { // Case 1: No children
                delete root;
                root = nullptr;
            } else if (root->left && root->right) { // Case 2: Two children
                if (root->left->priority < root->right->priority) { 
                    left_rotate(root);
                    remove_rec(root->left, key);
                } else {
                    right_rotate(root);
                    remove_rec(root->right, key);
                }
            } else { // Case 3: One child 
                TNode<T> *child = root->left ? root->left : root->right;
                TNode<T> *old = root;
                root = child;
                delete old;
            }
            return true;
        }

        std::pair<TNode<T>*, TNode<T>*> split_tree(T key) {
            insert_rec(root, key, std::numeric_limits<int>::max());
            return std::make_pair(root->left, root->right);
        }

        void clean(TNode<T>* node) {
            delete node;
            node = nullptr;
        }

        void delete_tree_rec(TNode<T>* root) {
            if (!root) return;
            delete_tree_rec(root->left);
            delete_tree_rec(root->right);
            clean(root);
        }

        void print_rec(TNode<T>* &root) {
            if (!root) return;
            print_rec(root->left);
            print_rec(root->right);
            std::cout << root->key << ' ';
        }

    public:
        Treap() {
            this->root = nullptr;
            srand(time(nullptr));
            sz = 0;
        }

        Treap(T data[], int length) {
            this->root = nullptr;
            srand(time(nullptr));
            root = build(data, 0, length - 1);
            sz = 0;
        }

        template <typename... Ts>
        void insert(Ts... keys) {
            for(const auto key : {keys...}) {
                sz++;
                insert_rec(root, key, rand());
            }
        }

        bool remove(T key) {
            sz--;
            return remove_rec(root, key);
        }

        std::pair<TNode<T>*, TNode<T>*> split(T key) {
            return split_tree(key);
        }

        void print() {
            print_rec(root);
            std::cout << "\n";
        }

        size_t size() {
            return sz;
        }

        ~Treap() {
            delete_tree_rec(root);
        }

        // void insert(T key) {
        //     TNode<T> *newNode = new TNode<T>(key);
        //     if (!root) {
        //         root = newNode;
        //         return;
        //     }
        //     std::stack<TNode<T>*> path; // Store path
        //     bst_insert(newNode, path);
        //     path_rotate(path);
        // }

        // void bst_insert(TNode<T> *newNode, std::stack<TNode<T>*> &path) {
        //     TNode<T> *node = root;
        //     while(node) {
        //         path.push(node);
        //         if (newNode->key == node->key) break;
        //         if (newNode->key > node->key) {
        //             if (node->right == nullptr) {
        //                 node->right = newNode;
        //                 break;
        //             }
        //             node = node->right;
        //         } else {
        //             if (node->left == nullptr) {
        //                 node->left = newNode;
        //                 break;
        //             }
        //             node = node->left;
        //         }
        //     }
        // }
        
        // void path_rotate(std::stack<TNode<T>*> &path) {
        //     while(!path.empty()) {
        //         TNode<T> *node = path.top();
        //         if (node->left && node->left->priority > node->priority) {
        //             right_rotate(node);
        //         }
        //         if (node->right && node->right->priority > node->priority) {
        //             left_rotate(node);
        //         }
        //         path.pop();
        //     }
        // }
};

int main() {
    // Treap<int> treap;
    // treap.insert(10);
    // treap.insert(15);
    // treap.insert(5);
    // treap.insert(20);
    // treap.insert(25);
    int data[5] {1,2,3,4,5};
    Treap<int> treap(data, 5);
    treap.remove(3);
    treap.print();
    treap.insert(6, 3);
    treap.print();
    auto split = treap.split(5);
}