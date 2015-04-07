package com.melody.utils;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONOperatorUtil {
    public static JSONObject parserStrToJSONObject(String str){
        return (JSONObject) JSONValue.parse(str);
    }
    public static JSONArray parserStrToJSONArray(String str){
        return (JSONArray) JSONValue.parse(str);
    }
    public static void main(String[] args) {
        String str="[{\"one\":1,\"two\":2}]";
//        JSONObject jsonObject=parserStrToJSONObject(str);
//        if(jsonObject.containsKey("2")){
//            System.out.println(jsonObject.get("two"));
//        }else{
//            System.out.println("not contain the key!");
//        }
        JSONArray array=parserStrToJSONArray(str);
        Iterator<JSONObject> iterator=array.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
