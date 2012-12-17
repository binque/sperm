#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import message_pb2

request = message_pb2.Request()
request.table_name='t1'
request.row_key='t1.fk'
request.column_family='cf'
request.qualifiers.append('cl')

data = request.SerializeToString()

import urllib2
def raiseHTTPRequest(url,data=None,timeout=3):
    # if we do post, we have to provide data.
    f=urllib2.urlopen(url,data,timeout)
    return f.read()

back = raiseHTTPRequest('http://localhost:8000',data,timeout=20)

response = message_pb2.Response()
response.ParseFromString(back)

print response
print response.table_name



