package com.tomato.tools.Parameter;

import com.tomato.tools.dataBase.Bean.SysInfoBean;
import com.tomato.tools.dataBase.Dao.ChannelDetectionRecordDao;
import com.tomato.tools.dataBase.Dao.DetectionObjectInfoDao;
import com.tomato.tools.dataBase.Dao.SysInfoDao;

/**
 * Created by zch22 on 2017/5/16.
 *  系统参数
 */
public class SystemParameter {

    public int nChannelNumber;                                        // 采样通道数
    public int channelWeight;                                         // 通道分量，默认值为1
    public int nChannelDistance;                                     // 通道间距
    public double disSensorStepLen;                                 // 位移传感器步长(单位：mm)
    public int nStepInterval;                                        //横向梯度间隔
    public int nDetectionObjectID;                                  // 当前检测对象ID

    private static  SystemParameter systemParameter=null;
    //向相应表中插入初始数据，并初始化系统参数
    private SystemParameter(){
        DetectionObjectInfoDao.getInstance().initDetectionObjectTable();//初始化检测对象表
        ChannelDetectionRecordDao.getInstance().initChannelDetectionRecordTable();//初始化通道校准记录表
        SysInfoDao.getInstance().initSysInfoTable();//初始化系统信息表
        SysInfoBean sysBean=SysInfoDao.getInstance().query();//从数据库取出系统信息
        nChannelDistance=sysBean.getChannelDistance();
        channelWeight=sysBean.getChannelWeight();
        disSensorStepLen=sysBean.getStepDistance();
        nChannelNumber =sysBean.getChannelCount();
        nStepInterval = sysBean.getStepInterval();
        nDetectionObjectID =sysBean.getDetectionID();

    }

    public static SystemParameter getInstance(){
        if(systemParameter==null){
            systemParameter=new SystemParameter();
        }
        return systemParameter;
    }
}
