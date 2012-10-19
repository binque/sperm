%{
  
#define SAVE_TOKEN yylval.string = new std::string(yytext, yyleng)
  
%}

%option 8bit
%option reentrant
%option bison-bridge
%option bison-locations
%option yywrap
%option warn
%option nodefault
%option verbose
%option prefix="yy"
%option header-file="nasty.l.h"
%option outfile="nasty.l.c"
     
%%
     
"(" { SAVE_TOKEN; return LBRACE; }
")" { SAVE_TOKEN; return RBRACE; }
[a-zA-Z_][a-zA-Z0-9_]* { SAVE_TOKEN; return ID; }
[0-9]+\.[0-9]* { SAVE_TOKEN; return VDBL; }
[0-9]+  { SAVE_TOKEN; return VINT; }
[ \t\n] ;
. yyterminate();

%%


