/* coding:utf-8
 * Copyright (C) dirlt
 */

#include "common/logger_inl.h"
#include "nasty/nasty.h"
#include "nasty/nasty.y.hh"
#include "nasty/nasty.l.hh"

int yyparse(void* scanner, sperm::nasty::Parser* parser);

namespace sperm {
namespace nasty {

void Parser::free() {
  delete ex_;
  ex_ = 0;
}

Parser::~Parser() {
  free();  
}

Expr* Parser::run() {
  yyscan_t scanner;
  yylex_init(&scanner);
  FILE* fin = fopen(f_.c_str(), "rb");
  if(fin==0) { // nothing warning.
    SPERM_WARNING("open(%s) failed(%s)",f_.c_str(), SERRNO);
    return 0;
  }
  yyset_in(fin, scanner);  
  if(yyparse(scanner, this)!=0) {
    free();
    fclose(fin);
    return 0;
  }
  return ex_;
}

void Expr::AppendAtom(Atom* at) {
  es_.push_back(at);
}

Expr::~Expr() {
  for(size_t i=0;i<es_.size();i++){
    delete es_[i];
  }
}

Atom::~Atom() {
  delete ex_;
  ex_ = 0;
}

} // namespace nasty
} // namespace sperm
