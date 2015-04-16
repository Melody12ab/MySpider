package com.weibo.common.utils;

public class LJSearch_StaticValue {
	public static String attentionQueryFomate = "<index><no>1</no><main>1</main><join>uid_long</join><cmd>$1</cmd></index><index><no>2</no><main>0</main><join>uid_long</join><cmd>$2</cmd></index>";
	public static String contentQueryFormate_no_join_begin = "<index><no>1</no><main>1</main><cmd>$1</cmd></index>";
	public static String contentQueryFormate_join_begin = "<index><no>1</no><main>1</main><join>person_id_long</join><cmd>$1</cmd></index>";
	public static String contentQueryFormate_join_end = "<index><no>2</no><main>0</main><join>url_id_long</join><cmd>$1</cmd></index>";
	public static String groupAndUserDeleteQueryFormate = "<index><no>2</no><main>0</main><cmd>${1}</cmd></index>";
}
