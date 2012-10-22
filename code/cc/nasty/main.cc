/* coding:utf-8
 * Copyright (C) dirlt
 */

#include <cstdio>
#include "nasty/nasty.h"
using namespace sperm::nasty;
int main() {
  Parser p("test.in");
  Expr* e=p.run();
  printf("%p\n",e);
  return 0;
}
