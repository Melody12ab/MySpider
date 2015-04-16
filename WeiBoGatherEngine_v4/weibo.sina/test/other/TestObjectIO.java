package test.other;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import com.mbm.elint.entity.util.UrlPojo;
import com.weibo.common.utils.ReadSpiderConfig;

public class TestObjectIO {

	public static void main(String[] args) throws Exception{
/*        
		List<UrlPojo> list=new ArrayList<UrlPojo>();
		list.add(new UrlPojo("www.baidu.com",0));
		list.add(new UrlPojo("www.sina.com",1));
		list.add(new UrlPojo("www.sohu.com",2));
		list.add(new UrlPojo("www.163.com",0));
		list.add(new UrlPojo("www.126.com",1));
		list.add(new UrlPojo("www.weibo.com",2));
        
        FileOutputStream output=new FileOutputStream(ReadSpiderConfig.getValue("doc_bloomFilter_file_path"));
        ObjectOutputStream objectOutput=new ObjectOutputStream(output);
        
        objectOutput.writeObject(list);
        System.out.println("写入成功");*/
		
        FileInputStream input=new FileInputStream(ReadSpiderConfig.getValue("doc_bloomFilter_file_path"));
        ObjectInputStream objectInput=new ObjectInputStream(input);
        List<UrlPojo> list=(List)objectInput.readObject();
        
        System.out.println("读入成功");
        UrlPojo url=new UrlPojo("www.sohu.com",2);
        if(list.contains(url)){
        	System.out.println("包含它了");
        }else {
        	System.out.println("不包含");
        }
        url=new UrlPojo("www.sohu.com",8);
        if(list.contains(url)){
        	System.out.println("包含它了");
        }else {
        	System.out.println("不包含");
        }
        
	}

}
