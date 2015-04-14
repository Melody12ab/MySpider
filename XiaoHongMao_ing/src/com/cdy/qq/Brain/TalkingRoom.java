package com.cdy.qq.Brain;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQGroup;
import iqq.im.bean.QQMsg;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionEvent.Type;

import java.util.HashMap;

import com.cdy.qq.core.XiaoHongMao;

/**
 * @author dongyu.cai
 * 维护正在交谈的联系关系
 */
public class TalkingRoom {

	/**
	 * 交谈时候的对话薄
	 */
	private HashMap<Long,TalkingObject> talkingBook = new HashMap<Long,TalkingObject>();
	/**
	 * 当前账号的记忆空间
	 */
	private ProtectedMemory protectedMemory = new ProtectedMemory();
	
	/**
	 * 新添一个交谈对象，如果存在，就修改
	 */
	public void talking(final XiaoHongMao xiaoHongMao,QQMsg msgReceived){
		TalkingObject talkingObject = null;
		long key = 0;//私有记忆链表的键值
		switch(msgReceived.getType()){
	        case BUDDY_MSG:
	        	key = msgReceived.getFrom().getUin();
	        	break;
	        case DISCUZ_MSG:
	        	key = msgReceived.getDiscuz().getDid();
	        	break;
	        case GROUP_MSG:
	        	key = msgReceived.getGroup().getGin();//gid有时候就是0，真恶心
	        	break;
	        case SESSION_MSG:
	        	break;
	        default:break;
	    }
		if(talkingBook.containsKey(key)){
			talkingObject = talkingBook.get(key);
		}else{
			talkingObject = new TalkingObject();
			talkingBook.put(key, talkingObject);
		}
		talkingObject.talkingAuto(xiaoHongMao, msgReceived,protectedMemory);
	}
	
	private void updateGroupMemory(final QQClient client,final QQMsg msgWaitToSend){
		client.getGroupList(new QQActionListener() {
			@Override
			public void onActionEvent(QQActionEvent event) {
				if(event.getType() == Type.EVT_OK) {
					talkingBook.clear();
					for(QQGroup g : client.getGroupList()) {
						client.getGroupInfo(g, null);
						TalkingObject obj = new TalkingObject();
						obj.msgWaitToSend = msgWaitToSend;
						talkingBook.put(g.getGin(), obj);
					}
				} else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
                    System.out.println("** 群列表获取失败，处理重新获取");
                }
			}
		});
	}
	
	public void dispatchMessageToAllGroups(QQClient client,QQMsg msgReceived){
		switch(msgReceived.getType()){
	        case BUDDY_MSG:
	        	//将需要推送的内容分发给指定的群目录
	        	updateGroupMemory(client, msgReceived);
	        	break;
	        case DISCUZ_MSG:
	        	//讨论组忽略
	        	break;
	        case GROUP_MSG:
	        	//回复推送
	        	TalkingObject talkingObject = talkingBook.get(msgReceived.getGroup().getGin());
	        	if(talkingObject == null){
	        		break;
	        	}
	        	if(talkingObject.msgWaitToSend == null){
	        		break;
	        	}
	        	talkingObject.disaptchMesgToGroup(client, msgReceived);
	        	break;
	        case SESSION_MSG:
	        	//零时对话忽略
	        	break;
	        default:break;
        }
	}
}
