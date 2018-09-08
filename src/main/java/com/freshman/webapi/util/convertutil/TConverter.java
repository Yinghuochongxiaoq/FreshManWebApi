package com.freshman.webapi.util.convertutil;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TConverter {
    /**
     * 转为字符串
     *
     * @param value
     * @return
     */
    public static String toSafeString(Object value) {
        try {
            return (value == null) ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 转为long型
     *
     * @param value
     * @return
     */
    public static long ObjectToLong(Object value) {
        return ObjectToLong(value, 0l);
    }

    /**
     * 转为long型
     *
     * @param value
     * @param defalutValue
     * @return
     */
    public static long ObjectToLong(Object value, long defalutValue) {
        if (value == null) {
            return defalutValue;
        }
        String strValue = value.toString();
        if (strValue.indexOf(".") >= 0)
            strValue = strValue.substring(0, strValue.indexOf("."));
        try {
            return Long.parseLong(strValue);
        } catch (Exception ex) {
            return defalutValue;
        }
    }

    /**
     * 转为short
     *
     * @param value
     * @param defualtValue
     * @return
     */
    public static short ObjectToShort(Object value, short defualtValue) {
        if (value == null) {
            return defualtValue;
        }
        String strValue = value.toString();
        if (strValue.indexOf(".") >= 0)
            strValue = strValue.substring(0, strValue.indexOf("."));

        try {
            return Short.parseShort(strValue);
        } catch (Exception ex) {
            return defualtValue;
        }
    }

    /**
     * 转为short
     *
     * @param value
     * @return
     */
    public static short ObjectToShort(Object value) {
        return ObjectToShort(value, (short) 0);
    }

    /**
     * 转为BigDecimal对象
     *
     * @param value
     * @param defualtValue
     * @return
     */
    public static BigDecimal ObjectToBigDecimal(Object value, BigDecimal defualtValue) {
        if (value == null) {
            return defualtValue;
        }
        String strValue = value.toString();
        try {
            return new BigDecimal(strValue);
        } catch (Exception ex) {
            return defualtValue;
        }
    }

    /**
     * 转为BigDecimal对象
     *
     * @param value
     * @return
     */
    public static BigDecimal ObjectToBigDecimal(Object value) {
        if (value == null) {
            return new BigDecimal(0);
        }
        String strValue = value.toString();
        try {
            return new BigDecimal(strValue);
        } catch (Exception ex) {
            return new BigDecimal(0);
        }
    }


    /**
     * 转为int
     *
     * @param value
     * @param defualtValue
     * @return
     */
    public static int ObjectToInt(Object value, int defualtValue) {
        if (value == null) {
            return defualtValue;
        }

        String strValue = value.toString();
        if (strValue.indexOf(".") >= 0)
            strValue = strValue.substring(0, strValue.indexOf("."));
        try {
            return Integer.parseInt(strValue);
        } catch (Exception ex) {

        }
        return defualtValue;
    }

    /**
     * 转为int
     *
     * @param value
     * @return
     */
    public static int ObjectToInt(Object value) {
        return ObjectToInt(value, 0);
    }

    /**
     * 转为float
     *
     * @param value
     * @param defualtValue
     * @return
     */
    public static float ObjectToFloat(Object value, float defualtValue) {
        if (value == null) {
            return defualtValue;
        }
        String strValue = value.toString();
        try {
            return Float.parseFloat(strValue);
        } catch (Exception ex) {
            return defualtValue;
        }
    }

    /**
     * 转为float
     *
     * @param value
     * @return
     */
    public static float ObjectToFloat(Object value) {
        return ObjectToFloat(value, 0);
    }

    /**
     * 获取列表首元素
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T GetFirstOrDefualt(List<T> list) {
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    /**
     * 安全转换类型
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T safeConvert(Object obj) {
        try {
            if (obj == null)
                return null;

            return (T) obj;
        } catch (Exception ex) {
            return null;
        }
    }


    /**
     * 对象转为map
     *
     * @param thisObj
     * @return
     */
    public static Map<String, Object> convertObjToMap(Object thisObj) {
        Map map = new HashMap();
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get")) {
                    try {
                        Object value = m[i].invoke(thisObj);
                        if (value != null) {
                            String key = method.substring(3);
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                            map.put(key, value);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 将Map中的list对应的值转为对象
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T GetListFromMap(Object result) {
        if (result == null)
            return null;
        Map<String, Object> map = (Map<String, Object>) result;
        Object list = map.get("list");
        if (list == null)
            return null;
        return (T) list;
    }
}