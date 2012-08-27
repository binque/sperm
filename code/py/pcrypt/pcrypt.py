#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

from PIL import Image
import math
import string

def decrypt(f, t):
    im=Image.open(f)
    (h,w)=im.size
    s=[]
    # collect all pixels.
    for i in xrange(0,h):
        for j in xrange(0,w):
            pix=im.getpixel((i,j))
            s.extend(pix)
    # parse length.
    # first two RGBs represents length.
    length=0
    for i in range(0,6):
        length=length*256 + s[i]
    open(t,'wb').write(string.join(map(lambda x:chr(x),s[6:6+length]),''))

def encrypt(f, t):
    s=open(f,'rb').read()
    length=len(s)
    pix=[]
    for i in range(0,6):
        pix.append(length % 256)
        length /= 256
    pix.reverse()
    length=len(s)
    for i in xrange(0,length):
        pix.append(ord(s[i]))
        
    # construct height and width.    
    actual=(len(pix)+2)/3 # round to 3.
    width=int(math.sqrt(actual))
    height=(actual+width-1)/width
    actual2=width*height    
    pix.extend((0,)* (actual2 * 3 -len(pix)))

    # write it out.
    im=Image.new("RGB", (height, width))
    offset=0
    for i in xrange(0,height):
        for j in xrange(0,width):
            im.putpixel((i,j),
                        (pix[offset],
                         pix[offset+1],
                         pix[offset+2]))
            offset+=3
    im.save(t,"PNG")

if __name__=='__main__':    
    encrypt('pcrypt.py','pcrypt.png')
    decrypt('pcrypt.png','pcrypt.py.ok')
    import os
    assert(os.system('diff pcrypt.py pcrypt.py.ok')==0)
    
