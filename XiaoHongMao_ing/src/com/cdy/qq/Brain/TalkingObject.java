package com.cdy.qq.Brain;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQGroup;
import iqq.im.bean.QQMsg;
import iqq.im.bean.content.ContentItem;
import iqq.im.bean.content.FaceItem;
import iqq.im.bean.content.FontItem;
import iqq.im.bean.content.OffPicItem;
import iqq.im.bean.content.TextItem;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

import java.util.Date;
import java.util.List;

import com.caidongyu.application.log.LogUtil;
import com.caidongyu.application.text.StringUtil;
import com.cdy.qq.core.QQRobot;
import com.cdy.qq.core.XiaoHongMao;

/**
 * @author dongyu.cai 
 * 交谈对象
 */
public class TalkingObject {
	
	/**
	 * 待发送的消息
	 */
	public QQMsg msgWaitToSend;
	public PrivateMemory privateMemory = new PrivateMemory(); 
	public OthersMemory othersMemory = new OthersMemory();
	private CommandType command = CommandType.talk;//默认是聊天模式
	private QQRobot myRobot = null;//QQ机器人
	public TalkingObject() {}
	

	/**
	 * @author dongyu.cai
	 * 是否需要记忆和回复
	 */
	private enum NeedReply{
		Y,N;
	}
	
	/**
	 * @author caidongyu
	 * 命令类型
	 */
	private enum CommandType{
		talk,//聊天模式
		administrator,//管理员模式
		over;//什么都不做,掠过
	}
	
	/**
	 * 根据收到的消息自动交谈
	 * @param protectedMemory 
	 */
	public void talkingAuto(final XiaoHongMao xiaoHongMao,QQMsg msgReceived, ProtectedMemory protectedMemory){
		List<ContentItem> items = msgReceived.getContentList();
		for(ContentItem item : items) {
			if(item.getType() == ContentItem.Type.FACE) {
				System.out.print(" Face:" + ((FaceItem)item).getId());
			}else if(item.getType() == ContentItem.Type.OFFPIC) {
				System.out.print(" Picture:" + ((OffPicItem)item).getFilePath());
			}else if(item.getType() == ContentItem.Type.TEXT) {
				
				String message = ((TextItem)item).getContent();
				message = StringUtil.isBlank(message)?"":message.trim();
				
				NeedReply nr = null;
		        switch(msgReceived.getType()){
		        case BUDDY_MSG:
		        	//判定是否是administrator模式
		        	//好友交谈可以开启
		        	if(command == CommandType.administrator){
		        		//如果已经是管理员模式,需要推出才会进入正常聊天模式
		        		//CST
		        		if(!"#talk".equals(message)){
		        			break;
		        		}
		        	}
		        	command = CommandType.talk;//默认是聊天模式
		        	
		        	//CST
		        	if("#admin".equals(message)){
		        		//开启管理员模式
		        		command = CommandType.administrator;
		        		//CST
		        		String console = "管理员模式开启,请回复您希望的操作.\n";
		        		replyMessage(xiaoHongMao, msgReceived, console);
		        		return;
		        	}
		        	
		        	//需要回复
		        	nr = NeedReply.Y;
		        	break;
		        case DISCUZ_MSG:
		        	//讨论组只聊天
		        	command = CommandType.talk;
		        	//如果是对机器人说的，需要回复，否则，不用回复
		        	nr = NeedReply.N;
		        	//如果是叫了机器人，就需要回复了
		        	//CST
		        	if(message.startsWith("小红帽")){
		        		nr = NeedReply.Y;
		        		message = message.substring(3);
		        		if(StringUtil.isBlank(message)){
		        			message="小红帽";
		        		}
		        	}
		        	break;
		        case GROUP_MSG:
		        	//群只聊天
		        	command = CommandType.talk;
		        	//如果是对机器人说的，需要回复，否则，不用回复
		        	nr = NeedReply.N;
		        	//如果是叫了机器人，就需要回复了
		        	//CST
		        	if(message.startsWith("小红帽")){
		        		nr = NeedReply.Y;
		        		message = message.substring(3);
		        		if(StringUtil.isBlank(message)){
		        			message="小红帽";
		        		}
		        	}
		        	break;
		        case SESSION_MSG:
		        	//零时对话不开启
		        	command = CommandType.over;
		        	//先暂时不需要记忆和回复
		        	nr = NeedReply.N;
		        	break;
		        default:
		        	//零时对话不开启
		        	command = CommandType.over;
		        	//未知来源的消息，不用回复，也不用记忆
		        	nr = NeedReply.N;
		        	break;
		        }
		        
		        switch(command){
			        case talk:
			        	thinkingAndResponse(xiaoHongMao, msgReceived, protectedMemory,message,nr);
			        	break;
			        case administrator:
			        	administratorTalk(xiaoHongMao, msgReceived, protectedMemory,message,nr);
			        	break;
			        case over:
			        	break;
		        	default:break;
		        }
			}
		}

	}
	
