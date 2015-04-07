package com.melody.impl.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.melody.iface.parser.IHtmlParser;
import com.melody.manager.WangYiDaiCrawlManager;
import com.melody.utils.JSONOperatorUtil;

public class HtmlParserImpl4WangYiDai implements IHtmlParser {

    public static String[] column_key = { "platName", "locationAreaName",
            "locationCityName", "platUrl" };

    @Override
    public String parser(String htmlSource) {
        StringBuilder builder = new StringBuilder();
        JSONObject jsonObject = JSONOperatorUtil
                .parserStrToJSONObject(htmlSource);
        JSONArray jsonArray = JSONOperatorUtil.parserStrToJSONArray(jsonObject
                .get("list").toString());
        for (Object json : jsonArray) {
            JSONObject itemobj = (JSONObject) json;
            for (String column : column_key) {
                // System.out.print(itemobj.get(column)+"  ");
                builder.append(itemobj.get(column) + "\t");
            }
            WangYiDaiCrawlManager.item_count++;
            builder.append("\r\n");
        }
        return builder.toString();
    }

}
