#include <iostream>
using namespace std;

const int players = 459;
const int marbles = 71790;

struct marble {
    int val;
    struct marble *prev;
    struct marble *next;
};

struct marble* delete_marble(struct marble *curr) {
    struct marble *prev = curr->prev;
    struct marble *next = curr->next;
    prev->next = next;
    next->prev = prev;
    delete curr;
    return next;
}

struct marble* insert_marble(struct marble *curr, int val) {
    struct marble *next = curr->next;
    struct marble *node = new marble();
    node->val = val;
    node->prev = curr;
    node->next = next;
    curr->next = node;
    next->prev = node;
    return node;
}

struct marble* next(struct marble *curr, int steps) {
    struct marble *node = curr;
    for (int i = 0; i < steps; ++i) {
        node = node->next;
    }
    return node;
}

struct marble* prev(struct marble *curr, int steps) {
    struct marble *node = curr;
    for (int i = 0; i < steps; ++i) {
        node = node->prev;
    }
    return node;
}

long solve(int marbles) {
    struct marble *circle = new marble();
    circle->val = 0;
    circle->prev = circle;
    circle->next = circle;

    int player = 0;
    long scores[players] = { };

    for (int marble = 1; marble <= marbles; ++marble) {
        if (marble % 23 == 0) {
            circle = prev(circle, 7);
            scores[player] += marble + circle->val;
            circle = delete_marble(circle);
        } else {
            circle = next(circle, 1);
            circle = insert_marble(circle, marble);
        }
        
        player = (player + 1) % players;
    }

    long max_score = *max_element(begin(scores), end(scores));

    return max_score;
}

int main()
{
    cout << solve(marbles) << "\n";
    cout << solve(marbles * 100) << "\n";
 
    return 0;
}