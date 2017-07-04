package com.tomato.main.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tomato.main.R;
import com.tomato.tools.chart.ChartService;


/**
 * Created by zch22 on 2017/5/15.
 * //这个工具类用于每隔Fragment中动态添加按钮组，并为其设置点击事件
 */
public class ChangeButtonNumsUtil {
    private ChartService chartService;
    private ChartService chartService1;
    private int pageCode;//表示第几个Fragment 1,2,3
    private int channleCount;   //通道数
    private Context context;    //当前activity
    private LinearLayout ChannelButtonLayout;//相应Fragment中的通道按钮布局
    //设置成成员变量是为了在Fragment与Dialog间状态能保留下来
    private  boolean []ifClicked;//设置channleCount个布尔值，每个用来表示每个通道按钮选中与未选中两种状态
    private  boolean [][]ifSelected;//每个flag用来表示每个通道按钮选中与未选中两种状态
    private  boolean ifChooseAllSeleted;//是否选了全选

    //小图片，只有c1-c7和弹出框按钮
    private final int smallNormalDrawable[]={R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,R.drawable.c5,R.drawable.c6,R.drawable.c7,R.drawable.omission};//在Fragment点击后的小图片资源
    private final int smallNoColorDrawable[]={R.drawable.c1_normal,R.drawable.c2_normal,R.drawable.c3_normal,R.drawable.c4_normal,R.drawable.c5_normal,R.drawable.c6_normal,R.drawable.c7_normal};//在Fragment未点击的小图片资源
    //大图片 c1-c15 这里的c1-c7全都要改为大的
    private final int[][] bigNormalDrawable={{R.drawable.b_c1,R.drawable.b_c2,R.drawable.b_c3,R.drawable.b_c4,R.drawable.b_c5},
                                                    {R.drawable.b_c6,R.drawable.b_c7,R.drawable.b_c8,R.drawable.b_c9,R.drawable.b_c10},
                                                    {R.drawable.b_c11,R.drawable.b_c12,R.drawable.b_c13,R.drawable.b_c14,R.drawable.b_c15}};
    private final int[][] bigNoColorDrawable= {  {R.drawable.b_c1_normal,R.drawable.b_c2_normal,R.drawable.b_c3_normal,R.drawable.b_c4_normal,R.drawable.b_c5_normal},
                                                    {R.drawable.b_c6_normal,R.drawable.b_c7_normal,R.drawable.b_c8_normal,R.drawable.b_c9_normal,R.drawable.b_c10_normal},
                                                    {R.drawable.b_c11_normal,R.drawable.b_c12_normal,R.drawable.b_c13_normal,R.drawable.b_c14_normal,R.drawable.b_c15_normal}};
    private final int[][] id={{0,1,2,3,4},{5,6,7,8,9},{10,11,12,13,14}};//id一样的所以可以同时改变


    public ChangeButtonNumsUtil(int channleCount,int index, LinearLayout channelGroupLayout, Context context, ChartService chartService,ChartService chartService1){
        this.chartService=chartService;
        this.chartService1=chartService1;
        this.channleCount= channleCount;
        this.pageCode =index;
        this.ChannelButtonLayout=channelGroupLayout;
        this.context=context;
        ifClicked =new boolean[channleCount];
        for(int i=0;i<channleCount;i++){
            ifClicked[i]=false;
        }
        //标志全都初始化为false
        ifChooseAllSeleted=false;
        ifSelected=new boolean[3][5];
        for(int i=0;i<3;i++)
            for(int j=0;j<5;j++){
                ifSelected[i][j]=false;
            }
    }

    public void setLineToTransparent(int index){
        chartService.setLineTransparent(index);
        //在只有不是第一个页面并且是最后一个按钮时才不会变化   //纵向梯度曲线 少一条
        if(index ==channleCount-1&&this.pageCode!=1) {
        }else{
            chartService1.setLineTransparent(index);
        }
    }


