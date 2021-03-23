#include <vector>
#include <initializer_list>
#include <iostream>

class Point {
    public:
        double x, y;
        Point(double x, double y) {
            this->x = x;
            this->y = y;
        }
};

class Rectangle {
    public:
        double x, y, w, h;
        Rectangle(double x, double y, double w, double h) {
            this->x = x;
            this->y = y;
            this->w = w;
            this->h = h;
        }

        bool contains(Point *point) {
            return point->x >= x - w 
                && point->x < x + w
                && point->y >= y - h 
                && point->y < y + h;
        }

        bool insersects(Rectangle *range) {
            return !(range->x - range->w > x + w 
                  || range->x + range->w < x - w 
                  || range->y - range->h > y + h 
                  || range->y + range->h < y - h);
        }
};

class Points : public std::vector<Point*> {
    public:
        bool contains(Point *point) {
            for(auto i = begin(); i != end(); i++) {
                if (*i == point) return true;
            }
            return false;
        }
};

class QuadTree {
    private:
        int capacity;
        Rectangle *boundary;
        QuadTree *northWest;
        QuadTree *northEast;
        QuadTree *southWest;
        QuadTree *southEast;
        Points *points;

        void subdivide() {
            double x = this->boundary->x;
            double y = this->boundary->y;
            double w = this->boundary->w;
            double h = this->boundary->h;
            northWest = new QuadTree(x - w / 2, y - h / 2, w / 2, h / 2, capacity);
            northEast = new QuadTree(x + w / 2, y - h / 2, w / 2, h / 2, capacity);
            southWest = new QuadTree(x - w / 2, y + h / 2, w / 2, h / 2, capacity);
            southEast = new QuadTree(x + w / 2, y + h / 2, w / 2, h / 2, capacity);
            divided = true;
        }

        bool insert_do(Point *newPoint) {
            if (!boundary->contains(newPoint)) return false;
            if (points->size() < capacity) {
                points->push_back(newPoint);
                return true;
            } else {
                if (!divided) {
                    subdivide();
                }
                if (northWest->insert_do(newPoint) 
                 || northEast->insert_do(newPoint) 
                 || southWest->insert_do(newPoint) 
                 || southEast->insert_do(newPoint)) {
                    return true;
                }
            }
            return false;
        }

        void query_rec(QuadTree *root, Points *found, Rectangle *range) {
            if (root->boundary->insersects(range)) {
                for(const auto p : *points) {
                    if (range->contains(p) && !found->contains(p)) {
                        found->push_back(p);
                    }
                }
                if (root->divided) {
                    query_rec(root->northWest, found, range);
                    query_rec(root->northEast, found, range);
                    query_rec(root->southWest, found, range);
                    query_rec(root->southEast, found, range);
                }
            }
        }
    public:
        bool divided;

        QuadTree(double x, double y, double w, double h, int capacity) {
            boundary = new Rectangle(x, y, w, h);
            points = new Points();
            this->capacity = capacity;
            northWest = nullptr;
            northEast = nullptr;
            southWest = nullptr;
            southEast = nullptr;
            divided = false;
        }

        bool insert(std::initializer_list<int> list) {
            if (list.size() != 2) throw "Insert only takes two integers as an argument";
            Point *newPoint = new Point(*list.begin(), *(list.end() - 1));
            return insert_do(newPoint);
        }

        Points* query(double x, double y, double w, double h) {
            Points *found = new Points();
            Rectangle *range = new Rectangle(x, y, w, h);
            query_rec(this, found, range);
            delete range;
            return found;
        }
};

int main() {
    QuadTree qd(0, 0, 200, 200, 4);
    qd.insert({172, 98});
    qd.insert({100, 75});
    qd.insert({5, 3});
    qd.insert({101, 75});
    auto query = qd.query(0, 0, 173, 200);
    for(const auto p : *query) {
        std::cout << p->x << " " << p->y << "\n";
    }
}