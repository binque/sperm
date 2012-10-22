/* coding:utf-8
 * Copyright (C) dirlt
 */

#include <cassert>
#include <cstdio>
#include "nasty/nasty.h"
using namespace sperm::nasty;

int main() {
  Parser p("test.in");
  Expr* e=p.run();
  assert(e);  
  return 0;
}
