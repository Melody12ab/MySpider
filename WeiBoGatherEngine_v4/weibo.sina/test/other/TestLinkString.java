package test.other;

public class TestLinkString {

	public static void main(String[] args) {
       String[] str={"1","2","3","4","5","6"};
       String sum="";
       for(String tt:str){
    	   sum+=tt+"#";
       }
       
       System.out.println(sum);
       System.out.println(sum.subSequence(0,sum.length()-1));
	}
}
