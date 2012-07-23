/*
 * Copyright (C) dirlt
 */

#ifndef __SPERM_CC_COMMON_ATOMIC_H__
#define __SPERM_CC_COMMON_ATOMIC_H__

#include <stdint.h>
#ifdef __x86_64__
#include "common/atomic_x86_64.h"
#else
#include "common/atomic_i386.h"
#endif
#include "common/logger_inl.h"


template< typename T >
T CAS(volatile T& x, T o, T n) {
  if(sizeof(x) == 1) {
    return __arch_compare_and_exchange_val_8_acq(&x, o, n);
  } else if(sizeof(x) == 2) {
    return __arch_compare_and_exchange_val_16_acq(&x, o, n);
  } else if(sizeof(x) == 4) {
    return __arch_compare_and_exchange_val_32_acq(&x, o, n);
  } else if(sizeof(x) == 8) {
    return __arch_compare_and_exchange_val_64_acq(&x, o, n);
  }
  SPERM_FATAL("unknown size=%zu", sizeof(T));
  return 0;
}

template< typename T >
T AtomicGetValue(T& x) {
  return CAS(x, static_cast<T>(0), static_cast<T>(0));
}
#define AtomicSetValue(x,v) (atomic_exchange_acq(&(x),v))
#define AtomicInc(x) (atomic_exchange_and_add(&(x),1)+1)
#define AtomicDec(x) (atomic_exchange_and_add(&(x),-1)-1)

namespace common {

class RefCount {
public:
  RefCount(): ref_count_(1) {}
  ~RefCount() {}
  int32_t acquire() {
    return AtomicInc(ref_count_);
  }
  int32_t release() {
    return AtomicDec(ref_count_);
  }
  int32_t getRefCount() const {
    return AtomicGetValue(ref_count_);
  }
private:
  mutable volatile int32_t ref_count_;
}; // class RefCount

} // namespace common

#endif // __SPERM_CC_COMMON_ATOMIC_H__
