import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ����һ���������� nums ��һ��Ŀ��ֵ target�������ڸ��������ҳ���ΪĿ��ֵ�����������������������ǵ������±ꡣ
 * ����Լ���ÿ������ֻ���Ӧһ���𰸡����ǣ��㲻���ظ��������������ͬ����Ԫ�ء�
 *
 * ʾ��:
 * ���� nums = [2, 7, 11, 15], target = 9
 * ��Ϊ nums[0] + nums[1] = 2 + 7 = 9
 * ���Է��� [0, 1]
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
                //������ֵ
//                return "[ "+diff+","+nums[i]+" ]";
                //�����±�
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
                //������ֵ
//                match.put(diff,nums[i]);
                //�����±�+1
//                match.put(ht.get(diff)+1,i+1);
                //�����±�
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
