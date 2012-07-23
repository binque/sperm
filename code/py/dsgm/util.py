#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import simple

def getLogger(category):
    import logging
    FORMAT = '%(asctime)-15s %(threadName)s [%(levelname)s] [%(name)s] %(funcName)s@%(filename)s:%(lineno)d %(message)s'
    logging.basicConfig(format=FORMAT)
    logger = logging.getLogger(category)
    logger.setLevel(logging.DEBUG)
    return logger

def readConfig():
    import os
    _curdir=os.path.dirname(__file__)
    if(not os.path.exists(os.path.join(_curdir,'dsgm.conf'))):
        logger=getLogger('define')
        logger.fatal('dsgm.conf not exists in directory %s', _curdir)
        sys.exit(-1)
    config=simple.SimpleConfig()
    config.readFile(os.path.join(_curdir,'dsgm.conf'))
    return config

def readDB():
    import os
    _curdir=os.path.dirname(__file__)    
    return simple.SimpleDB(os.path.join(_curdir,'dsgm.db'))

def toUTF8(s):
    try:
        # detect gb2312 first.
        return s.decode('gb2312').encode('utf8')
    except UnicodeDecodeError,e:
        return s
        
def toLocal(s):
    try:
        open(s,'r') # what a tricky way.
        return s
    except IOError,e:
        if(e.errno==22):
            return s.decode('utf8').encode('gb2312')
        return s

def mime(ext):
    ext=ext.lower()
    
    if(ext in ('.pdf',)):
        return 'application/pdf'
    elif(ext in ('.doc','.docx')):
        return 'application/msword'    
    return 'application/octet-stream'

def isExtSupported(ext):
    ext=ext.lower()
    if(ext in ('.doc','.docx','.ppt','.pptx',
               '.pps','.pdf')):
        return True
    return False
