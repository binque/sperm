/* coding:utf-8
 * Copyright (C) dirlt
 */

#include "common/lockfree.h"
typedef common::lockfree::SampleMemoryAllcator<int> IntSampleMemoryAllocator;
  
int main() {
  IntSampleMemoryAllocator ma;
  IntSampleMemoryAllocator::Entry* e=ma.alloc();
  ma.dealloc(e);
  return 0;
}
