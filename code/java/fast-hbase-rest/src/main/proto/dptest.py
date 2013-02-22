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

def queryColumn():
    print '----------queryColumn----------'
    request = message_pb2.ReadRequest()

    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'
    request.qualifiers.append('14_day_active_count_avg')

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://dp0:12345/read',data,timeout=20)

    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    #print response

def queryColumnFamily():
    print '----------queryColumnFamily----------'
    request = message_pb2.ReadRequest()

    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://dp0:12345/read',data,timeout=20)

    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    #print response

def multiQuery():
    print '----------multiQuery----------'
    mRequest = message_pb2.MultiReadRequest()

    request = message_pb2.ReadRequest()
    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'
    request.qualifiers.append('14_day_active_count_avg')
    mRequest.requests.extend([request])

    request = message_pb2.ReadRequest()
    request.table_name='appbenchmark'
    request.row_key='2012-04-08_YULE'
    request.column_family='stat'
    mRequest.requests.extend([request])

    data = mRequest.SerializeToString()
    data2 = raiseHTTPRequest('http://dp0:12345/multi-read',data,timeout=20)
    
    mResponse = message_pb2.MultiReadResponse()
    mResponse.ParseFromString(data2)
    #print mResponse

    
if __name__=='__main__':
    queryColumn()
    queryColumnFamily()
    multiQuery()
