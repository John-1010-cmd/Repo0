import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 *
 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 * @author John
 */
public class TwoSum {
    int[] nums;
    int target;
    TwoSum(){
        this.nums = new int[]{};
    }
    TwoSum(int[] nums,int target){
        this.nums = new int[nums.length];
        this.nums = nums;
        this.target =target;
    }
    @Override
    public String toString(){
        String result = "";
        for(int i = 0;i<nums.length;i++){
            if(i == 0){
                result += nums[0];
            }else {
                result += "," + nums[i];
            }
        }
        return "nums = ["+result+"], "+"target = "+target;
    }
    public String getFirstMatch(){
        Map<Integer,Integer> ht = new HashMap<Integer,Integer>();
        for(int i = 0;i<nums.length;i++){
            int diff = target - nums[i];
            if(ht.containsKey(diff)){
                //返回数值
//                return "[ "+diff+","+nums[i]+" ]";
                //返回下标
                return "[ "+ht.get(diff)+", "+i+" ]";
            }
            ht.put(nums[i],i);
        }
        return "can't match!";
    }
    public Map<Integer,Integer> getMatch(){
        Map<Integer,Integer> ht = new HashMap<Integer,Integer>();
        Map<Integer,Integer> match = new HashMap<Integer,Integer>();
        for(int i = 0;i<nums.length;i++){
            int diff = target - nums[i];
            if(ht.containsKey(diff)){
                //返回数值
//                match.put(diff,nums[i]);
                //返回下标+1
//                match.put(ht.get(diff)+1,i+1);
                //返回下标
                match.put(ht.get(diff),i);
            }
            ht.put(nums[i],i);
        }
        return match;
    }
    public static void main(String[] args){
        int[] nums = {2, 7, 1, 8, 2};
        int target = 9;
        //for output [1,8],[2,7] but not [1,8],[2,7],[7,2]
//        Arrays.sort(nums);
        TwoSum twoSum = new TwoSum(nums,target);
//        System.out.println(twoSum.getFirstMatch());
        Map<Integer,Integer> result = twoSum.getMatch();
        result.forEach((k,v)->{
            System.out.println("[ "+k+","+v+" ]");
        });
    }
}
