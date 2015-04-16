package com.mbm.LJSearch;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.weibo.common.utils.StaticValue;

/**
 * 该管理器主要管理各个搜索请求时socket的连接
 * 
 * @author zel
 */
//@Component
public class LJSearchSocketManager {
//	private static Logger logger = LoggerFactory
//			.getLogger(LJSearchSocketManager.class);
//
//	public static LinkedList<Socket> personSocketLink_notBusy = null;
//	public static LinkedList<Socket> attentionSocketLink_notBusy = null;
//	public static LinkedList<Socket> docSocketLink_notBusy = null;
//
////	public static LinkedList<Socket> personSocketLink_isBusy = new LinkedList<Socket>();// 初始化personSocketLink_isBusy集合;
////	public static LinkedList<Socket> attentionSocketLink_isBusy = new LinkedList<Socket>();// 初始化attentionSocketLink_isBusy集合;
////	public static LinkedList<Socket> docSocketLink_isBusy = new LinkedList<Socket>();// 初始化docSocketLink_isBusy集合;
//
//	static {
//		personSocketLink_notBusy = new LinkedList<Socket>();// 初始化personSocket集合
//		attentionSocketLink_notBusy = new LinkedList<Socket>();// 初始化personSocket集合
//		docSocketLink_notBusy = new LinkedList<Socket>();// 初始化personSocket集合
//		Socket socket = null;
//		try {
//			for (int i = 0; i < StaticValue.person_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_accountInfo_ip,
//						StaticValue.search_accountInfo_port);
//				personSocketLink_notBusy.add(socket);
//			}
//			logger.info("人物搜索socket连接池初始化完毕,大小为  "
//					+ StaticValue.person_search_socket_num + " 个");
//
//			for (int i = 0; i < StaticValue.attention_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_attentionInfo_ip,
//						StaticValue.search_attentionInfo_port);
//				attentionSocketLink_notBusy.add(socket);
//			}
//			logger.info("关注搜索socket连接池初始化完毕,大小为  "
//					+ StaticValue.attention_search_socket_num + " 个");
//
//			for (int i = 0; i < StaticValue.doc_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_doc_ip,
//						StaticValue.search_doc_port);
//				docSocketLink_notBusy.add(socket);
//			}
//			logger.info("博文内容搜索socket连接池初始化完毕,大小为  "
//					+ StaticValue.doc_search_socket_num + " 个");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("初始化sokcet连接池时失败!");
//		}
//	}
//	/**
//	 * 分别用于3个搜索socket获取的同步锁
//	 */
//	private Object syn_person = new Object();
//	private Object syn_attention = new Object();
//	private Object syn_doc = new Object();
//
//	private int temp_person_i = 0;
//	private int temp_attention_i = 0;
//	private int temp_doc_i = 0;
//
//	private Socket temp_person_socket = null;
//	private Socket temp_attention_socket = null;
//	private Socket temp_doc_socket = null;
//
//	public Socket getNotBusyPerson() {
//		synchronized (syn_person) {
//			for (temp_person_i = 0; temp_person_i < StaticValue.request_socket_fail_times; temp_person_i++) {
//				temp_person_socket = personSocketLink_notBusy.pollFirst();
//				if (temp_person_socket == null) {
//					logger.info("正在等待一个person搜索socket!");
//					continue;
//				} else {
//					break;
//				}
//			}
//			return temp_person_socket;
//		}
//	}
//
//	public void putNotBusyPerson(Socket socket) {
//		synchronized (syn_person) {
//			logger.info("释放了一个person搜索socket!");
//			personSocketLink_notBusy.add(socket);
//		}
//	}
//
//	public Socket getNotBusyAttention() {
//		synchronized (syn_attention) {
//			for (temp_attention_i = 0; temp_attention_i < StaticValue.request_socket_fail_times; temp_attention_i++) {
//				temp_attention_socket = attentionSocketLink_notBusy.pollFirst();
//				if (temp_attention_socket == null) {
//					logger.info("正在等待一个attention搜索socket!");
//					continue;
//				} else {
//					break;
//				}
//			}
//			return temp_attention_socket;
//		}
//	}
//
//	public void putNotBusyAttention(Socket socket) {
//		synchronized (syn_attention) {
//			logger.info("释放了一个attention搜索socket!");
//			attentionSocketLink_notBusy.add(socket);
//		}
//	}
//
//	public Socket getNotBusyDoc() {
//		synchronized (syn_doc) {
//			for (temp_doc_i = 0; temp_doc_i < StaticValue.request_socket_fail_times; temp_doc_i++) {
//				temp_doc_socket = docSocketLink_notBusy.pollFirst();
//				if (temp_doc_socket == null) {
//					logger.info("正在等待一个doc搜索socket!");
//					continue;
//				} else {
//					break;
//				}
//			}
//			return temp_doc_socket;
//		}
//	}
//
//	public void putNotBusyDoc(Socket socket) {
//		synchronized (syn_doc) {
//			logger.info("释放了一个doc搜索socket!");
//			docSocketLink_notBusy.add(socket);
//		}
//	}
//
//	/**
//	 * 如遇异常，如Socket关闭等，重新初始化某种流
//	 * 
//	 * @throws IOException
//	 * @throws UnknownHostException
//	 */
//	public void resetPersonSocketLink() throws Exception {
//		synchronized (syn_person) {
//			Socket socket = null;
//			personSocketLink_notBusy.clear();
//			for (int i = 0; i < StaticValue.person_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_accountInfo_ip,
//						StaticValue.search_accountInfo_port);
//				personSocketLink_notBusy.add(socket);
//			}
//			logger.info("reset--人物搜索socket连接池重新初始化,大小为  "
//					+ StaticValue.person_search_socket_num + " 个");
//		}
//	}
//
//	public void resetAttetionSocketLink() throws Exception {
//		synchronized (syn_attention) {
//			Socket socket = null;
//			attentionSocketLink_notBusy.clear();
//			for (int i = 0; i < StaticValue.attention_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_attentionInfo_ip,
//						StaticValue.search_attentionInfo_port);
//				attentionSocketLink_notBusy.add(socket);
//			}
//			logger.info("reset--关注搜索socket连接池重新初始化,大小为  "
//					+ StaticValue.attention_search_socket_num + " 个");
//		}
//	}
//
//	public void resetDocSocketLink() throws Exception {
//		synchronized (syn_doc) {
//			Socket socket = null;
//			docSocketLink_notBusy.clear();
//			for (int i = 0; i < StaticValue.doc_search_socket_num; i++) {
//				socket = new Socket(StaticValue.search_doc_ip,
//						StaticValue.search_doc_port);
//				docSocketLink_notBusy.add(socket);
//			}
//			logger.info("reset--内容搜索socket连接池重新初始化,大小为  "
//					+ StaticValue.doc_search_socket_num + " 个");
//		}
//	}

}
