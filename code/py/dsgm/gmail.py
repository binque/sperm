#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import os
import string
import urllib
import time
import email
import email.header
import base64
import imaplib
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email.mime.text import MIMEText    
from email import encoders
from util import *

# we wrapper it because we need to control timeout.
class IMAP4_SSL2(imaplib.IMAP4_SSL):
    def __init__(self,host,port):
        self._logger=getLogger('IMAP4_SSL2')
        imaplib.IMAP4_SSL.__init__(self,host,port)
        
    def read(self,size):        
        #self._logger.debug('read size=%d bytes',size)
        if(size < 1000): # header.
            self.socket().settimeout(None)
        else:
            self._logger.info('settimeout=60 secs')
            self.socket().settimeout(60) # 60s
        return imaplib.IMAP4_SSL.read(self,size)

    def send(self,data):        
        #self._logger.debug('send size=%d bytes',len(data))
        if(len(data) < 1000): # header.
            self.socket().settimeout(None)
        else:
            self._logger.info('settimeout=60 secs')
            self.socket().settimeout(60) # 60s
        return imaplib.IMAP4_SSL.send(self,data)

# imap client for accessing gmail.
class ImapGmail:
    def __init__(self,username,password):
        self._username=username
        self._password=password

    def login(self):
        self._logger=getLogger('ImapGmail')
        self._logger.info('IMAP4_SSL imap.gmai.com:993')
        self._m=IMAP4_SSL2('imap.gmail.com',993)
        self._logger.info('login username=%s password=%s',
                           self._username,
                           self._password)
        self._m.login(self._username,self._password)

    def logout(self):
        try:
            self._m.logout()
        except Exception,e:
            self._logger.info('logout exception=%s',e)
            pass

    def createMailbox(self,mailbox):
        self._logger.info('mailbox=%s',mailbox)
        self._m.create(mailbox)
        self._mailbox=mailbox

    def selectMailbox(self,mailbox):
        self._logger.info('mailbox=%s',mailbox)
        self._m.select(mailbox)
        self._mailbox=mailbox

    def requestAllUids(self):
        self._logger.info('uid(SEARCH,ALL)')
        type, data= self._m.uid('SEARCH','ALL')
        if(type != 'OK'):
            self._logger.warn('uid(SEARCH ALL) return None')
            return None
        return data[0].split()

    def requestBodyHeader(self,uid):
        self._logger.info('uid(FETCH,%s,(RFC822.HEADER))',uid)
        type, data=self._m.uid('FETCH',uid,'(RFC822.HEADER)')
        if(type !='OK'):
            self._logger.warn('uid(FETCH,%s,(RFC822.HEADER)) return None',uid)
            return None
        headers=data[0][1].split('\r\n')
        self._logger.debug('headers=%s',headers)
        return headers

    def parseSubjectFromBodyHeader(self,headers):
        subject=''
        on=False
        for header in headers:
            if(on and not header):
                break                
            if(header.startswith('Subject: ') or on):
                on=True
                if(header.startswith('Subject: ')):
                    header=header[len('Subject: '):]
                parts=email.header.decode_header(header)
                # as string encode in utf8.
                subject+=''.join(map(lambda x:x[0].decode(x[1] or 'utf8').encode('utf8'),parts))
        return subject

    def mime(self,filename):
        # simple
        (_,ext)=os.path.splitext(filename)
        return mime(ext)

    def sendMail(self,mailbox, f, tos, subject, text=None, files=[]):
        msg = MIMEMultipart()
        msg['From'] = f
        msg['To'] = ','.join(tos)
        msg['Subject'] = subject # as string encode in 'utf8'.
        if(text):
            msg.attach(MIMEText(text))
        for f in files:
            mm=self.mime(f).split('/')
            part = MIMEBase(mm[0],mm[1])
            part.set_payload(open(f, 'rb').read())
            encoders.encode_base64(part) 
            # since we need f local encoding for reading
            # but as attachment name we need encode as UTF8.
            part.add_header('Content-Disposition', 'attachment; filename="%s"' % os.path.basename(toUTF8(f)))
            msg.attach(part)
        self._m.append(mailbox, '', time.localtime(time.time()), msg.as_string())

    def requestBody(self,uid):
        self._logger.info('uid(FETCH,%s,(RFC822))',uid)
        type, data=self._m.uid('FETCH',uid,'(RFC822)')
        if(type != 'OK'):
            self._logger.warn('uid(FETCH,%s,(RFC822)) return None',uid)
            return None        
        return data[0][1]

    def parseAttachmentFromBody(self,body):
        attachments=[]
        msg=email.message_from_string(body)
        assert(msg.is_multipart()) # usually it is!!!
        for part in msg.walk():
            if(part.get_filename()): # find attachment.
                filename=part.get_filename()
                payload=part.get_payload()            
                content=base64.b64decode(payload)
                # filename as string encode in UTF8.
                attachments.append((filename,content))
        return attachments


