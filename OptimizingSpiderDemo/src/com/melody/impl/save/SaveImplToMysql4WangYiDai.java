package com.melody.impl.save;

import java.util.List;

import com.melody.iface.save.ISave;
import com.melody.pojos.WangYiDaiItemPojo;
import com.melody.utils.IOutils;
import com.melody.utils.StaticValue;

public class SaveImplToMysql4WangYiDai implements ISave<List<WangYiDaiItemPojo>> {
    private String filePath;
    private String encoding=StaticValue.encoding_default;
    
    public SaveImplToMysql4WangYiDai(String filePath, String encoding) {
        this.filePath = filePath;
        this.encoding = encoding;
    }
    

    public SaveImplToMysql4WangYiDai(String filePath) {
        this.filePath = filePath;
    }


    public String getFilePath() {
        return filePath;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getEncoding() {
        return encoding;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    @Override
    public boolean save(List<WangYiDaiItemPojo> list) {
        try {
//            IOutils.writeFile(getFilePath(), t, getEncoding());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
