#!/usr/bin/env python
#coding:utf-8
#Copyright (C) dirlt

import happybase

# create connection.
connection = happybase.Connection('localhost', autoconnect = False)
connection.open()

# create table.
kTableName = 't1'
kColumnFamily = 'cf'

if (kTableName in connection.tables()):
    print 'disable_table...'
    connection.disable_table('t1')
    print 'delete table...'
    connection.delete_table('t1')

print 'create_table...'
connection.create_table(kTableName, {kColumnFamily:{}})
table = connection.table(kTableName)

# put data.
print 'put data...'
table.put('r1', {kColumnFamily+':c1':'value1'})

# get data.
print 'get data...'
row = table.row('r1')
assert(row[kColumnFamily + ':c1'] == 'value1')

# tables.
print connection.tables()

# # scan
# print 'do scan...'
# iters = table.scan()

# for k,v in iters:
#     print k,v
    

