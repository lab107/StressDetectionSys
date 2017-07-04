package com.tomato.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.tools.Parameter.SystemParameter;
import com.tomato.tools.Parameter.ThreadParameter;
import com.tomato.tools.dataBase.Bean.ChannelDetectionRecordBean;
import com.tomato.tools.dataBase.Dao.ChannelDetectionRecordDao;
import com.tomato.usbutil.SerialPortOpe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *  标准磁场校准页面
 */
public class StandardMagneticCalibrationActivity extends Activity {
    private TextView selectChannel;        //选择通道
    private EditText standardEditText1;    //标准值1
    private EditText standardEditText2;    //标准值2
    private ImageButton btnNext;           //下一步按钮
    private ImageButton btnQuit1;        //页面1的退出
    private ImageButton btnQuit2;        //页面2的退出
    private ImageButton btnFront;        //页面2的上一步
    private ImageButton btnFinish;       //页面2的完成
    private ImageView imageView;         //小三角
    private ImageView back;
    private LinearLayout layout1;        //文本布局框
    private LinearLayout layout2;
    private LinearLayout layout3;        //底部按钮
    private LinearLayout layout4;

    private Double[] standardValue;    //两个标准值
    private List<Integer> chaneels;    //通道选择
    private List<Double> kValues;     //每个通道对应的k值 与 0值
    private List<Double> zeroValues;
    private List<Double> xList1;
    private List<Double> xList2;

