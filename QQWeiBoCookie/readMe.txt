本项目主要两个功能：
1、模拟登陆腾讯微博，但是老版的，有学习意义，需要改造才能真正使用。
2、提供了一个类的方法，来测试你得到的cookies是否是真正的正确的cookies，
具体的实验说明如下：
1、运行类com.weibo.utils.qq.login.GetUserCookie4QQ的主方法，可以得到老版的cookies。
2、检验cookie是否是正确的类为com.weibo.qq.cookie.common.GrabPageSource4QQ，
       将得到的cookies或是浏览器中的cookies放到该类的主方法中即可，并看该方法说明。