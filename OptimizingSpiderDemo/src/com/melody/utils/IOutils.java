package com.melody.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOutils {
    public static void  writeFile(String filePath,String value,String encoding){
        File file=new File(filePath);
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(file);
            fos.write(value.getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        String filepath="a.txt";
        String value="你好啊 hello";
        String encoding="utf-8";
        writeFile(filepath, value, encoding);
    }
}
