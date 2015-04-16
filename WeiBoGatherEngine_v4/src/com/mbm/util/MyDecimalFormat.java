package com.mbm.util;

import java.text.DecimalFormat;

public class MyDecimalFormat {
	public static DecimalFormat df=new DecimalFormat("########.##");
    public static String formatDouble(double d){
    	return df.format(d);
    }
}
