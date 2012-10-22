all:nasty.l.o nasty.y.o
nasty.l.o:nasty.l.cc nasty.l.hh nasty.y.hh
	g++ -o $@ -c $<
nasty.y.o:nasty.y.cc
	g++ -o $@ -c $<
nasty.l.hh nasty.l.cc:nasty.lexer
	flex nasty.lexer
nasty.y.hh nasty.y.cc:nasty.parser
	bison nasty.parser
clean:
	rm -rf *.o nasty.y.output nasty.l.hh nasty.l.cc nasty.y.hh nasty.y.cc