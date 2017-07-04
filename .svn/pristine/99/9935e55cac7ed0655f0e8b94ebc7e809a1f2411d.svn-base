package com.tomato.thread;

import android.content.Context;

import com.tomato.tools.Parameter.ThreadParameter;
import com.tomato.usbutil.SerialPortOpe;

/**
 *
 * 将原始数据读取到缓冲队列中
 */
public class ReadDataThread implements Runnable{
    ThreadParameter threadParameter;
    SerialPortOpe serialPortOpe;
    public ReadDataThread(Context parentContext){
        threadParameter= ThreadParameter.getInstance();              //获取线程参数类的对象
        serialPortOpe=SerialPortOpe.getInstance(parentContext);      //获取设备操作类的对象
    }

    @Override
    public void run(){
        int index=0;
        while (threadParameter.threadFlag) {
            String data=serialPortOpe.getMeasureData();//读取长度为ThreadParameter.getInstance().nReadSizeWords的一个字符串
            if(data!=null){
                threadParameter.ADBuffer[index] = data;         //数据存入缓冲队列
                threadParameter.bNewSegmentData[index]=true;  //对应的标志改为true
                index++;                                           //继续存入下一组
                index%=threadParameter.MAX_SEGMENT;
            }
        }
    }
}
