#coding:utf-8
#Copyright (C) dirlt

libhello.so:hello.cc native.h
	g++ -o $@ $< -shared -fPIC -O2 -ltcmalloc -I${JAVA_HOME}/include

native.h:
	javah -o native.h -classpath ../target/classes com.dirlt.java.jni.Main

clean:
	rm -rf libhello.so