/* coding:utf-8
 * Copyright (C) dirlt
 */

#ifndef __SPERM_CC_NASTY_NASTY_H__
#define __SPERM_CC_NASTY_NASTY_H__

namespace sperm {
namespace nasty {

class Parser {
 public:  
}; // class Parser

class Token {
 public:
  enum {
    RS,
    SR,
    ID,
    DBL,
    INT,
  };
  Token(int type, const char* text, int leng, int line, int column):
      type_(type), s_(text, leng), line_(line), column_(column) {
  }  
 private:
  int type_;
  std::string s_;
  int line_;
  int column_;
}; // class Token

} // namespace nasty
} // namespace sperm

#endif // __SPERM_CC_NASTY_NASTY_H__
