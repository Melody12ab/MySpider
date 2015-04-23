package test;

/*给定一个单链表，只给出头指针h：
 1、如何判断是否存在环？
 2、如何知道环的长度？
 3、如何找出环的连接点在哪里？
 4、带环链表的长度是多少？

 解法：
 1、对于问题1，使用追赶的方法，设定两个指针slow、fast，从头指针开始，每次分别前进1步、2步。如存在环，则两者相遇；如不存在环，fast遇到NULL退出。
 2、对于问题2，记录下问题1的碰撞点p，slow、fast从该点开始，再次碰撞所走过的操作数就是环的长度s。
 3、问题3：有定理：碰撞点p到连接点的距离=头指针到连接点的距离，因此，分别从碰撞点、头指针开始走，相遇的那个点就是连接点。(证明在后面附注)
 4、问题3中已经求出连接点距离头指针的长度，加上问题2中求出的环的长度，二者之和就是带环单链表的长度*/

public class hasLoop {
	class Node {
		int data;
		Node next;
	}

	// 判断是否有环
	boolean isLoop(Node head) {
		Node fast = head;
		Node slow = head;
		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;
			if (fast == slow) {
				break;
			}
		}
		if (fast == null || fast.next == null) {
			return false;
		} else {
			return true;
		}
	}
}
