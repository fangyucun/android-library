/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.applib.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jason Tool
 *
 * Create on 2014年12月6日 下午12:27:31
 *
 * @author Jason Fang
 */
public final class JsonUtils {
	static final boolean DEBUG = false;

	private JsonUtils(){}
	
    public static Map<String, Object> parse(String jsonString) {
        Map<String, Object> result = null;

        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                result = parseJsonObject(jsonObject);
            } catch (JSONException e) {
            	if (DEBUG) Flog.e(e);
            }
        }
        return result;
    }

	private static Map<String, Object> parseJsonObject(JSONObject jsonObject) throws JSONException {
        Map<String, Object> valueObject = null;
        
        if (jsonObject != null) {
            valueObject = new HashMap<>();
            Iterator<String> keyIter = jsonObject.keys();
            
            while (keyIter.hasNext()) {
                String keyStr = keyIter.next();
                Object itemObject = jsonObject.opt(keyStr);
                if (itemObject != null) {
                    valueObject.put(keyStr, parseValue(itemObject));
                }
            }
        }
        return valueObject;
    }
	
    private static Object parseValue(Object inputObject) throws JSONException {
    	Object outputObject = null;

        if (inputObject != null) {
            if (inputObject instanceof JSONArray) {
                outputObject = parseJsonArray((JSONArray) inputObject);
            } else if (inputObject instanceof JSONObject) {
                outputObject = parseJsonObject((JSONObject) inputObject);
            } else if (inputObject instanceof String 
            		|| inputObject instanceof Boolean 
            		|| inputObject instanceof Integer) {
                outputObject = inputObject;
            }
        }
        return outputObject;
    }

    private static List<Object> parseJsonArray(JSONArray jsonArray) throws JSONException {
        List<Object> valueList = null;

        if (null != jsonArray) {
            valueList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object itemObject = jsonArray.get(i);
                if (null != itemObject) {
                    valueList.add(parseValue(itemObject));
                }
            } 
        }
        return valueList;
    }
    
    public static boolean has(JSONObject object, String key) {
        return object != null && object.has(key);
    }
    
    public static JSONObject getJsonObject(String jsonString) {
    	try {
			return new JSONObject(jsonString);
		} catch (JSONException e) {
			if (DEBUG) Flog.e(e);
		}
    	return null;
    }
    
    public static Object get(JSONObject object, String name) {
    	if (object != null && object.has(name)) {
    		try {
				return object.get(name);
			} catch (JSONException e) {
				if (DEBUG) Flog.e(e);
			}
    	}
    	return null;
    }
    
    public static Object get(JSONArray array, int index, String name) {
    	return get(JsonUtils.getJsonObject(array, index), name);
    }
    
    public static String getString(JSONObject object, String name) {
    	return StringUtils.valueOf(get(object, name));
    }
    
    public static String getString(JSONArray array, int index, String name) {
    	return StringUtils.valueOf(get(array, index, name));
    }
    
    public static int getInt(JSONArray array, int index) {
    	if (array == null || array.length() <= 0) return -1;
    	try {
    		return array.getInt(index);
    	} catch (JSONException e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return -1;
    }
    
    public static int getInt(JSONArray array, int index, String name) {
    	int i = -1;
    	try {
    		i = Integer.valueOf(StringUtils.valueOf(get(array, index, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return i;
    }
    
    public static long getLong(JSONObject object, String name) {
    	long l = -1;
    	try {
    		l = Long.valueOf(StringUtils.valueOf(get(object, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return l;
    }
    
    public static long getLong(JSONArray array, int index, String name) {
    	long l = -1;
    	try {
    		l = Long.valueOf(StringUtils.valueOf(get(array, index, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return l;
    }
    
    public static double getDouble(JSONObject object, String name) {
    	double d = 0;
    	try {
    		d = Double.valueOf(StringUtils.valueOf(get(object, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return d;
    }

    public static double getDouble(JSONArray array, int index, String name) {
    	double d = -1;
    	try {
    		d = Double.valueOf(StringUtils.valueOf(get(array, index, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return d;
    }
    
    public static int getInt(JSONObject object, String name) {
    	int i = -1;
    	try {
    		i = Integer.valueOf(StringUtils.valueOf(get(object, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return i;
    }
    
    public static boolean getBoolean(JSONObject object, String name) {
    	boolean b = false;
    	try {
    		b = Boolean.parseBoolean(StringUtils.valueOf(get(object, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return b;
    }
    
    public static boolean getBoolean(JSONArray array, int index, String name) {
    	boolean b = false;
    	try {
    		b = Boolean.parseBoolean(StringUtils.valueOf(get(array, index, name)));
    	} catch (Exception e) {
    		if (DEBUG) Flog.e(e);
    	}
    	return b;
    }
    
    public static JSONArray getJsonArray(JSONObject object, String name) {
    	if (object != null && object.has(name)) {
	    	try {
				return object.getJSONArray(name);
			} catch (JSONException e) {
				if (DEBUG) Flog.e(e);
			}
    	}
    	return new JSONArray();
    }
    
    public static JSONObject getJsonObject(JSONObject object, String name) {
    	if (object != null && object.has(name)) {
    		try {
    			return object.getJSONObject(name);
    		} catch (JSONException e) {
    			if (DEBUG) Flog.e(e);
    		}
    	}
    	return null;
    }
    
    public static JSONObject getJsonObject(JSONArray array, int index) {
    	if (array != null && array.length() > 0) {
    		try {
    			return array.getJSONObject(index);
    		} catch (JSONException e) {
    			if (DEBUG) Flog.e(e);
    		}
    	}
    	return new JSONObject();
    }

}
