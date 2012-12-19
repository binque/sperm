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

    request.table_name='t1'
    request.row_key='r1'
    request.column_family='cf'
    request.qualifiers.append('key')

    data = msg.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000',data,timeout=20)
    
    msg = message_pb2.Message()
    msg.ParseFromString(data2)
    print msg

def queryColumnFamily():
    print '----------queryColumnFamily----------'
    msg = message_pb2.Message()
    msg.type = message_pb2.Message.kReadRequest
    request = msg.readRequest

    request.table_name='t1'
    request.row_key='r1'
    request.column_family='cf'
        

    data = msg.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000',data,timeout=20)
    
    msg = message_pb2.Message()
    msg.ParseFromString(data2)
    print msg

def write():
    print '----------write----------'
    msg = message_pb2.Message()
    msg.type = message_pb2.Message.kWriteRequest
    request = msg.writeRequest

    request.table_name = 't1'
    request.row_key = 'r0'
    request.column_family = 'cf'
    kv = request.kvs.add()
    kv.qualifier = 'key'
    kv.content = 'value'

    data = msg.SerializeToString()
    data2 = raiseHTTPRequest('http://localhost:8000',data,timeout=20)

    msg = message_pb2.Message()
    msg.ParseFromString(data2)
    print msg
    

if __name__=='__main__':
    queryColumn()
    queryColumnFamily()
    write()



