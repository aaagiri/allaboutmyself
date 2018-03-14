import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
      Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k;
        String s=sc.next();
        if(n>100&&n<=999){
        String rs="";
        rs=s.charAt((s.length())-1)+"";
        rs=(n%10)+rs;
        switch((n/10)%10){
            case 1:{
                rs='!'+rs;
                break;}
                case 2:{
                rs='@'+rs;
                break;}
                case 3:{
                rs='#'+rs;
                break;}
                case 4:{
                rs='$'+rs;
                break;}
                case 5:{
                rs='^'+rs;
                break;}
                case 7:{
                rs='&'+rs;
                break;}
                case 8:{
                rs='*'+rs;
                break;}
                case 9:{
                rs='('+rs;
                break;}
                case 0:{
                rs=')'+rs;
                break;}
            default:{
                break;
            }
        }
        k=(n/100)+(n/10)%10+(n%10);
        if(k>9)
        k=(k/10)+(k%10);
        if(k%2==0)
        rs=k+rs;
            else
                rs=(k+1)+rs;
        System.out.println(rs);}
        else{
             System.out.println("0000");
        }
    }
}
