/* coding:utf-8
 * Copyright (C) dirlt
 */

package com.dirlt.java;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class InstallRecord {
    public String appKey;
    public String umid;
    public String installAtClient;
    public String installAtSerevr;
    public String idmd5;
    public String appVersion;
    public String os;
    public String osVersion;
    public String channel;
}

class DeviceInfoRecord {
    public String umid;
    public String createAt;
    public String createIp;
    public String createCountry;
    public String createProvince;
    public String createCity;
    public String deviceId;
    public String cpu;
    public String model;
    public String os;
    public String osVersion;
    public int resolutionHeight;
    public int resolutionWidth;
    public int isJailBreak;
    public int isPirate;
}

// - ["and",pred,pred,pred,...]
// - ["or",pred,pred,pred,...]
// - ["not",pred]
// - pred
// - {"install-date":[<date-from>,<date-to>],[<date-from>,<date-to>],...} //
// 用于限制新增时间范围，只要匹配一个即可。
// - {"country":[value,value,value,..]} // 将国家限制在这些范围内，只要匹配一个即可。
// - {"province":[value,value,value,...]} // 将省份限制在这些范围内，只要匹配一个即可。
// - {"city":[value,value,value...]} // 将城市限制在这些范围内，只要匹配一个即可。
// - {"event":[value,value,...]} // 出现过某些event，只要匹配一个即可。
// - {"event.label":[value,value,...]} // 出现某个event-label，只要匹配一个即可。
// - {"event.label.value":[k,op,op,op...]} //
// event-lavel-value之需要满足其中一个条件即可。其中k表示event.label.下面是op
// - {"le":value}
// - {"eq":value}
// - {"gt",value}
// - {"in",[value,value,value]} // 匹配一个即可。
// - {"not-in",[value,value,value]} // 不属于其中的任何一个。

class MarkMatcher {
    private JSONArray pred;

    MarkMatcher(String rule) throws JSONException {
        pred=new JSONArray(rule);
    }
    MarkMatcher(JSONArray pred) {
        this.pred = pred;
    }

    private String getFirstKey(JSONObject obj) {
        Iterator<String> iter = obj.keys();
        String key = iter.next();
        return key;
    }

    // we assume date is normal form like 2012-08-12.
    private boolean isInDateRange(String date, String from, String to) {
        if (date.compareTo(from) >= 0 && date.compareTo(to) <= 0) {
            return true;
        }
        return false;
    }
    
    public boolean isMatchInstallRecord(JSONObject obj, InstallRecord record)
            throws JSONException {
        String key = getFirstKey(obj);
        if (key.equals("install-date")) {
            JSONArray vs = obj.getJSONArray(key);
            assert (vs.length() > 0);
            for (int i = 0; i < vs.length(); i++) {
                JSONArray vs2 = vs.getJSONArray(i);
                assert (vs2.length() == 2);
                String from = vs2.getString(0);
                String to = vs2.getString(1);
                if (isInDateRange(record.installAtClient, from, to)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public boolean isMatchDeviceInfoRecord(JSONObject obj,
            DeviceInfoRecord record) throws JSONException {
        String key = getFirstKey(obj);
        if (key.equals("country")) {
            JSONArray vs = obj.getJSONArray(key);
            assert (vs.length() > 0);
            for (int i = 0; i < vs.length(); i++) {
                if (record.createCountry.equals(vs.getString(i))) {
                    return true;
                }
            }
            return false;
        } else if (key.equals("province")) {
            JSONArray vs = obj.getJSONArray(key);
            assert (vs.length() > 0);
            for (int i = 0; i < vs.length(); i++) {
                if (record.createProvince.equals(vs.getString(i))) {
                    return true;
                }
            }
            return false;
        } else if (key.equals("city")) {
            JSONArray vs = obj.getJSONArray(key);
            assert (vs.length() > 0);
            for (int i = 0; i < vs.length(); i++) {
                if (record.createCity.equals(vs.getString(i))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public boolean isMatchR(Object obj, Object record) throws JSONException {
        if (JSONArray.class.isInstance(obj)) {
            JSONArray array = (JSONArray) obj;
            String op = array.getString(0);
            assert (array.length() >= 2);
            if (op.equals("and")) {
                for (int i = 1; i < array.length(); i++) {
                    Object pred = array.get(i);
                    if (!isMatchR(pred, record)) {
                        return false;
                    }
                    return true;
                }
            } else if (op.equals("or")) {
                for (int i = 1; i < array.length(); i++) {
                    Object pred = array.get(i);
                    if (isMatchR(pred, record)) {
                        return true;
                    }
                    return false;
                }
            } else if (op.equals("not")) {
                assert (array.length() == 2);
                Object pred = array.get(1);
                return !isMatchR(pred, record);
            }
        } else if (JSONObject.class.isInstance(obj)) {
            JSONObject jobj = (JSONObject) obj;
            assert (jobj.length() == 1);
            if (InstallRecord.class.isInstance(record)) {
                return isMatchInstallRecord(jobj, (InstallRecord) record);
            } else if (DeviceInfoRecord.class.isInstance(record)) {
                return isMatchDeviceInfoRecord(jobj, (DeviceInfoRecord) record);
            }
        }
        assert ("impossible thing happens" == null);
        return false;
    }

    public boolean isMatch(Object record) throws JSONException {
        return isMatchR(pred, record);
    }
}

public class ParseJsonRule {
    private static final Logger LOG = Logger.getLogger(ParseJsonRule.class);

    public static void main(String[] args) {
        // TODO(dirlt):
    }
}
