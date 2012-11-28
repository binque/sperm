#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

from PIL import Image
import math
import string
import os

""" encode bytes to picture and decode back """

def decode(f, t):
    im=Image.open(f)
    (h,w)=im.size
    
    # collect all pixels.
    s=[]
    for i in xrange(0,h):
        for j in xrange(0,w):
            pix=im.getpixel((i,j))
            s.extend(pix)
            
    # parse length.
    # first two RGBs represents length.
    lth = 0 
    for i in range(0,6):
        lth = lth * 256 + s[i]

    data = string.join(map(lambda x:chr(x),s[6:6+lth]),'')
    open(t,'wb').write(data)    
    
def encode(f, t):
    stbuf = os.stat(f)
    if stbuf.st_size > 2 ** 48:
        return (False,'file size exceeds 2^48')
    
    # read content.
    s=open(f,'rb').read()    
    lth=len(s)
    
    # encode length into header.
    lth2 = lth   
    pix=[]
    for i in range(0,6):
        pix.append(lth2 % 256)
        lth2 /= 256 # 2^8.
    pix.reverse()

    # encode content in byte.
    for i in xrange(0,lth):
        pix.append(ord(s[i]))
        
    # construct height and width.
    lth = len(pix) 
    size = (lth + 3 - 1) / 3 # each cell takes 3 pixels.
    # make a approximatesquare.
    w = int(math.sqrt(size))
    h = (size + w - 1) / w
    size2 = w * h
    # fill zeros.
    pix.extend((0,)* (size2 * 3 - lth))

    # write it out.
    im=Image.new("RGB", (h,w))
    idx = 0
    for i in xrange(0,h):
        for j in xrange(0,w):
            im.putpixel((i,j),(pix[idx],pix[idx+1],pix[idx+2]))
            idx += 3
    im.save(t,"PNG")
    return (True,'OK')

def unittest():
    print 'making unittest...'
    encode('b2p.py','/tmp/out.png')
    decode('/tmp/out.png','/tmp/b2p.py')
    def isFileContentSame(x,y):
        dx = open(x,'rb').read()
        dy = open(y,'rb').read()
        return dx == dy
    assert(isFileContentSame('b2p.py','/tmp/b2p.py'))

if __name__ == '__main__':
    unittest()
    
