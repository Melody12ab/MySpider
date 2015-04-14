package developing;

import com.cdy.qzone.core.QZone;
import com.cdy.qzone.util.SimpleUtil;

public class Main {

	public static void main(String[] args) {
//		System.out.println(DateUtil.formatDateToStr(new Date(1422116227), "yyyy-MM-dd hh:mm:ss"));
		
//		QZone qq = new QZone("1157656909","xxx!1991");
		QZone qq = new QZone("1827985499","Aa1234");
		//登录
		qq.login();
//		System.out.println(qq.getCookie());
		try {
			//点赞
//			qq.praise("673872882", "f27b2a28a6ecc1540e6d0300");
//			qq.praiseFriendsNewShuoShuoTimely("673872882,1827985499");
//			String result = SendRequestUtil.doRequest("http://w.cnc.qzone.qq.com/cgi-bin/likes/internal_dolike_app?g_tk=295017352", "get", null, null, qq.getCookie(), null, null);
//			System.out.println(result);
		
			//获取说说
//			String result = qq.getLastShuoShuo("1157656909");
//			System.out.println(result);
			
			//查看说说是否点赞过
//			qq.getShuoShuoPraiseInfo("1157656909", "4d71004583c5c35482f80400");//4d71004583c5c35482f80400

			//创建相册
//			String result = qq.addAlbum("1157656909");
//			System.out.println(result);
			
			/*final int[] arg = {1,2};
			
			new Thread(){
				public void run() {
					
					while(true){
						
						for(int a:arg){
							System.out.print(a);
						}
						try {
							System.out.println("+++++++");
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}.start();
		*/
			
			
			//发布一条说说
//			qq.publishOneShuoShuo("为什么~不~说话~");
			
			//获取所有好友关系
//			String result = qq.getMyFriendShip();
//			System.out.println(result);
			
			//获取qq的最近访问者
//			String result = qq.getVisitorByQQ("79594225");
//			System.out.println(SimpleUtil.getCallBackContent(result));
			
			//发送私密留言
			for(int i=1;true;){
				String result = qq.sendMsg("79594225", "能不能看到，截个图");
				System.out.println(result);
				i++;
				Thread.sleep(100);
			}
//			for(int i=1;i<11;i++){
//				String result = qq.sendMsg("673872882", "妈妈你在干嘛");
//				System.out.println(result);
//				Thread.sleep(1000);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
