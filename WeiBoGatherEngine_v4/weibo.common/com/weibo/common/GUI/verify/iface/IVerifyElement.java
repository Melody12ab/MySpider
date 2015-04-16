package com.weibo.common.GUI.verify.iface;

public interface IVerifyElement {
	public String getCurrent_main_thread_name();

	public int getVerify_keyword_pic_index();
	
	public void setVerify_keyword_pic_index(int autoAddOne);
}
