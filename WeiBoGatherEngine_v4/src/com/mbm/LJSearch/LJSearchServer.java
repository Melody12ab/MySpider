package com.mbm.LJSearch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mbm.LJSearch.pojo.LJSearchResult;
import com.weibo.common.utils.StaticValue;

@Component
public class LJSearchServer {
	private static Logger logger = LoggerFactory
			.getLogger(LJSearchServer.class);
	/**
	 * 在这里将LJSearchSocketManager注入过来
	 */
	// @Autowired
	// private LJSearchSocketManager socketManager;

	private String ip;
	private int port;

	public void init(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public LJSearchServer(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public LJSearchServer() {

	}

	public void deleteQuery(String query) {
		if (query == null || query.trim().length() == 0) {
			return;
		}

		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			byte[] bytes = query.getBytes("GBK");
			socket = new Socket(ip, port);
			// if (search_type == 1) {
			// socket = socketManager.getNotBusyPerson();
			// } else if (search_type == 2) {
			// socket = socketManager.getNotBusyAttention();
			// } else if (search_type == 3) {
			// socket = socketManager.getNotBusyDoc();
			// }
			dos = new DataOutputStream(socket.getOutputStream());
			/**
			 * 简单的将命令发给服务器端， 不做任何返回响应
			 */
			dos.writeInt(bytes.length + 4);
			dos.writeInt(bytes.length);
			dos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 此遇到异常，往往是由于搜索端的重启导致的，将在此做判断
			 */
			try {
				dos.close();
				socket.close();
			} catch (Exception e1) {
				logger.info("socket搜索遇到异常后，尝试关闭时也遇到了异常了，即将放弃关闭!");
			}
			// finally {
			// try {
			// if (search_type == 1) {
			// socketManager.resetPersonSocketLink();
			// } else if (search_type == 2) {
			// socketManager.resetAttetionSocketLink();
			// } else if (search_type == 3) {
			// socketManager.resetDocSocketLink();
			// }
			// deleteQuery(search_type, query);
			// } catch (Exception e2) {
			// e2.printStackTrace();
			// logger.info("重新初始化某种sokcet连接池时出现异常!");
			// }
			// }
		}
		// if (search_type == 1) {
		// socketManager.putNotBusyPerson(socket);
		// } else if (search_type == 2) {
		// socketManager.putNotBusyAttention(socket);
		// } else if (search_type == 3) {
		// socketManager.putNotBusyDoc(socket);
		// }
	}

	/**
	 * search_type:是指1为person,2为attention,3为doc
	 * 
	 * @param search_type
	 * @param query
	 * @param start
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public LJSearchResult search(String query, int start, int size)
			throws Exception {
		if (query == null || query.trim().length() == 0) {
			return null;
		}

		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			byte[] bytes = query.getBytes("GBK");
			socket = new Socket(ip, port);
			// if (search_type == 1) {
			// socket = socketManager.getNotBusyPerson();
			// } else if (search_type == 2) {
			// socket = socketManager.getNotBusyAttention();
			// } else if (search_type == 3) {
			// socket = socketManager.getNotBusyDoc();
			// }
			dos = new DataOutputStream(socket.getOutputStream());

			dis = new DataInputStream(socket.getInputStream());

			dos.writeInt(bytes.length + 12);

			dos.writeInt(start);

			dos.writeInt(size);

			dos.writeInt(bytes.length);

			dos.write(bytes);

			LJSearchResult ljSearchResult = new LJSearchResult(dis);

			// 读取socket流内容
			ljSearchResult.readSocket();

			// if (search_type == 1) {
			// socketManager.putNotBusyPerson(socket);
			// } else if (search_type == 2) {
			// socketManager.putNotBusyAttention(socket);
			// } else if (search_type == 3) {
			// socketManager.putNotBusyDoc(socket);
			// }

			return ljSearchResult;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			/**
			 * 此遇到异常，往往是由于搜索端的重启导致的，将在此做判断
			 */
			try {
				dos.close();
				socket.close();
			} catch (Exception e1) {
				logger.info("socket搜索遇到异常后，尝试关闭时也遇到了异常了，即将放弃关闭!");
			}
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		// LJSearchServer server = new LJSearchServer();
		// server.init("127.0.0.1", 8001);
		// server
		// .search(
		// "[field] url [or] http://www.weibo.com/1598304682 http://www.weibo.com/1008823111/ http://www.weibo.com/1496887652/",
		// 0, 10);
	}

}
