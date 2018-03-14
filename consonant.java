import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
      Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        String[] ds=s.split(": ");
        if(Pattern.matches("[a-zA-Z]",ds[1])){
            if(Pattern.matches("[aeiou]",ds[1]))
            System.out.println("Input letter is Vowel");
            else
            System.out.println("Input letter is Consonant"); }   
        else
            System.out.println("Error");
        
    }
}
