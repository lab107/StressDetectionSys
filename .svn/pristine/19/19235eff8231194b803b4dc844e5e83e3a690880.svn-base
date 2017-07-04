package com.tomato.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.thread.DataProcessThread;
import com.tomato.thread.ReadDataThread;
import com.tomato.tools.Parameter.SystemParameter;
import com.tomato.tools.Parameter.ThreadParameter;
import com.tomato.tools.chart.ChartService;
import com.tomato.tools.dataBase.Dao.ChannelDetectionRecordDao;
import com.tomato.usbutil.SerialPortOpe;

import org.achartengine.GraphicalView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 地磁场校准页面
 */
public class MagneticCalibrationActivity extends Activity {
    private SerialPortOpe serialPortOpe=null;//串口操作工具类

    public ChartService mService = null;   //画图工具类,控制原始曲线的小表及弹出窗口的大表
    private GraphicalView mGraphicalView;  //原始曲线图表
    private LinearLayout chartLayout;

    private ImageView back;
    private ImageButton btnStart;
    private ImageButton btnFinish;
    private ImageButton btnQuit;
    private TextView selectChaneel;
    private ImageView imageView;
    private boolean isStart=false;

    private Dialog dialog;
    private List<Integer> chaneels;    //通道选择
    //所选通道的标记
    static private boolean allChaneel=false;
    static private boolean[][] chaneelSelect={
            {false,false,false,false,false},
            {false,false,false,false,false},
            {false,false,false,false,false},
    };

     //更新图表
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mService.repaintDetectionChart();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.magnetic_calibration);
        initView();
        initEvent();

    }

    public void initView(){
        btnStart=(ImageButton)findViewById(R.id.btnStart);
        btnFinish=(ImageButton)findViewById(R.id.btnFinish);
        btnFinish.setEnabled(false);
        btnQuit=(ImageButton)findViewById(R.id.btnQuit);
        selectChaneel=(TextView)findViewById(R.id.selectChaneel);
        imageView=(ImageView)findViewById(R.id.img_select);
        chartLayout=(LinearLayout)findViewById(R.id.chart);
        back=(ImageView)findViewById(R.id.return_key);
        serialPortOpe=SerialPortOpe.getInstance(getApplicationContext());
        mService = new ChartService(MagneticCalibrationActivity.this,SystemParameter.getInstance().nChannelNumber,1);
        mService.setXYMultipleSeriesDataset("");
        mService.setXYMultipleSeriesRenderer();
        mGraphicalView= mService.getGraphicalView();    //地磁场校准的图表
        chartLayout.removeAllViews();
        chartLayout.addView(mGraphicalView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    public void initEvent(){
        //开始校准
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectChaneel.getText()==""){
                    Toast.makeText(MagneticCalibrationActivity.this,"请选择通道",Toast.LENGTH_SHORT).show();
                }else {
                    isStart=true;
                    //获得选中通道的下标
                    chaneels=new ArrayList<Integer>();
                    for (int i=0;i<15;i++){
                        if (chaneelSelect[i/5][i%5])
                            chaneels.add(i);   //如果选择了通道，则将通道的id添加的集合chaneels中
                    }
                    mService = new ChartService(MagneticCalibrationActivity.this,chaneels.size(),1);
                    mService.setXYMultipleSeriesDataset("");
                    mService.setXYMultipleSeriesRenderer();
                    mGraphicalView= mService.getGraphicalView();    //地磁场校准的图表
                    chartLayout.removeAllViews();
                    chartLayout.addView(mGraphicalView, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    //如果初始化并打开设备成功，则返回true,否则返回false
                    serialPortOpe.createDeviceList();
                    boolean flag = serialPortOpe.initDevice();
                    if (flag) {
                        ThreadParameter.getInstance().initThreadParameter();//初始化参数
                        ThreadParameter.getInstance().ifHaveEncoder = false;//以无位移的方式处理数据
                        ThreadParameter.getInstance().threadFlag = true;   //打开线程开关
                        ReadDataThread readDataThread = new ReadDataThread(getApplicationContext());
                        new Thread(readDataThread).start();            //启动读数据线程
                        DataProcessThread dataProcessThread = new DataProcessThread(handler,mService,chaneels);
                        new Thread(dataProcessThread).start();         //启动数据处理线程
                        int sendChannelNum = SystemParameter.getInstance().nChannelNumber + 1;
                        serialPortOpe.sendMessage("" + sendChannelNum);
                        btnFinish.setEnabled(true);
                    }
                }
            }
        });

        //同右上角的完成
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart == true){
                    isStart=false;
                    if (!mService.isFrist){
                        mService.isFrist=true;
                    }
                    serialPortOpe.stopReciveData();    //停止接收数据，关闭设备
                    ThreadParameter.getInstance().threadFlag=false;     //停止线程
                    //根据选中的通道从yList.get(?)中取出最大值和最小值,计算K和零值，然后更新到数据库
                    List<Double> kValueList = new ArrayList<Double>();
                    List<Double> zeroListt = new ArrayList<Double>();
                    for(int i=0;i<chaneels.size();i++){
                        List<Double> list=ThreadParameter.getInstance().yList.get(chaneels.get(i));
                        if(list.size()>0) {
                            double max = Collections.max(list);
                            double min = Collections.min(list);
                            double k = 80.0/ (max - min);
                            double zero = k * max - 40.0;
                            kValueList.add(k);
                            zeroListt.add(zero);
                        }
                    }
                    ChannelDetectionRecordDao.getInstance().updateDetectionData(chaneels,kValueList,zeroListt,1);
                    ThreadParameter.getInstance().clearXAndYList();
                    Toast.makeText(getApplication(), "校准成功！",Toast.LENGTH_SHORT).show();
                    mService.clearDetectionChart();
                }else {
                    Toast.makeText(getApplication(), "请先开始！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnQuit.callOnClick();
            }
        });

        //直接退出
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mService.isFrist){
                    mService.isFrist=true;
                }
                finish();
            }
        });

        selectChaneel.setOnClickListener(new View.OnClickListener() {
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
    }

    public void showDialog(){
        dialog = new Dialog(MagneticCalibrationActivity.this, R.style.common_dialog);
        //对话框布局
        View inflate = LayoutInflater.from(MagneticCalibrationActivity.this).inflate(R.layout.select_channel_dialog, null);
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
        for (int i=0;i<15;i++){
            chaneelSelect[i/5][i%5]=false;
            allChaneel=false;
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                imageView.setImageResource(R.drawable.strigon1);
                boolean flag=false;
                //未选择通道
                for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                    if (chaneelSelect[i/5][i%5]) {
                        flag=true;
                    }
                }
                if (!flag){   //未选择通道，并且原文本为空
                    selectChaneel.setText("");
                }else if(allChaneel){
                    selectChaneel.setText("全部通道");
                }else{
                    selectChaneel.setText("");
                    String chaneelStr="通道";
                    for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                        if (chaneelSelect[i/5][i%5]) {
                            chaneelStr +=(Integer.toString(i+1)+",");
                        }
                    }
                    chaneelStr=chaneelStr.substring(0,chaneelStr.length()-1);
                    selectChaneel.setText(chaneelStr);
                }
            }
        });

        allChaneelButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!allChaneel){
                    allChaneel=true;
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
                    allChaneel=false;
                    allChaneelButton.setImageDrawable(getDrawable(R.drawable.all_channel));
                    for (int i=0;i<SystemParameter.getInstance().nChannelNumber;i++){
                        chaneelSelect[i/5][i%5]=false;
                    }
                    switch (SystemParameter.getInstance().nChannelNumber){
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
}
