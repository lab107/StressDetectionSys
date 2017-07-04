package com.tomato.tools.dataBase.Dao;


import com.tomato.tools.dataBase.Bean.SysInfoBean;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * 对系统信息的数据库操作类
 */
public class SysInfoDao {

    private static SysInfoDao sysInfoDao = null;
    private SysInfoDao() {
        LitePal.getDatabase();
    }
    public static SysInfoDao getInstance() {
        if (sysInfoDao == null) {
            sysInfoDao = new SysInfoDao();
        }
        return sysInfoDao;
    }

    /*****************************************************************
     * 方法名  :  insertFirstRow
     * 功能描述: 将SysInfoBean对象存入数据库
     *
     ******************************************************************/
    public void initSysInfoTable(){
        int rowCount = DataSupport.count(SysInfoBean.class);
        if (rowCount == 0) {
            SysInfoBean sysInfoBean= new SysInfoBean();
            sysInfoBean.setDeviceID(1);
            sysInfoBean.setDeviceName("设备1");
            sysInfoBean.setChannelCount(6);
            sysInfoBean.setChannelWeight(1);
            sysInfoBean.setChannelDistance(1);
            sysInfoBean.setStepInterval(1);
            sysInfoBean.setStepDistance(0.942);
            sysInfoBean.setDetectionID(1);//默认监检测的是第一个对象
            sysInfoBean.save();
        }
    }

    /*****************************************************************
     * 方法名  :  delete
     * 输入参数：systemID:需要删除的SysInfoBean对象的ID
     * 输出参数：无
     * 功能描述:  删除系统信息
     ******************************************************************/
    public void delete(int systemID){
        DataSupport.deleteAll(SysInfoBean.class, "id == ?", String.valueOf(systemID));
    }

    /*****************************************************************
     * 方法名  :  update
     * 输入参数：SysInfoBean 对象
     * 输出参数：无
     * 功能描述:   更新系统信息
     ******************************************************************/
    public void update(SysInfoBean sysInfoBean){
        sysInfoBean.update(1);
    }

    /*****************************************************************
     * 方法名  :  query
     * 输入参数：systemID:需要查询的SysInfoBean记录的ID
     * 输出参数：SysInfoBean对象
     * 功能描述:  查询系统信息
     ******************************************************************/
    public SysInfoBean query(){
        //如果需要查询的数据量很大，建议使用事务
        List<SysInfoBean> sysInfoBeans = DataSupport
                .where("id == ?", String.valueOf(1))
                .find(SysInfoBean.class);
        return sysInfoBeans.get(0);
    }
}