    public void setLineResume(int index){
        chartService.setLineRecover(index);
        //在只有不是第一个页面并且是最后一个按钮时才不会变化    //纵向梯度曲线 少一条
        if(index ==channleCount-1&&this.pageCode!=1) {
        }else{
            chartService1.setLineRecover(index);
        }
    }
    /*
     * 根据通道数来改变显示通道按钮的数目并为每个按钮添加事件监听
     */
    public void initChannelButtons(){
        int channleCountTemp=0;
        if(channleCount>7){
            channleCountTemp=8;
        }else{
            channleCountTemp=channleCount;
        }
        ChannelButtonLayout.removeAllViews();

        for(int i=0;i<channleCountTemp;i++){
            final ImageButton imageButton= new ImageButton(context) ;
            imageButton.setId(i);
            imageButton.setImageResource(smallNormalDrawable[i]);
            imageButton.setBackgroundColor(context.getResources().getColor(R.color.channelBtnBack));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            imageButton.setLayoutParams(lp); //设置每个按钮权重为1
            ChannelButtonLayout.addView(imageButton);
            //为每个按钮设置点击事件，主要是改变背景图片，将对应通道的那条线变为背景色与还原
            imageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch(v.getId()){
                        case 0:
                            ImageButton imageButton0=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[0]){
                                //Toast.makeText(context,"第1个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[0]=false;
                                ifSelected[0][0]=false;
                                imageButton0.setImageResource(smallNormalDrawable[0]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第1个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[0]=true;
                                ifSelected[0][0]=true;
                                imageButton0.setImageResource(smallNoColorDrawable[0]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 1:
                            ImageButton imageButton1=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[1]){
                                //Toast.makeText(context,"第2个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[1]=false;
                                ifSelected[0][1]=false;
                                imageButton1.setImageResource(smallNormalDrawable[1]);
                                setLineResume(v.getId());
                            }else{
                                    //Toast.makeText(context,"第2个被取消",Toast.LENGTH_LONG).show();
                                    ifClicked[1]=true;
                                    ifSelected[0][1]=true;
                                    imageButton1.setImageResource(smallNoColorDrawable[1]);
                                    setLineToTransparent(v.getId());
                            }
                            break;
                        case 2:
                            ImageButton imageButton2=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[2]){
                                //Toast.makeText(context,"第3个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[2]=false;
                                ifSelected[0][2]=false;
                                imageButton2.setImageResource(smallNormalDrawable[2]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第3个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[2]=true;
                                ifSelected[0][2]=true;
                                imageButton2.setImageResource(smallNoColorDrawable[2]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 3:
                            ImageButton imageButton3=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[3]){
                                //Toast.makeText(context,"第4个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[3]=false;
                                ifSelected[0][3]=false;
                                imageButton3.setImageResource(smallNormalDrawable[3]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第4个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[3]=true;
                                ifSelected[0][3]=true;
                                imageButton3.setImageResource(smallNoColorDrawable[3]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 4:
                            ImageButton imageButton4=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[4]){
                                //Toast.makeText(context,"第5个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[4]=false;
                                ifSelected[0][4]=false;
                                imageButton4.setImageResource(smallNormalDrawable[4]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第5个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[4]=true;
                                ifSelected[0][4]=true;
                                imageButton4.setImageResource(smallNoColorDrawable[4]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 5:
                            ImageButton imageButton5=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[5]){
                                //Toast.makeText(context,"第6个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[5]=false;
                                ifSelected[1][0]=false;
                                imageButton5.setImageResource(smallNormalDrawable[5]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第6个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[5]=true;
                                ifSelected[1][0]=true;
                                imageButton5.setImageResource(smallNoColorDrawable[5]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 6:
                            ImageButton imageButton6=(ImageButton) ChannelButtonLayout.findViewById(v.getId());
                            if(ifClicked[6]){
                                //Toast.makeText(context,"第7个被选中",Toast.LENGTH_LONG).show();
                                ifClicked[6]=false;
                                ifSelected[1][1]=false;
                                imageButton6.setImageResource(smallNormalDrawable[6]);
                                setLineResume(v.getId());
                            }else{
                                //Toast.makeText(context,"第7个被取消",Toast.LENGTH_LONG).show();
                                ifClicked[6]=true;
                                ifSelected[1][1]=true;
                                imageButton6.setImageResource(smallNoColorDrawable[6]);
                                setLineToTransparent(v.getId());
                            }
                            break;
                        case 7:showDialog();break; //点击最后一个按钮出现弹出框
                    }
                }
            });
        }
    }


   //显示Dialog弹出框
    private void showDialog() {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_view, null);
        final Dialog dialog = new Dialog(context,R.style.common_dialog);  // 设置style 控制默认dialog带来的边距问题

        ImageButton btnSure=(ImageButton) view.findViewById(R.id.btn_sure);
        final ImageButton btnChooseAll =(ImageButton)view.findViewById(R.id.btn_chooseall);
        LinearLayout chooseall_layout=(LinearLayout)view.findViewById(R.id.chooseall_layout);
        //由于最多15个按钮这里只用了三个布局
        final LinearLayout[] linearLayouts={(LinearLayout) view.findViewById(R.id.channelButtonGroupLayout1),(LinearLayout) view.findViewById(R.id.channelButtonGroupLayout2),(LinearLayout) view.findViewById(R.id.channelButtonGroupLayout3)};

        int layoutNum=0;//需要用到的布局数
        int[] buttons=new int[3];//每个布局里的按钮数目
        if(channleCount<=5) {
           layoutNum=1;
            buttons[0]=channleCount;
        }else if(channleCount<=10) {
            layoutNum=2;
            buttons[0]=5;
            buttons[1]=channleCount-5;
        }else{
            layoutNum=3;
            buttons[0]=5;
            buttons[1]=5;
            buttons[2]=channleCount-10;
        }
        //向Dialog中添加按钮并添加点击事件
        for(int i=0;i<layoutNum;i++){
            for(int j=0;j<buttons[i];j++) {
                final ImageButton imageButton = new ImageButton(context);
                imageButton.setId(id[i][j]);//为按钮设置id
                if(ifSelected[i][j]){//为true说明在Fragment中是点击状态（默认是有颜色的）
                    imageButton.setImageResource(bigNoColorDrawable[i][j]);//背景设为白色
                    btnChooseAll.setImageResource(R.drawable.select_box);//初始有一个没选，全选按钮就为空白背景
                    ifChooseAllSeleted=true;
                }else{
                    imageButton.setImageResource(bigNormalDrawable[i][j]);//背景设为彩色
                }
                imageButton.setBackgroundColor(context.getResources().getColor(R.color.channelBtnBackInDialog));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                imageButton.setLayoutParams(lp); //设置每个按钮权重为1
                linearLayouts[i].addView(imageButton);
                //为每个按钮设置点击事件，主要是改变背景图片和点击状态，将对应通道的那条线变为背景色与还原
                imageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ImageButton imageButton;
                        switch (v.getId()) {
                            case 0:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());//获得Fragment中的按钮
                                ImageButton imageButton00 =(ImageButton)linearLayouts[0].findViewById(v.getId());//获取Dialog中的按钮
                                if (ifSelected[0][0]) {
                                    //Toast.makeText(context, "第1个被选中---", Toast.LENGTH_LONG).show();
                                    ifSelected[0][0] = false;
                                    ifClicked[0]=false;
                                    imageButton.setImageResource(smallNormalDrawable[0]);
                                    imageButton00.setImageResource(bigNormalDrawable[0][0]);
                                    setLineResume(v.getId());

                                } else {
                                    //Toast.makeText(context, "第1个被取消---", Toast.LENGTH_LONG).show();
                                    ifSelected[0][0]= true;
                                    ifClicked[0]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[0]);
                                    imageButton00.setImageResource(bigNoColorDrawable[0][0]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 1:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton01 =(ImageButton)linearLayouts[0].findViewById(v.getId());
                                if (ifSelected[0][1]) {
                                    //Toast.makeText(context, "第2个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[0][1] = false;
                                    ifClicked[1]=false;
                                    imageButton.setImageResource(smallNormalDrawable[1]);
                                    imageButton01.setImageResource(bigNormalDrawable[0][1]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第2个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[0][1]= true;
                                    ifClicked[1]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[1]);
                                    imageButton01.setImageResource(bigNoColorDrawable[0][1]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 2:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton02 = (ImageButton)linearLayouts[0].findViewById(v.getId());
                                if (ifSelected[0][2]) {
                                    //Toast.makeText(context, "第3个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[0][2] = false;
                                    ifClicked[2]=false;
                                    imageButton.setImageResource(smallNormalDrawable[2]);
                                    imageButton02.setImageResource(bigNormalDrawable[0][2]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第3个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[0][2]= true;
                                    ifClicked[2]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[2]);
                                    imageButton02.setImageResource(bigNoColorDrawable[0][2]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 3:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton03 = (ImageButton)linearLayouts[0].findViewById(v.getId());
                                if (ifSelected[0][3]) {
                                    //Toast.makeText(context, "第4个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[0][3] = false;
                                    ifClicked[3]=false;
                                    imageButton.setImageResource(smallNormalDrawable[3]);
                                    imageButton03.setImageResource(bigNormalDrawable[0][3]);
                                    setLineResume(v.getId());

                                } else {
                                    //Toast.makeText(context, "第4个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[0][3]= true;
                                    ifClicked[3]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[3]);
                                    imageButton03.setImageResource(bigNoColorDrawable[0][3]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 4:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton04 = (ImageButton) linearLayouts[0].findViewById(v.getId());
                                if (ifSelected[0][4]) {
                                    //Toast.makeText(context, "第5个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[0][4] = false;
                                    ifClicked[4]=false;
                                    imageButton.setImageResource(smallNormalDrawable[4]);
                                    imageButton04.setImageResource(bigNormalDrawable[0][4]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第5个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[0][4]= true;
                                    ifClicked[4]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[4]);
                                    imageButton04.setImageResource(bigNoColorDrawable[0][4]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 5:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton10 = (ImageButton) linearLayouts[1].findViewById(v.getId());
                                if (ifSelected[1][0]) {
                                    //Toast.makeText(context, "第6个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[1][0] = false;
                                    ifClicked[5]=false;
                                    imageButton.setImageResource(smallNormalDrawable[5]);
                                    imageButton10.setImageResource(bigNormalDrawable[1][0]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第6个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[1][0]= true;
                                    ifClicked[5]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[5]);
                                    imageButton10.setImageResource(bigNoColorDrawable[1][0]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 6:
                                imageButton = (ImageButton) ChannelButtonLayout.findViewById(v.getId());
                                ImageButton imageButton11 = (ImageButton) linearLayouts[1].findViewById(v.getId());
                                if (ifSelected[1][1]) {
                                    //Toast.makeText(context, "第7个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[1][1] = false;
                                    ifClicked[6]=false;
                                    imageButton.setImageResource(smallNormalDrawable[6]);
                                    imageButton11.setImageResource(bigNormalDrawable[1][1]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第7个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[1][1]= true;
                                    ifClicked[6]=true;
                                    imageButton.setImageResource(smallNoColorDrawable[6]);
                                    imageButton11.setImageResource(bigNoColorDrawable[1][1]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 7:
                                ImageButton imageButton12 = (ImageButton)linearLayouts[1].findViewById(v.getId());
                                if (ifSelected[1][2]) {
                                    //Toast.makeText(context, "第8个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[1][2] = false;
                                    imageButton12.setImageResource(bigNormalDrawable[1][2]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第8个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[1][2]= true;
                                    imageButton12.setImageResource(bigNoColorDrawable[1][2]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 8:
                                ImageButton imageButton13 = (ImageButton)linearLayouts[1].findViewById(v.getId());
                                if (ifSelected[1][3]) {
                                    //Toast.makeText(context, "第9个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[1][3] = false;
                                    imageButton13.setImageResource(bigNormalDrawable[1][3]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第9个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[1][3]= true;
                                    imageButton13.setImageResource(bigNoColorDrawable[1][3]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 9:
                                ImageButton imageButton14 = (ImageButton)linearLayouts[1].findViewById(v.getId());
                                if (ifSelected[1][4]) {
                                    //Toast.makeText(context, "第10个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[1][4] = false;
                                    imageButton14.setImageResource(bigNormalDrawable[1][4]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第10个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[1][4]= true;
                                    imageButton14.setImageResource(bigNoColorDrawable[1][4]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 10:
                                ImageButton imageButton20 = (ImageButton) linearLayouts[2].findViewById(v.getId());
                                if (ifSelected[2][0]) {
                                    //Toast.makeText(context, "第11个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[2][0] = false;
                                    imageButton20.setImageResource(bigNormalDrawable[2][0]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第11个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[2][0]= true;
                                    imageButton20.setImageResource(bigNoColorDrawable[2][0]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 11:
                                ImageButton imageButton21 = (ImageButton) linearLayouts[2].findViewById(v.getId());
                                if (ifSelected[2][1]) {
                                    //Toast.makeText(context, "第12个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[2][1] = false;
                                    imageButton21.setImageResource(bigNormalDrawable[2][1]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第12个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[2][1]= true;
                                    imageButton21.setImageResource(bigNoColorDrawable[2][1]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 12:
                                ImageButton imageButton22 = (ImageButton)linearLayouts[2].findViewById(v.getId());
                                if (ifSelected[2][2]) {
                                    //Toast.makeText(context, "第13个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[2][2] = false;
                                    imageButton22.setImageResource(bigNormalDrawable[2][2]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第13个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[2][2]= true;
                                    imageButton22.setImageResource(bigNoColorDrawable[2][2]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 13:
                                ImageButton imageButton23 = (ImageButton)linearLayouts[2].findViewById(v.getId());
                                if (ifSelected[2][3]) {
                                   // Toast.makeText(context, "第14个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[2][3] = false;
                                    imageButton23.setImageResource(bigNormalDrawable[2][3]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第14个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[2][3]= true;
                                    imageButton23.setImageResource(bigNoColorDrawable[2][3]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                            case 14:
                                ImageButton imageButton24 = (ImageButton)linearLayouts[2].findViewById(v.getId());
                                if (ifSelected[2][4]) {
                                    //Toast.makeText(context, "第15个被选中", Toast.LENGTH_LONG).show();
                                    ifSelected[2][4] = false;
                                    imageButton24.setImageResource(bigNormalDrawable[2][4]);
                                    setLineResume(v.getId());
                                } else {
                                    //Toast.makeText(context, "第15个被取消", Toast.LENGTH_LONG).show();
                                    ifSelected[2][4]= true;
                                    imageButton24.setImageResource(bigNoColorDrawable[2][4]);
                                    btnChooseAll.setImageResource(R.drawable.select_box);
                                    ifChooseAllSeleted=true;
                                    setLineToTransparent(v.getId());
                                }
                                break;
                        }
                    }
                });
            }
        }
        //如果当前布局中按钮不足5个，添加新按钮（不可点击）将其补充到5个按钮方便布局对齐
        for(int c=0;c<3;c++){
            if(buttons[c]<5){
                int a=5-buttons[c];
                for(int j=0;j<a;j++) {
                    ImageButton imageButton = new ImageButton(context);
                    imageButton.setBackgroundColor(context.getResources().getColor(R.color.channelBtnBackInDialog));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    imageButton.setLayoutParams(lp); //设置每个按钮权重为1
                    imageButton.setClickable(false);
                    linearLayouts[c].addView(imageButton);
                }
                break;
            }
        }
        //为确定和全选按钮定义监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_sure:
                        dialog.dismiss();
                        break;
                    case R.id.chooseall_layout:
                        if(ifChooseAllSeleted){
                            //Toast.makeText(context, "全选", Toast.LENGTH_LONG).show();
                            //点击全选，将所有按钮背景变为彩色,flag置为false
                            btnChooseAll.setImageResource(R.drawable.select_box_selected);
                            for(int i=0;i<channleCount;i++){
                                ImageButton imageButton=(ImageButton) view.findViewById(i);
                                if(i<=4) {
                                    imageButton.setImageResource(bigNormalDrawable[i / 5][i]);
                                    imageButton = (ImageButton) ChannelButtonLayout.findViewById(i);
                                    imageButton.setImageResource(smallNormalDrawable[i]);
                                    ifClicked[i]=false;
                                    ifSelected[i/5][i]=false;
                                }else if(i<=9){
                                    imageButton.setImageResource(bigNormalDrawable[i / 5][i-5]);
                                    if(i<7){
                                        imageButton = (ImageButton) ChannelButtonLayout.findViewById(i);
                                        imageButton.setImageResource(smallNormalDrawable[i]);
                                    }
                                    ifClicked[i] = false;
                                    ifSelected[i/5][i-5]=false;
                                }else{
                                    imageButton.setImageResource(bigNormalDrawable[i / 5][i-10]);
                                    ifClicked[i] = false;
                                    ifSelected[i/5][i-10]=false;
                                }
                            }
                            ifChooseAllSeleted=false;
                        }else{
                            //Toast.makeText(context, "取消全选", Toast.LENGTH_LONG).show();
                            //取消全选，将所有按钮背景变为白色，记得将所有的flag置为true,这里只有效果，点了确定之后才会生效
                            btnChooseAll.setImageResource(R.drawable.select_box);
                            for(int i=0;i<channleCount;i++){
                                ImageButton imageButton=(ImageButton) view.findViewById(i);
                                if(i<=4) {
                                    imageButton.setImageResource(bigNoColorDrawable[i / 5][i]);
                                    imageButton = (ImageButton) ChannelButtonLayout.findViewById(i);
                                    imageButton.setImageResource(smallNoColorDrawable[i]);
                                    ifClicked[i]=true;
                                    ifSelected[i/5][i]=true;
                                }else if(i<=9){
                                    imageButton.setImageResource(bigNoColorDrawable[i / 5][i-5]);
                                    if(i<7){
                                        imageButton = (ImageButton) ChannelButtonLayout.findViewById(i);
                                        imageButton.setImageResource(smallNoColorDrawable[i]);
                                    }
                                    ifClicked[i] = true;
                                    ifSelected[i/5][i-5]=true;
                                }else{
                                    imageButton.setImageResource(bigNoColorDrawable[i / 5][i-10]);
                                    ifClicked[i] = true;
                                    ifSelected[i/5][i-10]=true;
                                }
                            }
                            ifChooseAllSeleted=true;
                        }
                        break;
                }
            }
        };
        btnSure.setOnClickListener(listener);//为确定按钮添加监听器
        chooseall_layout.setOnClickListener(listener);//为全选按钮添加监听器

        dialog.setContentView(view);
        dialog.show();
        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;        //确定弹出框的位置
        window.setAttributes(params);

    }
}
