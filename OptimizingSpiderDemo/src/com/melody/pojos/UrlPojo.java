package com.melody.pojos;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.melody.enumeration.TaskLevel;

public class UrlPojo {
    private String url;
    private TaskLevel taskLevel = TaskLevel.MIDDLE;
    private Map<String,Object> parasMap;

    public Map<String, Object> getParasMap() {
        return parasMap;
    }

    public void setParasMap(Map<String, Object> parasMap) {
        this.parasMap = parasMap;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public TaskLevel getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(TaskLevel taskLevel) {
        this.taskLevel = taskLevel;
    }

    public UrlPojo(String url, TaskLevel taskLevel) {
        this.url = url;
        this.taskLevel = taskLevel;
    }

    public UrlPojo(String url) {
        this.url = url;
    }

    public UrlPojo(String url, Map<String, Object> parasMap) {
        this.url = url;
        this.parasMap = parasMap;
    }

    @Override
    public String toString() {
        return "UrlPojo [url=" + url + ", taskLevel=" + taskLevel + "]";
    }

    public HttpURLConnection getConnection() {
        try {
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                return (HttpURLConnection) connection;
            } else {
                throw new Exception("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHost() {
        try {
            URL url = new URL(this.url);
            return url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连不上网咯！");
        }
        return null;
    }

}
