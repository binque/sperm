#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

from datetime import datetime
import calendar
import time
import sys
import traceback
import urllib2
import socket
import fcntl
import struct

class UTCDateTime:
    @staticmethod
    def dt2ts(s):
        dt = datetime.strptime(s,'%Y-%m-%d %H:%M:%S')
        return calendar.timegm(dt.timetuple())

    @staticmethod
    def ts2dt(ts):
        dt = datetime.utcfromtimestamp(ts)
        return dt.strftime('%Y-%m-%d %H:%M:%S')

    @staticmethod
    def ts2dt2(ts):
        # another implementation.
        st = time.gmtime(ts)
        return time.strftime('%Y-%m-%d %H:%M:%S',st)

    @staticmethod
    def unittest():
        ts = UTCDateTime.dt2ts('2012-10-12 16:00:00')
        print UTCDateTime.ts2dt(ts)
        print UTCDateTime.ts2dt2(ts)

class LocalDateTime:
    @staticmethod
    def dt2ts(s):    
        st = time.strptime(s,'%Y-%m-%d %H:%M:%S')
        return time.mktime(st)

    @staticmethod
    def ts2dt(ts):
        st = time.localtime(ts)
        return time.strftime('%Y-%m-%d %H:%M:%S',st)

    @staticmethod
    def unittest():    
        ts = LocalDateTime.dt2ts('2012-10-12 16:00:00')
        print LocalDateTime.ts2dt(ts)

def raiseHTTPRequest(url,data=None,timeout=3):
    # if we do post, we have to provide data.
    f=urllib2.urlopen(url,data,timeout)
    return f.read()

def printHTTPLatency(url,times=3,timeout=3):
    count  = 0
    print '----------------------------------------'
    print 'testing http latency => %s'%(url)
    scale = 3
    for i in range(0,times):
        start=time.time()
        try:
            raiseHTTPRequest(url,timeout=timeout)
        except urllib2.URLError,e:
            print '[%d]: NaN sec'%(i)
            count += timeout * scale # approximately.
            if (scale < 128):
                scale *= 2
            continue
        end=time.time()
        print '[%d]: %.2lf sec'%(i,end-start)
        count += (end-start)
    print 'avg: %.2lf sec'%(count * 1.0 / times)

def exceptionToString(sep='\n'):
    exc_type, exc_value, exc_traceback = sys.exc_info()
    return sep.join(traceback.format_exception(exc_type, exc_value, exc_traceback))

def getIpAddress(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntoa(fcntl.ioctl(
        s.fileno(),
        0x8915,  # SIOCGIFADDR
        struct.pack('256s', ifname[:15])
    )[20:24])

if __name__=='__main__':
    UTCDateTime.unittest()
    LocalDateTime.unittest()
    try:
        raise Exception('hello')
    except:
        print exceptionToString()
    printHTTPLatency('http://www.baidu.com')
            
