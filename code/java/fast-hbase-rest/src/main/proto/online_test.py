#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import message_pb2

import urllib2
def raiseHTTPRequest(url,data=None,timeout=3):
    # if we do post, we have to provide data.
    f=urllib2.urlopen(url,data,timeout)
    return f.read()

def queryColumn():
    print '----------queryColumn----------'
    msg = message_pb2.Message()
    msg.type = message_pb2.Message.kReadRequest
    request = msg.readRequest
    
    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'
    request.qualifiers.append('14_day_active_count_avg')

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://dp0:12345',data,timeout=20)

    msg = message_pb2.Message()    
    msg.ParseFromString(data2)
    print msg

if __name__=='__main__':
    queryColumn()
    



