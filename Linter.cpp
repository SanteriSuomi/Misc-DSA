#include <fstream>
#include <iostream>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

// Implements a very basic (and very badly programmed) variable linter

std::vector<std::string> datatypes{"int"};

std::unordered_map<std::string, std::string>
    global;  // variable name | variable type global
std::unordered_map<std::string, std::string>
    local;  // variable name | variable type local

int gettype(std::string &line, std::string *type, size_t pos) {
    for (const auto &d : datatypes) {
        pos = line.find(d, pos);
        if (pos != std::string::npos) {
            pos += d.length();
            *type = d;
            break;
        }
    }
    return pos;
}

// Return true if is a function
size_t getname(std::string &line, std::string *name, size_t pos) {
    for (pos++; pos < line.length() && line[pos] != ' ' && line[pos] != ';'; pos++) {
        if (line[pos] == '(') {
            return pos;
        }
        (*name) += line[pos];
    }
    return 0;
}

void checkname(std::string *name) {
    if (global.count(*name) > 0) {
        std::cerr << "Variable " << *name << " is already declared globally!"
                  << "\n";
    } else if (local.count(*name) > 0) {
        std::cerr << "Variable " << *name << " is already declared locally!"
                  << "\n";
    }
}

void getparameters(std::string &line, size_t pos, std::string *type,
                   std::string *name) {
    *type = "";
    *name = "";
    bool b = true;
    bool end = false;
    for (pos++; pos < line.length(); pos++) {
        end = line[pos] == ')';
        if (line[pos] == ',' || end) {
            checkname(name);
            local[*name] = *type;
            std::cout << "Param. Type: " << *type << "\n";
            std::cout << "Param. Name: " << *name << "\n";
            *type = "";
            *name = "";
            if (end) break;
            pos++;
            b = !b;
        } else if (line[pos] == ' ') {
            b = !b;
        } else if (b) {
            *type += line[pos];
        } else {
            *name += line[pos];
        }
    }
}

std::ifstream getfile() {
    std::string filePath;
    std::ifstream file(filePath);
    while (!file.good()) {
        std::cout << "Give file path: ";
        std::getline(std::cin, filePath);
        file = std::ifstream(filePath);
    }
    return file;
}

void getvalue(std::string &line, std::string *value) {
    size_t pos = line.find('=');
    if (pos == std::string::npos) {
        *value = "";
    } else {
        for (pos++; pos < line.length(); pos++) {
            if (line[pos] == ';') {
                return;
            } else if (line[pos] != ' ') {
                *value += line[pos];
            }
        }
    }
}

void checkvalue(std::string &line, std::string *value, std::string *name) {
    getvalue(line, value);
    if (value->empty()) {
        std::cerr << "Variable " << *name << " may not have been initialized!"
                  << "\n";
    } else {
        std::cout << "Value: " << *value << "\n";
    }
}

void processline(std::ifstream &file, std::string &line, std::string *type,
                 std::string *name, std::string *value, bool &infunction) {
    if (line.empty() || line[0] == '/')
        return;
    else if (line.find('}') && infunction) {
        infunction = false;
        local.clear();
        return;
    }
    std::cout << "Line: " << line << "\n";
    size_t pos = gettype(line, type, 0);
    if (!type->empty()) {
        size_t length = getname(line, name, pos);
        if (length > 0) {
            infunction = true;
            std::cout << "Function '" << *name << "' start"
                      << "\n";
            getparameters(line, length, type, name);
        } else {
            std::cout << "Type: " << *type << "\n";
            std::cout << "Name: " << *name << "\n";
            checkname(name);
            checkvalue(line, value, name);
            if (infunction) {
                local[*name] = *type;
            } else {
                global[*name] = *type;
            }
        }
        *type = "";
        *name = "";
        *value = "";
    }
}

void lint(std::ifstream &file) {
    std::string line;
    bool infunction = false;
    if (file.is_open()) {
        std::string *type = new std::string;
        std::string *name = new std::string;
        std::string *value = new std::string;
        while (std::getline(file, line)) {
            processline(file, line, type, name, value, infunction);
        }
        delete type;
        delete name;
        delete value;
    }
}

int main() {
    std::ifstream file = getfile();
    lint(file);
}