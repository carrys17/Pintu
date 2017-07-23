package com.example.shang.map.pingtu.utils;

import com.example.shang.map.pingtu.bean.ItemBean;
import com.example.shang.map.pingtu.activity.PuzzleMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shang on 2017/4/21.
 */

public class GameUtil {

    // 游戏信息单元格Bean
    public static List<ItemBean> mItemBeans = new ArrayList<>();
    // 空格单元格
    public static ItemBean mBlankItemBean = new ItemBean();

    // 判断是否可以移动
    public static boolean isMoveable(int position){
        int type = PuzzleMain.TYPE;
        // 获取空格Item
        int blankId = GameUtil.mBlankItemBean.getmItemId() -1;
        if (Math.abs(blankId - position) == type){ //不同行相差为type
            return true;
        }
        if ((blankId/type == position/type)&&Math.abs(blankId - position)==1){ //同行相差为1
            return true;
        }
        return false;
    }

    /**
     * 是否拼图成功
     */
    public static boolean isSuccess(){
        for (ItemBean tempBean : GameUtil.mItemBeans){ //blank的mBitmapId为0，在imagesutils定义
            if (tempBean.getmBitmapId()!=0 && (tempBean.getmItemId()==tempBean.getmBitmapId())){ // 其他的对应id相等
                continue;
            }else if(tempBean.getmBitmapId() == 0&& tempBean.getmItemId()==PuzzleMain.TYPE *PuzzleMain.TYPE){ //空白在最后
                continue;
            }else {
                return false;
            }
        }
        return true;
    }


    /**
     * 生成随机打乱顺序
     */
    public static void getPuzzleGenerator(){
        int index = 0;
        for (int i=0;i< mItemBeans.size();i++){
            index = (int) (Math.random()*PuzzleMain.TYPE * PuzzleMain.TYPE);
            swapItems(mItemBeans.get(index),GameUtil.mBlankItemBean);
        }
        List<Integer> data = new ArrayList<>();
        for (int i=0;i<mItemBeans.size();i++){
            data.add(mItemBeans.get(i).getmBitmapId());
        }
        System.out.println("data:"+data); //这些是打乱后的矩阵id，最后一个就是有解的矩阵
        if (canSolve(data)&&!isSuccess()){
            return;
        }else {
            getPuzzleGenerator();
        }
    }

    /* 交换空格与点击Item的位置
    *
    * from 交换图
    * blank 空白图
    * */
    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItemBean = new ItemBean();
        //交换BitmapId
        tempItemBean.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(tempItemBean.getmBitmapId());
        //交换Bitmap
        tempItemBean.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(tempItemBean.getmBitmap());
        //设置新的blank
        GameUtil.mBlankItemBean = from;
    }


    //如果网格宽度为奇数，则可解决的情况下的反转次数为偶数。
    // 如果网格宽度为偶数，并且空白处于从底部（最后一个，第四个最后一个等）开始的偶数行上，则可解决情况下的反转次数为奇数。
    //  如果网格宽度为偶数，并且空白处于从底部（最后，最后，最后，最后等）计数的奇数行上，则可解决的情况下的反转次数为偶数。
    /*
    * 该数据是否有解
    *
    * data 拼图数组数据
    * */
    public static boolean canSolve(List<Integer> data) {
        //获取空格ID
        int blankId = GameUtil.mBlankItemBean.getmItemId();
        //可行性原则
        if (data.size()%2 ==1){// 网格宽度为奇数
            return getInversions(data)%2==0;
        } else { //网格宽度为偶数
            if (((blankId-1)/PuzzleMain.TYPE) %2 ==1){ //空白处于从底部（最后，最后，最后，最后等）计数的奇数行上
                return getInversions(data)%2==0;
            }
            else { //  空白处于从底部（最后一个，第四个最后一个等）开始的偶数行上
                return getInversions(data)%2==1;
            }
        }
    }

    /**
     *     计算倒置和算法
     * data 拼图数组数据
     *
     */
    public static int getInversions(List<Integer>data){
        int inversions = 0; //总
        int intversionCount = 0; // 每个数
        for (int i=0;i<data.size();i++){
            for (int j =i+1;j<data.size();j++){
                if (data.get(j)!=0 && data.get(j)<data.get(i)){
                    intversionCount++;
                }
            }
            inversions += intversionCount;
            intversionCount = 0;
        }
        return  inversions;
    }
}
