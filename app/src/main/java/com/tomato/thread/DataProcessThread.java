package com.tomato.thread;

import android.os.Handler;
import android.os.Message;

import com.tomato.tools.Parameter.SystemParameter;
import com.tomato.tools.Parameter.ThreadParameter;
import com.tomato.tools.chart.ChartService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *  数据处理线程：
 *  从缓冲队列中取出原始数据进行处理并将处理后的数据保存
 */
public class DataProcessThread implements Runnable{
    private ThreadParameter threadParameter;        //线程参数对象
    private SystemParameter systemParameter;        //系统参数对象
    private Handler firstFragmentHandler;          //第一个Fragment传过来的Handler
    private Handler secondFragmentHandler;         //第二个Fragment传过来的Handler
    private Handler thirdFragmentHandler;          //第三个Fragment传过来的Handler
    private Handler detectionHandler;               //地磁场校准的Handler
    ChartService mService;
    private List<Integer> chaneels;
    private int shiftStatus;               //移动状态，0-静止，1-开始前进，2-开始后退，3-前进结束，4-后退结束
    private double preVoltShiftData;      //第1个位移传感器的上一个值，用于判断位移状态
    List<List<Double>> nTempList;             //暂时存储一步之内多个通道的多个数据
    private int count;                      //计数器
    private int stepCount;                  //步数
    ChartService firstServive;
    ChartService firstServive_1;
    ChartService secondServive;
    ChartService secondServive_1;
    ChartService thirdServive;
    ChartService thirdServive_1;
    private static File file;
    private static FileWriter fileWriter;


    public DataProcessThread(Handler handler,ChartService mService,List<Integer> chaneels){
        threadParameter=ThreadParameter.getInstance();
        systemParameter = SystemParameter.getInstance();
        this.detectionHandler=handler;
        this.mService=mService;
        this.chaneels=chaneels;
        shiftStatus=0;
        preVoltShiftData=0;
        count=0;
        stepCount=0;
        nTempList = new ArrayList<>();
        for(int i=0;i<systemParameter.nChannelNumber;i++){//对tempList进行初始化
            nTempList.add(new ArrayList<Double>());
        }
    }

