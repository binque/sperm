#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

from datetime import datetime
import calendar
import time
import sys
import traceback
import urllib2

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

def exceptionToString(sep='\n'):
    exc_type, exc_value, exc_traceback = sys.exc_info()
    return sep.join(traceback.format_exception(exc_type, exc_value, exc_traceback))

if __name__=='__main__':
    UTCDateTime.unittest()
    LocalDateTime.unittest()
    try:
        raise Exception('hello')
    except:
        print exceptionToString()

        