	/**
	 * 管理员操作
	 */
	private void administratorTalk(final XiaoHongMao xiaoHongMao, final QQMsg msgReceived,
			ProtectedMemory protectedMemory, String message, NeedReply nr) {
		//CST
		if("start".equals(message)){
			//开启robot
			if(myRobot == null){
				myRobot = new QQRobot("1840658279", "123456terry",xiaoHongMao, msgReceived);
			}
			if(myRobot.client.isLogining()){
				replyMessage(xiaoHongMao, msgReceived, "正在登陆请稍后");
				return;
			}
			myRobot.login();					//CST
//			replyMessage(xiaoHongMao, msgReceived, "登陆成功");
			message = "ls";//CST //接着就刷新群列表并展示出来
		}
		
		
		//其他命令必须在start之后
		if(myRobot == null){
			//CST
			replyMessage(xiaoHongMao, msgReceived, "请先start你的QQ机器人");
			return;
		}
		
		//展示所有群信息
		if("ls".equals(message)){
			myRobot.client.getGroupList(new QQActionListener() {
				@Override
				public void onActionEvent(QQActionEvent event) {
					if(event.getType() == Type.EVT_OK) {
						StringBuffer groupList = new StringBuffer();
						int index = 1;
						for(QQGroup g : myRobot.client.getGroupList()) {
							System.out.println("Group: " + g.getName());
							groupList.append(index++).append(".").append(g.getName()).append("[").append(g.getGid()).append("]").append("\n");
						}
						replyMessage(xiaoHongMao, msgReceived, groupList.toString());
					} else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                        System.out.println("** 群列表获取失败，处理重新获取");
                    }
				}
			});
			return;
		}
		
		//设置目标群
		//CST			
		if(message.startsWith("set")){
			String[] arg = message.split(" ");
			String reply = "";
			//CST
			if("set".equals(arg[0])){
				if(arg.length >= 2){
					String[] groupIndexAry = arg[1].split(",");
					//WST
				}
			}else{
				reply = "set命令格式不正确";//CST
			}
			replyMessage(xiaoHongMao, msgReceived, reply);
			return;
		}
		