    public DataProcessThread(Handler firstHandler, Handler secondHandler, Handler thirdHandler, ChartService firstServive,ChartService firstServive_1,ChartService secondServive,ChartService secondServive_1,ChartService thirdServive,ChartService thirdServive_1){
        threadParameter=ThreadParameter.getInstance();
        systemParameter = SystemParameter.getInstance();
        firstFragmentHandler=firstHandler;
        secondFragmentHandler=secondHandler;
        thirdFragmentHandler=thirdHandler;
        shiftStatus=0;
        preVoltShiftData=0;
        count=0;
        stepCount=0;
        nTempList = new ArrayList<>();
        for(int i=0;i<systemParameter.nChannelNumber;i++){//对tempList进行初始化
            nTempList.add(new ArrayList<Double>());
        }
        this.firstServive=firstServive;
        this.firstServive_1=firstServive_1;
        this.secondServive=secondServive;
        this.secondServive_1=secondServive_1;
        this.thirdServive=thirdServive;
        this.thirdServive_1=thirdServive_1;

        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //sdf.format(date)
        String filename = sdf.format(date)+".txt";
        file=new File("/mnt/sdcard/doc/",filename);
        try {
            fileWriter=new FileWriter(file,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*每次有多组数据，对于每一组数据，其开始两个通道的数据用来判断设备是前进还是后退，判断出来后，为shiftStatus赋相应值
     *比如如果 shiftStatus 一直等于1就说明设备一直处在前进过程中，当shiftStatus突变为3的时候说明前进结束，
     *在这个过程中每个通道会有多个数据，而我们要对每个通道的多个数据取均值，方法为（先排序，取中间值）然后将每个通道对应的均值数据存入缓冲池
     *shiftStatus由1变为3的过程称为一步，横坐标是在上一条数据的横坐标上加上步长*/
    @Override
    public void run() {
        while (threadParameter.threadFlag) {
            int index =threadParameter.readDataIndex;            //当前从缓冲队列中取出数据对应的下标值
            if (!threadParameter.bNewSegmentData[index]) {       //如果当前数据不是最新的
                continue;
            }
            String data = threadParameter.ADBuffer[index];      //从缓冲队列中取出原始数据
            if(data==null){   //再次判断防止data为null
                continue;
            }
            String[] nGroupChannelData = data.split("\n");         //一次发来的数据有多组 ，每组之间用换行符号隔开
            for (int dataIndex = 0; dataIndex < nGroupChannelData.length; dataIndex++) {
                String aGroupChannelData = nGroupChannelData[dataIndex];  //一组通道数据
                if(aGroupChannelData.length()!=(systemParameter.nChannelNumber+2)*5){
                    continue;//如果这组数据长度不满足则丢弃
                }
                if(threadParameter.ifHaveEncoder) {
                    dataAnalyzeWithEncoder(aGroupChannelData);    //有位移的处理方法
                }else {
                    dataAnalyzeWithoutEncoder(aGroupChannelData);   //无位移的处理方法
                }
            }
            threadParameter.bNewSegmentData[index] = false;              //数据取出之后将标志复原
            threadParameter.readDataIndex++;                            //将游标向后移动一位
            threadParameter.readDataIndex %= threadParameter.MAX_SEGMENT;//存满后，使游标重新指向0
        }
    }


    DecimalFormat df   = new DecimalFormat("#0.0000");
    //有位移的处理方法
    private void dataAnalyzeWithEncoder(String aGroupChannelData){
//        System.out.println("当前线程：数据处理"+Thread.currentThread().getName());
        double firstSensorValue =(Integer.parseInt(aGroupChannelData.substring(0, 5))-32768)*(5000.0 / 32768);//第一个位移传感器的值
        double secondSensorValue =(Integer.parseInt(aGroupChannelData.substring(5, 10))-32768)*(5000.0 / 32768);;//第二个位移传感器的值
        if (preVoltShiftData > threadParameter.nMaxThreshold && firstSensorValue <  threadParameter.nMinThreshold && secondSensorValue > threadParameter.nMaxThreshold) {
            shiftStatus = 1;//开始前进(第1个传感器从高电平变为低电平，而此时第2个传感器处于高电平)
        }
        else if (preVoltShiftData > threadParameter.nMaxThreshold && firstSensorValue <  threadParameter.nMinThreshold && secondSensorValue <  threadParameter.nMinThreshold) {
            shiftStatus = 2;//开始后退(第1个传感器从高电平变为低电平，而此时第2个传感器处于低电平)
        }
        else if (preVoltShiftData <  threadParameter.nMinThreshold && firstSensorValue > threadParameter.nMaxThreshold && secondSensorValue <  threadParameter.nMinThreshold && shiftStatus == 1) {
            shiftStatus = 3;//前进结束(//第1个传感器从低电平变为高电平，而此时第2个传感器处于低电平)
        }
        else if (preVoltShiftData <  threadParameter.nMinThreshold && firstSensorValue > threadParameter.nMaxThreshold && secondSensorValue > threadParameter.nMaxThreshold && shiftStatus == 2) {
            shiftStatus = 4;//后退结束(第1个传感器从低电平变为高电平，而此时第2个传感器处于高电平)
        }
        preVoltShiftData = firstSensorValue;        //保留第1个位移传感器的值
        if(shiftStatus!=0) {  //如果不是静止状态
            if (count < 100 && (shiftStatus == 1 || shiftStatus == 2)) {//最多取100数据做平均
                for (int i = 0; i < systemParameter.nChannelNumber; i++) {
                    nTempList.get(i).add(Double.parseDouble(aGroupChannelData.substring(10 + 5 * i, 15 + 5 * i)));
                }
                count++;
            }else {
                if (shiftStatus == 3) {   //移动一格，显示一个数据，曲线向前走一格
                    try {
                        fileWriter.write(String.valueOf(firstSensorValue)+"  ");
                        fileWriter.write(String.valueOf(secondSensorValue)+"  ");
                    } catch (Exception e) {
                        //写入异常时
                        e.printStackTrace();
                    }
                    for (int i = 0; i < systemParameter.nChannelNumber; i++) {
                        Double kValue = threadParameter.kValue.get(i);            //取得第i通道的K值
                        Double zeroValue = threadParameter.zeroValue.get(i);      //取得第i通道零值
                        List<Double> tempList = nTempList.get(i);               //第i个通道的处于前进或后退过程中的多个数据（最多取100个）
                        Double[] array = tempList.toArray(new Double[tempList.size()]);
                        if (array.length > 0) {
                            Arrays.sort(array);   //升序排序
                            double x = array[tempList.size() / 2]; //取中间数
                            threadParameter.yList.get(i).add(Double.valueOf(df.format(kValue * (x - 32768) * (5000.0 / 32768) - zeroValue))); //保存第i个通道的纵坐标
                            try {
                                fileWriter.write(String.valueOf(x)+"  ");
                            } catch (Exception e) {
                                //写入异常时
                                e.printStackTrace();
                            }
//                            System.out.println("x ="+ x );
//                            System.out.println("ylist:  "+threadParameter.yList.get(i).get(threadParameter.yList.get(i).size()-1));
                        }
                        int size = threadParameter.yList.get(i).size();
                        if(size==1){
                            threadParameter.yList_1.get(i).add(0.0);
                        }else {
                            if (size <= systemParameter.nStepInterval) {
                                double y =Math.abs(threadParameter.yList.get(i).get(size - 1) - threadParameter.yList.get(i).get(size - 2));
                                threadParameter.yList_1.get(i).add(Double.valueOf(df.format(y/systemParameter.disSensorStepLen)));
                            } else {
                                double y=Math.abs(threadParameter.yList.get(i).get(size - 1) - threadParameter.yList.get(i).get(size - 1-systemParameter.nStepInterval));
                                threadParameter.yList_1.get(i).add(Double.valueOf(df.format(y/systemParameter.disSensorStepLen)));
                            }
                        }
                        nTempList.get(i).clear();        //清空此通道相应的临时数据缓存
                    }
                    try {
                        //保存文件名和内容
                        fileWriter.write("\n");
//                        fileWriter.close();
                    } catch (Exception e) {
                        //写入异常时
                        e.printStackTrace();
                    }
                    //通过原始数据纵坐标得到纵向梯度纵坐标
                    double temp=0;
                    for(int i=0;i< systemParameter.nChannelNumber-1;i++){
                        int size = threadParameter.yList.get(i).size();
                        double value=Math.abs(Double.valueOf(df.format(threadParameter.yList.get(i+1).get(size-1)-threadParameter.yList.get(i).get(size-1))))/systemParameter.nChannelDistance;
                        threadParameter.yList_2.get(i).add(value);
                        temp = value;
                    }
                    threadParameter.yList_2.get(systemParameter.nChannelNumber-1).add(temp);
                    stepCount++;//步数加1
                    threadParameter.xList.add(stepCount * SystemParameter.getInstance().disSensorStepLen/1000);
                    count = 0;
                }else if (shiftStatus == 4) {//后退一格，擦除一个数据，曲线向后走一格
                    int size =threadParameter.xList.size();
                    if (size > 0) {
                        for (int i = 0; i < systemParameter.nChannelNumber; i++) {
                            threadParameter.yList.get(i).remove(size - 1);
                            threadParameter.yList_1.get(i).remove(size-1);
                            if(i<systemParameter.nChannelNumber-1) {
                                threadParameter.yList_2.get(i).remove(size-1);
                            }
                            nTempList.get(i).clear();        //清空此通道相应的临时数据缓存
                        }
                        threadParameter.xList.remove(size - 1);
                        stepCount--;        //步数减1
                    }
                    count = 0;
                }
                int size = threadParameter.xList.size();
                if ((shiftStatus == 3 || shiftStatus == 4) && size > 0) {
                    double []array = new double[threadParameter.yList.size()];
                    double []array1 = new double[threadParameter.yList_1.size()];
                    double []array2 = new double[threadParameter.yList_2.size()];
                    if (shiftStatus==3){
                        try {
                            for (int i = 0; i < threadParameter.yList.size(); i++) {
                                array[i] = threadParameter.yList.get(i).get(size - 1);
                            }
                            for (int i = 0; i < threadParameter.yList_1.size(); i++) {
                                array1[i] = threadParameter.yList_1.get(i).get(size - 1);
                            }
                            for (int i = 0; i < threadParameter.yList_2.size(); i++) {
                                array2[i] = threadParameter.yList_2.get(i).get(size - 1);
                            }
                            double temp=threadParameter.xList.get(size - 1);
                            firstServive.drawMeasureChart(temp,array,shiftStatus);
                            firstServive_1.drawMeasureChart(temp,array1,shiftStatus);
                            secondServive.drawMeasureChart(temp,array,shiftStatus);
                            secondServive_1.drawMeasureChart(temp,array2,shiftStatus);
                            thirdServive.drawMeasureChart(temp,array1,shiftStatus);
                            thirdServive_1.drawMeasureChart(temp,array2,shiftStatus);

                            firstFragmentHandler.sendMessage(Message.obtain());  //通知主线程更新第一个界面
                            secondFragmentHandler.sendMessage(Message.obtain()); //通知主线程更新第二个界面
                            thirdFragmentHandler.sendMessage(Message.obtain());  //通知主线程更新第三个界面
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        firstServive.drawMeasureChart(0,array,shiftStatus);
                        firstServive_1.drawMeasureChart(0,array1,shiftStatus);
                        secondServive.drawMeasureChart(0,array,shiftStatus);
                        secondServive_1.drawMeasureChart(0,array2,shiftStatus);
                        thirdServive.drawMeasureChart(0,array1,shiftStatus);
                        thirdServive_1.drawMeasureChart(0,array2,shiftStatus);

                        firstFragmentHandler.sendMessage(Message.obtain());  //通知主线程更新第一个界面
                        secondFragmentHandler.sendMessage(Message.obtain()); //通知主线程更新第二个界面
                        thirdFragmentHandler.sendMessage(Message.obtain());  //通知主线程更新第三个界面*/
                    }
                    shiftStatus = 0;//将状态置初始值
                }
            }
        }
    }

    //地磁场校准无位移的处理方法
    private void dataAnalyzeWithoutEncoder(String aGroupChannelData){
        if (count < 20) {//取20数据做平均
            for (int i = 0; i < systemParameter.nChannelNumber; i++) {
                nTempList.get(i).add(Double.parseDouble(aGroupChannelData.substring(10+5 * i, 15+ 5 * i)));
            }
            count++;
        }else{
            for (int i = 0; i < systemParameter.nChannelNumber; i++) {
                List<Double> i_ChannelTemp = nTempList.get(i);               //第i个通道的处于前进或后退过程中的多个数据
                Double[] arry = i_ChannelTemp.toArray(new Double[i_ChannelTemp.size()]);
                if (arry.length > 0) {
                    Arrays.sort(arry);   //升序排序
                    double x = arry[i_ChannelTemp.size() / 2]; //取中间数
                    threadParameter.yList.get(i).add((x - 32768) * (5000.0 / 32768));      //保存第i个通道的纵坐标
                }
                nTempList.get(i).clear();        //清空此通道相应的临时数据缓存
            }
            stepCount++;//步数加1
            double x=stepCount*SystemParameter.getInstance().disSensorStepLen/1000;
            threadParameter.xList.add(x);//测试用
            count = 0;
            double[] y=new double[chaneels.size()];
            for (int i = 0; i < chaneels.size(); i++) {
                y[i] = threadParameter.yList.get(chaneels.get(i)).get(stepCount - 1);
            }

            mService.drawDetectionChart(x,y);
            detectionHandler.sendMessage(Message.obtain());  //通知主线程更新第一个界面
        }
    }
}
