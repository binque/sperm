/* coding:utf-8
 * Copyright (C) dirlt
 */

#include <sys/time.h>
#include <map>
#include <string>
#include <cstdio>

using namespace std;

static inline double gettime_ms() {
  struct timeval tv;
  gettimeofday(&tv, NULL);
  return tv.tv_sec * 1000.0 + tv.tv_usec * 0.001;
}

static const int NUMBER = 10000000;
static const char* PREFIX = "s";
static void action(map<string, long>& dict) {
  char buf[64];
  char buf2[64];
  for(int i = 0; i < NUMBER; i++) {
    snprintf(buf, sizeof(buf), "%s%d", PREFIX, i);
    dict[buf] = i;
  }
  for(int i = 0; i < NUMBER; i++) {
    snprintf(buf, sizeof(buf), "%s%d", PREFIX, i);
    snprintf(buf2, sizeof(buf2), "%s%d", PREFIX, (i + 1000) % NUMBER);
    dict[buf] += dict[buf2];
  }
}

int main() {
  double start = gettime_ms();
  map<string, long> dict;
  action(dict);
  double end = gettime_ms();
  printf("%.2lf\n", end - start);
  return 0;
}
