runnable_so:runnable_so.o
	g++ -o $@ $^ -shared -e so_main

%.o:%.cc
	g++ -o $@ $^ -fPIC -c -g
clean:
	rm -rf runnable_so runnable_so.o