		//停止QQ机器人
		//CST
		if("stop".equals(message)){
			if(myRobot.client.isLogining()){
												//CST
				replyMessage(xiaoHongMao, msgReceived, "正在登陆请稍后");
				return;
			}
			if(myRobot.client.isOnline()){			//CST
				replyMessage(xiaoHongMao, msgReceived, "正在退出请稍后");
				myRobot.client.logout(new QQActionListener() {
					@Override		
					public void onActionEvent(QQActionEvent event) {
						if(event.getType() == QQActionEvent.Type.EVT_OK){
							replyMessage(xiaoHongMao, msgReceived, "已下线");//CST
						}
					}
				});
			}
			/*//清理robot
			if(myRobot != null){
				myRobot = null;
			}*/
			return;
		}
		
	}

	public void disaptchMesgToGroup(QQClient client,QQMsg msgReceived){
		if(msgWaitToSend != null){
			List<ContentItem> items = msgWaitToSend.getContentList();
			QQMsg sendMsg = new QQMsg();
			for(ContentItem item : items) {
				if(item.getType() == ContentItem.Type.FACE) {
					sendMsg.addContentItem(item);
				}else if(item.getType() == ContentItem.Type.OFFPIC) {
					sendMsg.addContentItem(item);
				}else if(item.getType() == ContentItem.Type.TEXT) {
					sendMsg.addContentItem(item);
				}
			}
			sendMsg.setDate(new Date());
			sendMsg.setGroup(msgReceived.getGroup());
			sendMsg.setType(msgReceived.getType());
			sendMsg.setId(msgReceived.getId());
			sendMsg.setId2(msgReceived.getId2());
			sendMsg.setTo(client.getAccount());
			sendMsg.addContentItem(new FontItem());             // 使用默认字体
			client.sendMsg(sendMsg,new QQActionListener() {
				@Override
				public void onActionEvent(QQActionEvent event) {
					if(event.getType() == QQActionEvent.Type.EVT_OK){
						LogUtil.print("消息推送成功~");
						msgWaitToSend = null;
					}else{
						LogUtil.print("消息推送失败!");
					}
				}
			});   // 调用接口发送消息
		}
	}
	/**
	 * 思考发送过来的话并回答
	 * @param msgReceived 
	 * @param nr 
	 */
	public String thinkingAndResponse(final XiaoHongMao xiaoHongMao, QQMsg msgReceived,
			ProtectedMemory protectedMemory, String question,
			NeedReply nr) {
		switch(nr){
			case Y:break;
			case N:return null;//不用回复
			default:break;
		}
		
		question = StringUtil.isBlank(question)?"":question.trim();
		
		
		if(StringUtil.isBlank(question)){
			//空的话，就不回话
			return null;
		}
		
		
		//有对这句话，有好多种方式来回答
		//先在自我小空间找记忆
		String answer = privateMemory.getMemory(question);
		if(StringUtil.isBlank(answer)){
			//找不到就去共享空间找记忆
			answer = protectedMemory.getMemory(question);
			if(StringUtil.isBlank(answer)){
				//如果这里也没有，就去网上找吧
				AskAndAnswerMODEL model = othersMemory.getBaiDuZhiDaoSearchResult(question);
				if(model != null){
					answer = model.answer;
				}else{
					//CST
					answer = "你问倒我啦，我也不知道呀，你教教我吧?";
				}
			}
		}else{
			//找到了，需要查看下共享空间里是否有这个记忆，是否一样，没有或者不一样的也要同步下
			protectedMemory.saveMemory(question, answer);			//CST
		}
		
        return replyMessage(xiaoHongMao,msgReceived, answer);
	}
	
	private String replyMessage(final XiaoHongMao xiaoHongMao, QQMsg msgReceived,String replyMessage){
		// 组装QQ消息发送回去
        QQMsg sendMsg = new QQMsg();
        sendMsg.setDate(new Date());
        sendMsg.setDiscuz(msgReceived.getDiscuz());
        sendMsg.setFrom(msgReceived.getTo());
        sendMsg.setGroup(msgReceived.getGroup());
        sendMsg.setId(msgReceived.getId());
        sendMsg.setId2(msgReceived.getId2());
        sendMsg.setTo(msgReceived.getFrom());
        sendMsg.setType(msgReceived.getType());
        // QQ内容
//        sendMsg.addContentItem(new FaceItem(74));            // QQ id为0的表情
        sendMsg.addContentItem(new TextItem(replyMessage));      // 添加文本内容
        sendMsg.addContentItem(new FontItem());             // 使用默认字体
        xiaoHongMao.client.sendMsg(sendMsg, null);     // 调用接口发送消息
        return replyMessage;
	}
}
