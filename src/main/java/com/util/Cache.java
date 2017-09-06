package com.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: chenqimiao
 * Date: 2017/9/6
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class Cache {

    public static Map<String,String> dictionary = new ConcurrentHashMap<String, String>();

}
