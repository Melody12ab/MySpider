package com.melody.impl.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.melody.iface.parser.IHtmlParser;
import com.melody.manager.WangYiDaiCrawlManager;
import com.melody.pojos.WangYiDaiItemPojo;
import com.melody.utils.JSONOperatorUtil;

public class HtmlParserToObjectImpl4WangYiDai implements
        IHtmlParser<List<WangYiDaiItemPojo>> {

    public static String[] column_key = { "platName", "locationAreaName",
            "locationCityName", "platUrl" };

    @Override
    public List<WangYiDaiItemPojo> parser(String htmlSource) {
        JSONObject jsonObject = JSONOperatorUtil
                .parserStrToJSONObject(htmlSource);
        JSONArray jsonArray = JSONOperatorUtil.parserStrToJSONArray(jsonObject
                .get("list").toString());
        List<WangYiDaiItemPojo> list=new ArrayList<>();
        for (Object json : jsonArray) {
            WangYiDaiItemPojo itemPojo = new WangYiDaiItemPojo();
            JSONObject itemobj = (JSONObject) json;
            itemPojo.setPlatName(itemobj.get(column_key[0]).toString());
            itemPojo.setLocationAreaName(itemobj.get(column_key[1]).toString());
            itemPojo.setLocationCityName(itemobj.get(column_key[2]).toString());
            itemPojo.setPlatUrl(itemobj.get(column_key[3]).toString());
            list.add(itemPojo);
            WangYiDaiCrawlManager.item_count++;
        }
        return list;
    }

}
