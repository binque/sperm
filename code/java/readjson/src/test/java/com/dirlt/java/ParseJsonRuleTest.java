/* coding:utf-8
 * Copyright (C) dirlt
 */

package com.dirlt.java;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParseJsonRuleTest {
    private static final Logger LOG = Logger.getLogger(ParseJsonRuleTest.class);

    @Before
    public void setUp() throws Exception {
        // TODO(dirlt):
    }

    @After
    public void tearDown() throws Exception {
        // TODO(dirlt):
    }

    @Test
    public void testInstallRecord1() throws Exception {
        String cond = "[\"and\",{\"install-date\":[[\"2012-02-12\",\"2012-08-12\"]]}]";
        MarkMatcher matcher = new MarkMatcher(cond);
        InstallRecord ir = new InstallRecord();
        ir.installAtClient = "2012-07-11";
        Assert.assertEquals(matcher.isMatch(ir), true);
        ir.installAtClient = "2012-01-11";
        Assert.assertEquals(matcher.isMatch(ir), false);
    }

    @Test
    public void testInstallRecord2() throws Exception {
        String cond = "[\"and\",{\"install-date\":[[\"2012-02-12\",\"2012-08-12\"],[\"2012-01-10\",\"2012-02-11\"]]}]";
        MarkMatcher matcher = new MarkMatcher(cond);
        InstallRecord ir = new InstallRecord();
        ir.installAtClient = "2012-07-11";
        Assert.assertEquals(matcher.isMatch(ir), true);
        ir.installAtClient = "2012-01-11";
        Assert.assertEquals(matcher.isMatch(ir), true);
    }

    @Test
    public void testInstallRecord3() throws Exception {
        String cond = "[\"not\",[\"and\",{\"install-date\":[[\"2012-02-12\",\"2012-08-12\"],[\"2012-01-10\",\"2012-02-11\"]]}]]";
        MarkMatcher matcher = new MarkMatcher(cond);
        InstallRecord ir = new InstallRecord();
        ir.installAtClient = "2012-07-11";
        Assert.assertEquals(matcher.isMatch(ir), false);
        ir.installAtClient = "2012-01-11";
        Assert.assertEquals(matcher.isMatch(ir), false);
    }

    @Test
    public void testDeviceInfoRecord1() throws Exception {
        String cond = "[\"and\",{\"country\":[\"china\",\"usa\",\"mexico\"]}]";
        MarkMatcher matcher = new MarkMatcher(cond);
        DeviceInfoRecord dir = new DeviceInfoRecord();
        dir.createCountry = "usa";
        Assert.assertEquals(matcher.isMatch(dir), true);
        dir.createCountry = "brazil";
        Assert.assertEquals(matcher.isMatch(dir), false);
    }
}
