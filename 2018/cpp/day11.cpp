// compile with max optimization for speed:
// g++ -std=c++20 -O3 -o day11 day11.cpp

#include <iostream>
using namespace std;

const int grid_serial = 7803;
const int grid_size = 300;

int grid[grid_size][grid_size];

typedef struct {
  int x, y, val;
} MaxSquare;

int cell_power(int x, int y) {
  ++x; ++y;
  return ((((x + 10) * y) + grid_serial) * (x + 10)) / 100 % 10 - 5;
}

void fill_grid() {
  for (int x = 0; x < grid_size; ++x) {
    for (int y = 0; y < grid_size; ++y) {
      grid[x][y] = cell_power(x, y);
    }
  }
}

int square_power(int x, int y, int size) {
  int power = 0;
  for (int i = x; i < x + size; ++i) {
    for (int j = y; j < y + size; ++j) {
      power += grid[i][j];
    }
  }
  return power;
}

MaxSquare max_square(int size) {
  MaxSquare max_power = {0, 0, square_power(0, 0, size)};
  for (int x = 0; x <= grid_size - size; ++x) {
    for (int y = 0; y <= grid_size - size; ++y) {
      int power = square_power(x, y, size);
      if (power > max_power.val) {
        max_power.x = x;
        max_power.y = y;
        max_power.val = power;
      }
    }
  }
  return max_power;
}

int main() {
  fill_grid();
  MaxSquare part1 = max_square(3);
  cout << part1.x + 1 << " " << part1.y + 1 << " " << "\n";

  MaxSquare part2 = {0, 0, square_power(0, 0, 1)};
  int max_size = 1;

  for (int size = 1; size <= grid_size; ++size) {
    // cout << size << "\n";
    MaxSquare interim = max_square(size);
    // if (interim.val < 0) break;
    if (interim.val > part2.val) {
      part2 = interim;
      max_size = size;
    } 
  }
  cout << part2.x + 1 << " " << part2.y + 1 << " " << max_size << "\n";

  return 0;
}
