INCPATHS=-I../
CXX=g++
CXXFLAGS=$(INCPATHS)

main:main.o nasty.o nasty.l.o nasty.y.o
	$(CXX) -o $@ $^
main.o:main.cc
	$(CXX) -o $@ -c $(CXXFLAGS) $<
nasty.o:nasty.cc nasty.h
	$(CXX) -o $@ -c $(CXXFLAGS) $<
nasty.l.o:nasty.l.cc nasty.l.hh nasty.y.hh
	$(CXX) -o $@ -c $(CXXFLAGS) $<
nasty.y.o:nasty.y.cc
	$(CXX) -o $@ -c $(CXXFLAGS) $<
nasty.l.hh nasty.l.cc:nasty.lexer
	flex nasty.lexer
nasty.y.hh nasty.y.cc:nasty.parser
	bison nasty.parser
clean:
	rm -rf *.o nasty.y.output nasty.l.hh nasty.l.cc nasty.y.hh nasty.y.cc main