#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import message_pb2
import time

import urllib2
def raiseHTTPRequest(url,data=None,timeout=3):
    # if we do post, we have to provide data.
    f=urllib2.urlopen(url,data,timeout)
    return f.read()

def queryColumn(times = 50):
    print '----------queryColumn----------'
    request = message_pb2.ReadRequest()

    request.table_name='appuserstat'
    request.row_key='2012-10-26_4db949a2112cf75caa00002a'
    request.column_family='stat'
    request.qualifiers.append('models_1_lanCnt')

    data = request.SerializeToString()
    all = 0
    for i in xrange(0,times):
        s = time.time()
        data2 = raiseHTTPRequest('http://dp0:12345/read',data,timeout=20)
        e = time.time()
        all += (e-s)
    print 'fast time spent %.2lf'%(all)

    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    #print response

    all = 0
    for i in xrange(0,times):
        s = time.time()
        data2 = raiseHTTPRequest('http://dp30:8080/appuserstat/2012-10-26_4db949a2112cf75caa00002a/stat:models_1_lanCnt')
        e = time.time()
        all += (e-s)
    print 'rest time spent %.2lf'%(all)

def queryColumnFamily(times = 10):
    print '----------queryColumnFamily----------'
    request = message_pb2.ReadRequest()

    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'

    data = request.SerializeToString()
    all = 0
    for i in xrange(0,times):    
        s = time.time()
        data2 = raiseHTTPRequest('http://dp0:12345/read',data,timeout=20)
        e = time.time()
        all += (e-s)
    print 'fast time spent %.2lf'%(all)
    
    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    #print response

    all = 0
    for i in xrange(0,times):
        s = time.time()
        data2 = raiseHTTPRequest('http://dp30:8080/appbenchmark/2012-04-08_YULE/stat:')
        e = time.time()
        all += (e-s)
    print 'rest time spent %.2lf'%(all)


if __name__=='__main__':
    queryColumn()
    queryColumnFamily()
