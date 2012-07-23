/*
 * Copyright (C) dirlt
 */

#include <pthread.h>
#include "common/queue.h"
#include "common/lock.h"
#include "common/cond.h"
#include "common/utils.h"

using namespace common;

class Item {
public:
  Item* prev;
  Item* next;
}; // class Item;

typedef SyncedQueue< STLDeque< Item* > , Item* , SpinLock, FutexCondition > Q;
//typedef SyncedQueue< STLDeque< Item* > ,Item* ,SpinLock, PipePairCondition > Q;
static const int kConsumerThread = 2;
static const int kProducerThread = 2;

void* consumer_thread_function(void* arg) {
  Q* q = static_cast<Q*>(arg);
  while(1) {
    Item* item = NULL;
    if(!q->pop(&item, 1000)) {
      SPERM_DEBUG("consumer Q empty");
      continue;
    }
    delete item;
  }
  return NULL;
}

void* producer_thread_function(void* arg) {
  Q* q = static_cast<Q*>(arg);
  while(1) {
    Item* item = new Item();
    q->push(item);
    //sleep_ms(500); // 500 ms.
  }
  return NULL;
}

int main() {
  Q q;
  pthread_t ctid[kConsumerThread];
  pthread_t ptid[kProducerThread];
  SPERM_DEBUG("consumer thread...");
  for(int i = 0; i < kConsumerThread; i++) {
    pthread_create(ctid + i, NULL, consumer_thread_function, &q);
  }
  SPERM_DEBUG("producer thread...");
  for(int i = 0; i < kProducerThread; i++) {
    pthread_create(ptid + i, NULL, producer_thread_function, &q);
  }
  for(int i = 0; i < kConsumerThread; i++) {
    pthread_join(ctid[i], NULL);
  }
  for(int i = 0; i < kProducerThread; i++) {
    pthread_join(ptid[i], NULL);
  }
  return 0;
}