    private Dialog dialog;
    //所选通道的标记
    static private boolean allChaneel=false;
    static private boolean[][] chaneelSelect={
            {false,false,false,false,false},
            {false,false,false,false,false},
            {false,false,false,false,false},
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.standard_magnetic_calibration);
        initView();
        initEvent();
    }

    public void initView(){
        selectChannel=(TextView) findViewById(R.id.selectChaneel);
        standardEditText1=(EditText)findViewById(R.id.standardValue1);
        standardEditText2=(EditText)findViewById(R.id.standardValue2);
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnQuit1=(ImageButton)findViewById(R.id.btnQuit);
        btnQuit2=(ImageButton)findViewById(R.id.btnQuit_1);       //页面2的退出
        btnFront=(ImageButton)findViewById(R.id.btnLast);       //页面2 的上一步
        btnFinish=(ImageButton)findViewById(R.id.btnFinish);      //页面2的完成
        imageView=(ImageView)findViewById(R.id.img_select);
        back=(ImageView)findViewById(R.id.return_key);
        layout1=(LinearLayout)findViewById(R.id.linearlayout1);      //文本布局框
        layout2=(LinearLayout)findViewById(R.id.linearlayout2);
        layout3=(LinearLayout)findViewById(R.id.linearlayout3);     //底部按钮
        layout4=(LinearLayout)findViewById(R.id.linearlayout4);
    }

    public void initEvent(){
        //完成事件
        btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (standardEditText2.getText().length() == 0) {
                    Toast.makeText(StandardMagneticCalibrationActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
                }else {
                    xList2 = getDetectionData();    //从设备获取所有通道在当前磁场下的电压值
                    if (xList2!=null){
                        for (int i = 0; i < xList1.size(); i++) {
                            System.out.println("xList2_" + i + xList2.get(i));
                        }
                    }
                    if (xList2==null){
                        Toast.makeText(StandardMagneticCalibrationActivity.this, "未获取到有效数据！", Toast.LENGTH_SHORT).show();
                    }
                }
                standardValue=new Double[2];
                try {
                    standardValue[0]=Double.parseDouble(standardEditText1.getText().toString());
                    standardValue[1]=Double.parseDouble(standardEditText2.getText().toString());
                    chaneels=new ArrayList<Integer>();
                    for (int i = 0; i< SystemParameter.getInstance().nChannelNumber; i++){
                        if (chaneelSelect[i/5][i%5]){
                            chaneels.add(i);    //如果选择了通道则添加，  通道数的下标 0~14；
                        }
                    }
                    if(xList1!=null&&xList2!=null){
                        startCalibration();    //必须先初始化 xList和yList
                        Toast.makeText(StandardMagneticCalibrationActivity.this, "校准成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(StandardMagneticCalibrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(StandardMagneticCalibrationActivity.this, "请输入正确信息！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //退出事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnQuit1.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnQuit1.setImageDrawable(getDrawable(R.drawable.btn_quit_selected));
                finish();
            }
        });

        btnQuit2.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                btnQuit2.setImageDrawable(getDrawable(R.drawable.btn_quit_selected));
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //设置按下的效果
                if (selectChannel.getText().length() == 0
                        || standardEditText1.getText().length() == 0) {
                    Toast.makeText(StandardMagneticCalibrationActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
                }else{
                    xList1 = getDetectionData();//从设备获取所有通道在当前磁场下的电压值
                    if(xList1!=null){
                        for(int i=0;i<xList1.size();i++) {
                            System.out.println("xList1_"+i+xList1.get(i));
                        }
                    }
                    if (xList1==null){
                        Toast.makeText(StandardMagneticCalibrationActivity.this, "未获取到有效数据！", Toast.LENGTH_SHORT).show();
                    }
                    //更换布局
                    layout1.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout4.setVisibility(View.VISIBLE);
                }
            }
        });

        btnFront.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //更换布局
                layout1.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
            }
        });

        selectChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.callOnClick();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(getDrawable(R.drawable.strigon));
                showDialog();
            }
        });

        standardEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardEditText1.setHint("");
            }
        });

        standardEditText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardEditText2.setHint("");
            }
        });

    }

    public void showDialog(){
        dialog = new Dialog(StandardMagneticCalibrationActivity.this, R.style.common_dialog);
        //对话框布局
        View inflate = LayoutInflater.from(StandardMagneticCalibrationActivity.this).inflate(R.layout.select_channel_dialog, null);
        //初始化控件
        final ImageButton allChaneelButton= (ImageButton) inflate.findViewById(R.id.all_chaneel);
        final ImageButton chaneel_1= (ImageButton) inflate.findViewById(R.id.chaneel_1);
        final ImageButton chaneel_2= (ImageButton) inflate.findViewById(R.id.chaneel_2);
        final ImageButton chaneel_3= (ImageButton) inflate.findViewById(R.id.chaneel_3);
        final ImageButton chaneel_4= (ImageButton) inflate.findViewById(R.id.chaneel_4);
        final ImageButton chaneel_5= (ImageButton) inflate.findViewById(R.id.chaneel_5);
        final ImageButton chaneel_6= (ImageButton) inflate.findViewById(R.id.chaneel_6);
        final ImageButton chaneel_7= (ImageButton) inflate.findViewById(R.id.chaneel_7);
        final ImageButton chaneel_8= (ImageButton) inflate.findViewById(R.id.chaneel_8);
        final ImageButton chaneel_9= (ImageButton) inflate.findViewById(R.id.chaneel_9);
        final ImageButton chaneel_10= (ImageButton) inflate.findViewById(R.id.chaneel_10);
        final ImageButton chaneel_11= (ImageButton) inflate.findViewById(R.id.chaneel_11);
        final ImageButton chaneel_12= (ImageButton) inflate.findViewById(R.id.chaneel_12);
        final ImageButton chaneel_13= (ImageButton) inflate.findViewById(R.id.chaneel_13);
        final ImageButton chaneel_14= (ImageButton) inflate.findViewById(R.id.chaneel_14);
        final ImageButton chaneel_15= (ImageButton) inflate.findViewById(R.id.chaneel_15);
        //根据通道数初始化显示的按钮数
        switch (SystemParameter.getInstance().nChannelNumber){
            case 15:
                chaneel_15.setVisibility(View.VISIBLE);
            case 14:
                chaneel_14.setVisibility(View.VISIBLE);
            case 13:
                chaneel_13.setVisibility(View.VISIBLE);
            case 12:
                chaneel_12.setVisibility(View.VISIBLE);
            case 11:
                chaneel_11.setVisibility(View.VISIBLE);
            case 10:
                chaneel_10.setVisibility(View.VISIBLE);
            case 9:
                chaneel_9.setVisibility(View.VISIBLE);
            case 8:
                chaneel_8.setVisibility(View.VISIBLE);
            case 7:
                chaneel_7.setVisibility(View.VISIBLE);
            case 6:
                chaneel_6.setVisibility(View.VISIBLE);
            case 5:
                chaneel_5.setVisibility(View.VISIBLE);
            case 4:
                chaneel_4.setVisibility(View.VISIBLE);
            case 3:
                chaneel_3.setVisibility(View.VISIBLE);
            case 2:
                chaneel_2.setVisibility(View.VISIBLE);
            case 1:
                chaneel_1.setVisibility(View.VISIBLE);
        }
        //将布局设置给 dialog
        dialog.setContentView(inflate);
        //获取当前 Activity 所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置 dialog 从底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
        for (int i=0;i<15;i++){         //每次显示对话框后初始化标记
            chaneelSelect[i/5][i%5]=false;
            allChaneel=false;
        }
        //对话框解除时
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                imageView.setImageResource(R.drawable.strigon1);
                boolean flag=false;  //标记判断是否选择了通道
                //未选择通道
                for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                    if (chaneelSelect[i/5][i%5]) {
                        flag=true;
                    }
                }
                if (!flag){   //未选择通道，并且原文本为空
                    selectChannel.setText("");
                }else if(allChaneel){
                    selectChannel.setText("全部通道");
                }else{
                    selectChannel.setText("");
                    String chaneelStr="通道";
                    for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                        if (chaneelSelect[i/5][i%5]) {
                            chaneelStr +=(Integer.toString(i+1)+",");
                        }
                    }
                    chaneelStr=chaneelStr.substring(0,chaneelStr.length()-1);  //去掉最后一个逗号
                    selectChannel.setText(chaneelStr);
                }
            }
        });

        allChaneelButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!allChaneel){
                    allChaneel=true;      //选择全部通道
                    allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel_selected));
                    for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                        chaneelSelect[i/5][i%5]=true;
                    }
                    switch (SystemParameter.getInstance().nChannelNumber){
                        case 15:
                              chaneel_15.setImageDrawable(getDrawable(R.drawable.channel_15_selected));
                        case 14:
                              chaneel_14.setImageDrawable(getDrawable(R.drawable.channel_14_selected));
                        case 13:
                              chaneel_13.setImageDrawable(getDrawable(R.drawable.channel_13_selected));
                        case 12:
                              chaneel_12.setImageDrawable(getDrawable(R.drawable.channel_12_selected));
                        case 11:
                              chaneel_11.setImageDrawable(getDrawable(R.drawable.channel_11_selected));
                        case 10:
                              chaneel_10.setImageDrawable(getDrawable(R.drawable.channel_10_selected));
                        case 9:
                            chaneel_9.setImageDrawable(getDrawable(R.drawable.channel_9_selected));
                        case 8:
                            chaneel_8.setImageDrawable(getDrawable(R.drawable.channel_8_selected));
                        case 7:
                            chaneel_7.setImageDrawable(getDrawable(R.drawable.channel_7_selected));
                        case 6:
                            chaneel_6.setImageDrawable(getDrawable(R.drawable.channel_6_selected));
                        case 5:
                            chaneel_5.setImageDrawable(getDrawable(R.drawable.channel_5_selected));
                        case 4:
                            chaneel_4.setImageDrawable(getDrawable(R.drawable.channel_4_selected));
                        case 3:
                            chaneel_3.setImageDrawable(getDrawable(R.drawable.channel_3_selected));
                        case 2:
                            chaneel_2.setImageDrawable(getDrawable(R.drawable.channel_2_selected));
                        case 1:
                            chaneel_1.setImageDrawable(getDrawable(R.drawable.channel_1_selected));
                    }

                }else{
                    allChaneel=false;     //全部通道都未被选
                    allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                        chaneelSelect[i/5][i%5]=false;
                    }
                    switch (SystemParameter.getInstance().nChannelNumber){    //不加break语句，  小于等于通道数的都会被设置
                        case 15:
                            chaneel_15.setImageDrawable(getDrawable(R.drawable.channel_15));
                        case 14:
                            chaneel_14.setImageDrawable(getDrawable(R.drawable.channel_14));
                        case 13:
                            chaneel_13.setImageDrawable(getDrawable(R.drawable.channel_13));
                        case 12:
                            chaneel_12.setImageDrawable(getDrawable(R.drawable.channel_12));
                        case 11:
                            chaneel_11.setImageDrawable(getDrawable(R.drawable.channel_11));
                        case 10:
                             chaneel_10.setImageDrawable(getDrawable(R.drawable.channel_10));
                        case 9:
                            chaneel_9.setImageDrawable(getDrawable(R.drawable.channel_9));
                        case 8:
                            chaneel_8.setImageDrawable(getDrawable(R.drawable.channel_8));
                        case 7:
                            chaneel_7.setImageDrawable(getDrawable(R.drawable.channel_7));
                        case 6:
                            chaneel_6.setImageDrawable(getDrawable(R.drawable.channel_6));
                        case 5:
                            chaneel_5.setImageDrawable(getDrawable(R.drawable.channel_5));
                        case 4:
                            chaneel_4.setImageDrawable(getDrawable(R.drawable.channel_4));
                        case 3:
                            chaneel_3.setImageDrawable(getDrawable(R.drawable.channel_3));
                        case 2:
                            chaneel_2.setImageDrawable(getDrawable(R.drawable.channel_2));
                        case 1:
                            chaneel_1.setImageDrawable(getDrawable(R.drawable.channel_1));
                    }
                }
            }
        });

        chaneel_1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[0][0]){
                    chaneelSelect[0][0]=true;
                    chaneel_1.setImageDrawable(getDrawable(R.drawable.channel_1_selected));
                }else {
                    chaneelSelect[0][0]=false;
                    chaneel_1.setImageDrawable(getDrawable(R.drawable.channel_1));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_2.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[0][1]){
                    chaneelSelect[0][1]=true;
                    chaneel_2.setImageDrawable(getDrawable(R.drawable.channel_2_selected));
                }else {
                    chaneelSelect[0][1]=false;
                    chaneel_2.setImageDrawable(getDrawable(R.drawable.channel_2));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_3.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[0][2]){
                    chaneelSelect[0][2]=true;
                    chaneel_3.setImageDrawable(getDrawable(R.drawable.channel_3_selected));
                }else {
                    chaneelSelect[0][2]=false;
                    chaneel_3.setImageDrawable(getDrawable(R.drawable.channel_3));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_4.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[0][3]){
                    chaneelSelect[0][3]=true;
                    chaneel_4.setImageDrawable(getDrawable(R.drawable.channel_4_selected));
                }else {
                    chaneelSelect[0][3]=false;
                    chaneel_4.setImageDrawable(getDrawable(R.drawable.channel_4));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_5.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[0][4]){
                    chaneelSelect[0][4]=true;
                    chaneel_5.setImageDrawable(getDrawable(R.drawable.channel_5_selected));
                }else {
                    chaneelSelect[0][4]=false;
                    chaneel_5.setImageDrawable(getDrawable(R.drawable.channel_5));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_6.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[1][0]){
                    chaneelSelect[1][0]=true;
                    chaneel_6.setImageDrawable(getDrawable(R.drawable.channel_6_selected));
                }else {
                    chaneelSelect[1][0]=false;
                    chaneel_6.setImageDrawable(getDrawable(R.drawable.channel_6));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_7.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[1][1]){
                    chaneelSelect[1][1]=true;
                    chaneel_7.setImageDrawable(getDrawable(R.drawable.channel_7_selected));
                }else {
                    chaneelSelect[1][1]=false;
                    chaneel_7.setImageDrawable(getDrawable(R.drawable.channel_7));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_8.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[1][2]){
                    chaneelSelect[1][2]=true;
                    chaneel_8.setImageDrawable(getDrawable(R.drawable.channel_8_selected));
                }else {
                    chaneelSelect[1][2]=false;
                    chaneel_8.setImageDrawable(getDrawable(R.drawable.channel_8));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_9.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[1][3]){
                    chaneelSelect[1][3]=true;
                    chaneel_9.setImageDrawable(getDrawable(R.drawable.channel_9_selected));
                }else {
                    chaneelSelect[1][3]=false;
                    chaneel_9.setImageDrawable(getDrawable(R.drawable.channel_9));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_10.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[1][4]){
                    chaneelSelect[1][4]=true;
                    chaneel_10.setImageDrawable(getDrawable(R.drawable.channel_10_selected));
                }else {
                    chaneelSelect[1][4]=false;
                    chaneel_10.setImageDrawable(getDrawable(R.drawable.channel_10));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_11.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[2][0]){
                    chaneelSelect[2][0]=true;
                    chaneel_11.setImageDrawable(getDrawable(R.drawable.channel_11_selected));
                }else {
                    chaneelSelect[2][0]=false;
                    chaneel_11.setImageDrawable(getDrawable(R.drawable.channel_11));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_12.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[2][1]){
                    chaneelSelect[2][1]=true;
                    chaneel_12.setImageDrawable(getDrawable(R.drawable.channel_12_selected));
                }else {
                    chaneelSelect[2][1]=false;
                    chaneel_12.setImageDrawable(getDrawable(R.drawable.channel_12));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_13.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[2][2]){
                    chaneelSelect[2][2]=true;
                    chaneel_13.setImageDrawable(getDrawable(R.drawable.channel_13_selected));
                }else {
                    chaneelSelect[2][2]=false;
                    chaneel_13.setImageDrawable(getDrawable(R.drawable.channel_13));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_14.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[2][3]){
                    chaneelSelect[2][3]=true;
                    chaneel_14.setImageDrawable(getDrawable(R.drawable.channel_14_selected));
                }else {
                    chaneelSelect[2][3]=false;
                    chaneel_14.setImageDrawable(getDrawable(R.drawable.channel_14));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });

        chaneel_15.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!chaneelSelect[2][4]){
                    chaneelSelect[2][4]=true;
                    chaneel_15.setImageDrawable(getDrawable(R.drawable.channel_15_selected));
                }else {
                    chaneelSelect[2][4]=false;
                    chaneel_15.setImageDrawable(getDrawable(R.drawable.channel_15));
                    if (allChaneel){
                        allChaneel=false;
                        allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    }
                }
            }
        });
    }

    //开始校准
    public void startCalibration(){
        kValues=new ArrayList<>();
        zeroValues=new ArrayList<>();
        for (int i=0;i<chaneels.size();i++){
            kValues.add( (standardValue[1]-standardValue[0])/(xList2.get(chaneels.get(i))-xList1.get(chaneels.get(i))));
            //zeroValues.add( xList1.get(chaneels.get(i))-standardValue[0]/kValues.get(i));
            zeroValues.add(kValues.get(i)*xList1.get(chaneels.get(i)) -standardValue[0]);
            System.out.println("Values_k_"+chaneels.get(i)+": "+kValues.get(i));
            System.out.println("Values_zero_"+chaneels.get(i)+": "+zeroValues.get(i));
        }
        //保存到数据库
        ChannelDetectionRecordDao.getInstance().updateDetectionData(chaneels,kValues,zeroValues,0);
    }

    //从设备获取校准数据
    public ArrayList<Double> getDetectionData(){
        SerialPortOpe serialPortOpe =SerialPortOpe.getInstance(getApplicationContext());
        ArrayList<Double> resultList=null;
        if(serialPortOpe.initDevice()) {
            ThreadParameter.getInstance().initReadSizeWords();
            int sendChannelNum = SystemParameter.getInstance().nChannelNumber + 1;
            serialPortOpe.sendMessage("" + sendChannelNum);
            String data=null;
            while(data==null) {
                try {
                    data = serialPortOpe.getMeasureData();
                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
            serialPortOpe.stopReciveData();//获取一组数据后，立即停止接收数据，关闭设备
            resultList =dataProcess(data);//从设备获取数据
        }else{
            Toast.makeText(this,"设备初始化失败",Toast.LENGTH_LONG).show();
        }
        return  resultList;
    }

    //通过设备发过来的原始数据获得校准需要的X（电压）值
    public ArrayList<Double> dataProcess(String data){
        int count = 0;
        int nChannelNumber =SystemParameter.getInstance().nChannelNumber;
        List<List<Double>> nTempList = new ArrayList<>();
        ArrayList<Double> resultList = new ArrayList<>();
        for(int i=0;i<nChannelNumber;i++){//对tempList进行初始化
            nTempList.add(new ArrayList<Double>());
        }
        String[] nGroupChannelData = data.split("\n");         //一次发来的数据有多组 ，每组之间用换行符号隔开
        for (int dataIndex = 0; dataIndex < nGroupChannelData.length; dataIndex++) {
            if(count<20) {//取20组作平均
                String aGroupChannelData = nGroupChannelData[dataIndex];  //一组通道数据
                if (aGroupChannelData.length() != (nChannelNumber + 2) * 5) {
                    continue;//如果这组数据长度不满足则丢弃
                }
                for (int i = 0; i < nChannelNumber; i++) {
                    nTempList.get(i).add(Double.parseDouble(aGroupChannelData.substring(10 + 5 * i, 15 + 5 * i)));
                }
                count++;
            }else{
                break;
            }
        }
        for (int i = 0; i < nChannelNumber; i++) {
            List<Double> tempList = nTempList.get(i);
            Double[] arry = tempList.toArray(new Double[tempList.size()]);
            if (arry.length > 0) {
                Arrays.sort(arry);   //升序排序
                double x = arry[tempList.size() / 2]; //取中间数
                resultList.add(x);      //保存第i个通道的电压值
            }
            nTempList.get(i).clear(); //清空此通道相应的临时数据缓存
        }
        return resultList;
    }
}
