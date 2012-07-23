#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

# simple implementation
import string
import distutils.dir_util
import os

# simple config
class SimpleConfig:
    def __init__(self):
        self._dict={}
    
    def readFile(self,filename):
        content=open(filename).read()
        self.readString(content)
        
    def readString(self,content):
        xs=filter(lambda x:x and x[0]!='#',
                  map(lambda x:string.strip(x),
                      content.split('\n')))
        for x in xs:
            (k,v)=map(lambda x:string.strip(x),x.split('=',1))
            self._dict[k]=v
    
    def __getitem__(self,k):
        return self._dict[k]

    def __setitem__(self,k,v):
        self._dict[k]=v

    def has_key(self,k):
        return (k in self._dict)

    def keys(self):
        return self._dict.keys()

    def size(self):
        return len(self._dict)

    def writeString(self):
        return '\n'.join(map(lambda x:x[0] + ' = ' + x[1],self._dict.items()))
    
    def writeFile(self,filename):
        content=self.writeString()+'\n'
        open(filename,'w').write(content)

    @staticmethod
    def selfTest():
        sf=SimpleConfig()
        sf.readString("# comment\nkey1 = value1\nkey2 = value2\n")
        assert(sf['key1']=='value1')
        assert(sf['key2']=='value2')
        content=sf.writeString()
        
        sf.readString(content)
        assert(sf['key1']=='value1')
        assert(sf['key2']=='value2')
    

class SimpleDB(SimpleConfig):
    def __init__(self,dbname):
        SimpleConfig.__init__(self)
        distutils.dir_util.mkpath(os.path.dirname(dbname))
        self._dbname=dbname
        if(os.path.exists(dbname)):
            self.readFile(dbname)
    
    def __setitem__(self,k,v):
        # append the dbname.
        # now we don't sync.
        open(self._dbname,'a').write('%s = %s\n'%(k,v))
        SimpleConfig.__setitem__(self,k,v)
        
    def close(self):
        # we overwrite it.
        self.writeFile(self._dbname)
