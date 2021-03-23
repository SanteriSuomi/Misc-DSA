#include <initializer_list>
#include <iostream>
#include <cmath>

class Point {
    private:
        double *coords;
    public:
        Point(std::initializer_list<double> &coords) {
            this->coords = new double[coords.size()];
            int i = 0;
            for(const auto &j : coords) {
                this->coords[i] = j;
                i++;
            }
        }

        double& at(int i) {
            return coords[i];
        } 

        double& operator[](int); 

        ~Point() {
            delete coords;
            coords = nullptr;
        }
};

double& Point::operator[](int i) {
    return coords[i]; 
} 

class Node {
    public:
        Node *left, *right;
        Point *point;

        Node(std::initializer_list<double> &coords) {
            point = new Point(coords);
            left = nullptr;
            right = nullptr;
        }

        ~Node() {
            delete point;
            point = nullptr;
        }
};

class KDTree {
    private:
        int dimensions;
        Node *root;

        double distance(Point *first, Point* second) {
            double res;
            for(int i = 0; i < dimensions; i++) {
                double j = (*first)[i] - (*second)[i];
                res += pow(j, 2);
            }
            return sqrt(res);
        }

        Node* closest(Node *first, Node *second, Point *target) {
            if (!first) return second;
            if (!second) return first;
            double f = distance(first->point, target);
            double s = distance(second->point, target);
            return f < s ? first : second;
        }

        Node* nearest_rec(Node *root, Point* target, int level) {
            if (!root) return nullptr;
            Node *nextBranch;
            Node *otherBranch;
            if (target->at(level % dimensions) <= root->point->at(level % dimensions)) {
                nextBranch = root->left;
                otherBranch = root->right;
            } else {
                nextBranch = root->right;
                otherBranch = root->left;
            }
            Node *temp = nearest_rec(nextBranch, target, level + 1);
            Node *best = closest(temp, root, target);
            if (distance(best->point, target) > distance(root->point, target)) {
                temp = nearest_rec(nextBranch, target, level + 1);
                best = closest(temp, root, target);
            }
            return best;
        }
    public:
        KDTree(int dimensions) {
            this->dimensions = dimensions;
            root = nullptr;
        }

        void insert(std::initializer_list<double> coords) {
            if (coords.size() != dimensions) {
                throw "The number of coordinates does not equal the number of dimensions!";
            }
            int level = 0;
            Node *newNode = new Node(coords);
            if (root == nullptr) {
                root = newNode;
                return;
            }
            Node *cur = root;
            while(cur) {
                if (newNode->point->at(level % dimensions) <= cur->point->at(level % dimensions)) {
                    if (cur->left == nullptr) {
                        cur->left = newNode;
                        return;
                    }
                    cur = cur->left;
                } else {
                    if (cur->right == nullptr) {
                        cur->right = newNode;
                        return;
                    }
                    cur = cur->right;
                }
                level++;
            }
        }

        void remove(std::initializer_list<double> coords) {
            
        }

        Point* nearest(Point point) {
            return nearest_rec(root, &point, 0)->point;
        }

        Point* nearest(std::initializer_list<double> coords) {
            Point point(coords);
            return nearest_rec(root, &point, 0)->point;
        }
};

int main() {
    KDTree kdtree(2);
    kdtree.insert({2, 5});
    kdtree.insert({1, 2});
    kdtree.insert({4, 5});
    kdtree.insert({0, 2});
    Point *n = kdtree.nearest({1, 2});
    std::cout << n->at(0) << " " << n->at(1) << "\n";
}