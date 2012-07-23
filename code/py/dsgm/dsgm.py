#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt


import os
import time
import sys
import socket
import gmail
import distutils.dir_util
import hashlib
from util import *

class Subject:
    def __init__(self,subject):        
        subject=toLocal(subject)
        assert(subject.startswith('[dsgm]'))
        subject=subject[len('[dsgm]'):]
        (x,y)=subject.split('#')
        (filename,md5)=x.split('/')
        dirs=y.split('/')
        
        self._filename=filename
        self._md5=md5;
        self._dirs=dirs

    def fileName(self):
        return self._filename
    def md5(self):
        return self._md5
    def dirs(self):
        return self._dirs

# use multi-thread complicate it, and much tended to be blocked by gmail.
# so stick to single thread.
class DSGM:
    def __init__(self):
        self._logger=getLogger('DSGM')
        self._config=readConfig()
        self._db=readDB()
        self._directory=self._config['document.directory']
        distutils.dir_util.mkpath(self._directory)
        self._m=gmail.ImapGmail(self._config['gmail.username'],
                                self._config['gmail.password'])

    def open(self):
        retry=3
        while(True):            
            try:
                self._m.login()
                self._m.createMailbox('dsgm')
                self._m.selectMailbox('dsgm')
                break
            except Exception,e:
                retry-=1
                if(retry>=0):
                    self._logger.warn('network problem, exception=%s. wait for 1 sec to reconnect',e)
                    time.sleep(1)
                else:
                    self._logger.error('network problem, exception=%s. exit with 3 retries',e)
                    sys.exit(-1)

    def close(self):
        self._m.logout()
        self._db.close()

    # step1
    def pullUids(self):
        retry=3
        while(True):
            try:
                uids=self._m.requestAllUids()
                break
            except Exception,e:
                entry-=1
                if(entry<0):                    
                    self._logger.error('requestAllUids failed, exception=%s. exit with 3 retries',e)
                    sys.exit(-1)
                else:
                    self._logger.warn('requestAllUids failed, exception=%s. retry...',e)
                    self.open()

        # just return necessary ones.
        # important!!!

        # construct uids.
        keys=self._db.keys()            
        _uids={}
        for uid in uids:
            _uids[uid]='!'

        # update local db.
        for k in keys:
            v=self._db[k]
            if(not v in _uids): # not exists means we delete gmail.
                # then we need to update local db.
                self._db[k]='0' # flag deleted.

        # construct local db.
        _dbuids={}
        for k in keys:            
            v=self._db[k]
            if(v=='0'): # deleted one.
                continue
            _dbuids[v]=k
        
        # construct download uids.
        ruids=[]
        for uid in uids:
            if(not uid in _dbuids):
                ruids.append(uid)
        return ruids
        
    # step2
    def pullSubjectsAndUpdateToDB(self,uids):
        for uid in uids:
            retry=3
            while(True):
                try:
                    header=self._m.requestBodyHeader(uid)
                    sub=self._m.parseSubjectFromBodyHeader(header)
                    if(sub.startswith('[dsgm]')):
                        self._db[sub]=uid
                    else:
                        self._logger.info('abnormal subject=%s',sub)
                    break
                except Exception,e:
                    retry-=1
                    if(retry<0):
                        self._logger.warn('requestBodyHeader uid=%s exception=%s, ignored. file maybe duplicated!!!',uid,e) 
                        break
                    self._m.logout()
                    self._logger.warn('requestBodyHeader uid=%s exception=%s, retry...',uid,e)
                    self.open()
    
    # step3
    def isFileIgnored(self, filename):
        base=os.path.basename(filename)
        if(base == '.dsgm.db'): # internal db.
            return True

        (root,ext)=os.path.splitext(base)
        if(not isExtSupported(ext)):
            return True

        # Z-C{md5}.doc 32bit.
        p=root.split('-')[-1]
        if(p.startswith('C') and
           len(p)==33):
            self._logger.info('filename %s ignored'%(filename))
            return True

        return False

    def md5sum(self,filename):
        m=hashlib.md5()
        m.update(open(filename,'rb').read())
        return m.hexdigest()

    def constructLocalDirectoryInfo(self):
        info={}
        def walk(arg,dirname,names):
            _dirname=dirname[len(self._directory):]
            if(_dirname.startswith(os.sep)):
                _dirname=_dirname[1:]
            _dirname=_dirname.replace(os.sep,'/')

            names=filter(lambda x:os.path.isfile(os.path.join(dirname,x)) and
                         not self.isFileIgnored(x),names)
            for name in names:
                filename=os.path.join(dirname,name)
                try:
                    md5=self.md5sum(filename)
                    sub='[dsgm]%(name)s/%(md5)s#%(_dirname)s'%(locals())
                    sub=toUTF8(sub)
                    info[sub]=filename # as local encoding.
                except Exception,e:
                    self._logger.info('md5sum file %s exception=%s',
                                      filename, e)
                    continue
        os.path.walk(self._directory,walk,None)
        return info

    def downloadAttachment(self,s,content):
        dirname=os.path.join(self._directory,
                             os.sep.join(s.dirs()))
        distutils.dir_util.mkpath(dirname)
        if(os.path.exists(os.path.join(dirname,s.fileName()))):
            # conflict.
            self._logger.info('conflict dir=%s, file=%s'%(dirname,s.fileName()))
            (root,ext)=os.path.splitext(s.fileName())
            filename='%s-C%s%s'%(root,s.md5(),ext)
        else:
            filename=s.fileName()
        filename=os.path.join(dirname,filename)
        open(filename,'wb').write(content)

    # step4
    def pullItem(self,item,info):
        (k,uid)=item
        if(uid=='0'): # deleted item.
            return 
        if(k in info): # already have it.
            return 
        # already download mark as conflict.            
        s=Subject(k)
        (root,ext)=os.path.splitext(s.fileName())
        filename=os.path.join(self._directory,
                              os.sep.join(s.dirs()),
                              '%s-C%s%s'%(root,s.md5(),ext))
        if(os.path.exists(filename)):
            return 

        # download it
        self._logger.info('fetchMail subject=%s...',k)
        retry=3
        while(True):
            try:
                body=self._m.requestBody(uid)
                break
            except Exception,e:  # which exception is better?
                retry-=1
                if(retry<0):
                    self._logger.warn('fetchMail subject=%s exception=%s, ignored',k,e)
                    return 
                self._m.logout()
                self._logger.warn('fetchMail subject=%s exception=%s, retry...',k,e)
                self.open()
        
        attachments=self._m.parseAttachmentFromBody(body)
        if(len(attachments)!=1):
            self._logger.warn('mail subject=%s attachments=%d', k, len(attachments))
            return 

        self._logger.info('fetchMail subject=%s over',k)
        (_,content)=attachments[0]
        self.downloadAttachment(s,content)

    def pull(self,info):
        for k in self._db.keys():
            v=self._db[k]
            self.pullItem((k,v),info)

    def pushItem(self,item):
        (k,v)=item
        if(self._db.has_key(k)):
            _uid=self._db[k]
            if(_uid!='0'): # not deleted on gmail.
                # otherwise we need to upload it again!
                return
        
        self._logger.info('sendMail subject=%s...',k)
        retry=3
        while(True):
            try:
                self._m.sendMail('dsgm','dsgm',('dsgm',),k,None,(v,))
                break
            except Exception,e: # which exception is better?
                retry-=1
                if(retry<0):
                    self._logger.warn('sendMail subject=%s exception=%s, ignored',k,e)
                    return
                self._m.logout()
                self._logger.warn('sendMail subject=%s exception=%s, retry...',k,e)
                self.open()            
        self._logger.info('sendMail subject=%s over',k)
        
    def push(self,info):
        for item in info.items():
            self.pushItem(item)
