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
    request = message_pb2.ReadRequest()

    request.table_name='t1'
    request.row_key='r1'
    request.column_family='cf'
    request.qualifiers.append('key')

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000/read',data,timeout=20)

    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    print response

def queryColumnFamily():
    print '----------queryColumnFamily----------'
    request = message_pb2.ReadRequest()

    request.table_name='t1'
    request.row_key='r1'
    request.column_family='cf'

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000/read',data,timeout=20)

    response = message_pb2.ReadResponse()
    response.ParseFromString(data2)
    print response

def write():
    print '----------write----------'
    request = message_pb2.WriteRequest()

    request.table_name = 't1'
    request.row_key = 'r0'
    request.column_family = 'cf'
    kv = request.kvs.add()
    kv.qualifier = 'key'
    kv.content = 'value'

    data = request.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000/write',data,timeout=20)

    response = message_pb2.WriteResponse()
    response.ParseFromString(data2)
    print response
    

if __name__=='__main__':
    queryColumn()
    queryColumnFamily()
    write()
