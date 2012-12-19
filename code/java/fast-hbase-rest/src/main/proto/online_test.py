#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import message_pb2

request = message_pb2.Request()
request.table_name='appbenchmark'
request.row_key='2012-04-08_YULE'
request.column_family='stat'
request.qualifiers.append('14_day_active_count_avg')

data = request.SerializeToString()

import urllib2
def raiseHTTPRequest(url,data=None,timeout=3):
    # if we do post, we have to provide data.
    f=urllib2.urlopen(url,data,timeout)
    return f.read()

back = raiseHTTPRequest('http://dp0:12345',data,timeout=20)

response = message_pb2.Response()
response.ParseFromString(back)

print response
print response.table_name


