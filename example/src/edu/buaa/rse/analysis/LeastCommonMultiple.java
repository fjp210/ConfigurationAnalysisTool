package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.List;


/**
 * @description 这个最小公倍数是什么的最小公倍数？
*最小公倍数是模块上各自软件的执行周期最小公倍数
*静态方法of将生成输入数组的最小公倍数
*@author Li Yaonan
**/
public class LeastCommonMultiple {
    public static int of(ArrayList<Integer> input){
        int ans = 1;
        for(int i = 0;i < input.size(); i++){
            ans = LeastCommonMultiple.lcm(ans, input.get(i));
        }
        return ans;
    }
    
    public static int gcd(int a, int b){
        int big = Math.max(a, b);
        int small = Math.min(a, b);
        if(0==big % small){
            return small;
        }else{
            return gcd(small, big%small);
        }
    }
    
    public static int lcm(int a, int b){
        return a * b/gcd(a, b);
    }
    
}
