package com.melody.impl.save;

import com.melody.iface.save.ISave;
import com.melody.utils.IOutils;
import com.melody.utils.StaticValue;

public class SaveImplToFile4WangYiDai implements ISave<String> {
    private String filePath;
    private String encoding=StaticValue.encoding_default;
    
    public SaveImplToFile4WangYiDai(String filePath, String encoding) {
        this.filePath = filePath;
        this.encoding = encoding;
    }
    

    public SaveImplToFile4WangYiDai(String filePath) {
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
    public boolean save(String t) {
        try {
            IOutils.writeFile(getFilePath(), t, getEncoding());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
