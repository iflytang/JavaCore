import java.util.Scanner;
/** 
* @author xiaohao 	
* @date 创建时间：Aug 11, 2017 4:37:52 PM 
* @version 1.0   
*/
public class Test {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner (System.in);
		int n=sc.nextInt();
		int a[]=new int [n];
		for(int i=0;i<n;i++)
		{
			a[i]=sc.nextInt()/1024;
		}
		System.out.println(getTwoSubArrayMinDiff(a)*1024);
	}
 
	public static int getTwoSubArrayMinDiff(int[] arr) {
		// TODO Auto-generated method stub
		int sum=0;
		for(int i=0;i<arr.length;i++)
		{
			sum+=arr[i];
		}
		
		int temp[][]=new int[arr.length+1][sum/2+1];//注意开辟数组的行数列数要多1,是从第1行第1列开始保存数据
		for(int i=0;i<arr.length;i++)
			for(int capacity=1;capacity<=sum/2;capacity++)
			{
				temp[i+1][capacity]=temp[i][capacity];
				if(arr[i]<=capacity && temp[i][capacity-arr[i]] +arr[i]>temp[i][capacity]){
					temp[i+1][capacity]=temp[i][capacity-arr[i]]+arr[i];//可以放，且值比之前要大，则更新
				}
			}
		return Math.max(temp[arr.length][sum/2], sum-temp[arr.length][sum/2]);
	}
 
}
