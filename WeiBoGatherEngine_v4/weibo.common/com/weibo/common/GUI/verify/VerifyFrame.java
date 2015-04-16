package com.weibo.common.GUI.verify;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.weibo.common.GUI.verify.iface.IFrame;
import com.weibo.common.GUI.verify.iface.IVerifyElement;
import com.weibo.common.utils.StaticValue;

/**
 * 验证的主窗体,窗体在这个类中
 * 
 * @author zel
 * 
 */
public class VerifyFrame implements IFrame {
	public JTextField textField = null;
	/**
	 * 代表窗体是否存在
	 */
	private boolean isRunning = false;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String verifyCode = null;
	public JFrame mainFrame = null;
	public JLabel pitLabel = null;

	public VerifyFrame(String flag, IVerifyElement verifyElement) {
		isRunning = true;
		mainFrame = new JFrame();
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.setSize(300, 300);
		mainFrame.setLocation(300, 300);

		// 添加图片
		pitLabel = new JLabel();
		ImageIcon img = null;
		if (flag.equalsIgnoreCase("login")) {
			mainFrame.setTitle("登陆--输入验证码");
			img = new ImageIcon("temp_pic/verify_login"
					+ StaticValue.verify_login_pic_index + ".png");// 加载图片
		} else if (flag.equalsIgnoreCase("keyword")) {
			mainFrame.setTitle("反爬--输入验证码");
			img = new ImageIcon("temp_pic/"
					+ verifyElement.getCurrent_main_thread_name()
					+ verifyElement.getVerify_keyword_pic_index() + ".png");// 加载图片
		}
		pitLabel.setIcon(img);
		mainFrame.add(pitLabel);

		// 添加文本框
		textField = new JTextField();
		textField.setSize(100, 10);
		mainFrame.add(textField);

		JButton button = new JButton("提交");
		button.setActionCommand("click");
		mainFrame.add(button);

		/**
		 * 集中给相关对象添加事件处理
		 */
		button.addActionListener(new VerifyActionListener(this));// 给提交按钮添加事件
		textField.addKeyListener(new KeyBoardActionListener(this));

		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("keyboard test");
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setSize(300, 300);
		mainFrame.setLocation(300, 300);

		JTextField textField = new JTextField();
		mainFrame.add(textField);
		mainFrame.add(textField);

		textField.addKeyListener(new KeyBoardActionListener());

		mainFrame.setVisible(true);
	}
}

class VerifyActionListener implements ActionListener {
	private VerifyFrame verifyFrame;

	public VerifyActionListener(VerifyFrame verifyFrame) {
		this.verifyFrame = verifyFrame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("click")) {
			verifyFrame.setRunning(false);
			verifyFrame.verifyCode = verifyFrame.textField.getText().trim();
			verifyFrame.mainFrame.dispose();
		}
	}
}

class KeyBoardActionListener implements KeyListener {
	private VerifyFrame verifyFrame;

	public KeyBoardActionListener(VerifyFrame verifyFrame) {
		this.verifyFrame = verifyFrame;
	}

	public KeyBoardActionListener() {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 10) {
			verifyFrame.setRunning(false);
			verifyFrame.verifyCode = verifyFrame.textField.getText().trim();
			verifyFrame.mainFrame.dispose();
//			System.out.println("kk");
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}
