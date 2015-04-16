package test.others;

import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mbm.elint.entity.DocInfo;
import com.mbm.elint.entity.dao.DocInfoDao;

public class SpringAnnationTest {
	public static ApplicationContext context =new FileSystemXmlApplicationContext();
	
	public DocInfoDao getDocInfoDao(){
//		System.out.println("context--------------------"+context);
//		System.out.println("docInfoDao--------------------"+context.getBean("docInfoDao"));
//		DocInfoDao<DocInfo> dao=(DocInfoDao<DocInfo>)context.getBean("docInfoDao");
		
		return null;
	}
	
	public static void main(String[] args) {
//		HibernateGeneralDao hibernateGeneralDao=(HibernateGeneralDao)context.getBean("hibernateGeneralDao");
//		DocInfoDao docInfoDao=(DocInfoDao)context.getBean("docInfoDao");
//		
//		DocInfo docInfo=new DocInfo();
//		docInfoDao.save(docInfo);
//	    System.out.println(docInfo);   
	    //System.out.println(user.getUserName()); 
//		ApplicationContext context =new ClassPathXmlApplicationContext("applicationContext.xml");;
//		System.out.println("context------------"+context);
	}

}
