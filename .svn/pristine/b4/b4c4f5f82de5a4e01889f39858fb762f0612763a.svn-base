package com.tomato.tools.dataBase.Dao;

import android.content.ContentValues;

import com.tomato.tools.dataBase.Bean.ChannelDetectionRecordBean;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对通道校准记录的数据库操作类
 */
public class ChannelDetectionRecordDao {
    //存储方法对象的集合
    private List<Method> setKMethodList = new ArrayList<>();
    private List<Method> setZMethhodList =new ArrayList<>();
    private List<Method> getKMethodList = new ArrayList<>();
    private List<Method> getZMethhodList =new ArrayList<>();
    //生成单例
    private static ChannelDetectionRecordDao channelDetectionRecordDao = null;
    private ChannelDetectionRecordDao() {
        LitePal.getDatabase();
        try {
            Class<?> CDR_class = ChannelDetectionRecordBean.class;
            for (int i = 1; i <= 30; i++) {
                Method setK = CDR_class.getMethod(
                        "setkChannel"+i, double.class);//这里注意不能写成Double.class（不支持自动拆箱）
                Method setZero= CDR_class.getMethod(
                        "setZeroChannel"+i, double.class);
                Method getK = CDR_class.getMethod(
                        "getkChannel"+i);
                Method getZero= CDR_class.getMethod(
                        "getZeroChannel"+i);
                setKMethodList.add(setK);
                setZMethhodList.add(setZero);
                getKMethodList.add(getK);
                getZMethhodList.add(getZero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ChannelDetectionRecordDao getInstance() {
        if (channelDetectionRecordDao == null) {
            channelDetectionRecordDao = new ChannelDetectionRecordDao();
        }
        return channelDetectionRecordDao;
    }

    /*****************************************************************
     * 方法名  :  getStatusIsZeroBean
     * 功能描述: 得到状态为0的那条记录对应的对象
     ******************************************************************/
    private ChannelDetectionRecordBean getStatusIsZeroBean(){
        ChannelDetectionRecordBean bean = DataSupport
                .where("status == ?", String.valueOf(0))
                .find(ChannelDetectionRecordBean.class).get(0);//状态为0的记录只有一条
        return bean;
    }


    /************************************************************************************
     * 方法名  :  initChannelDetectionRecordTable
     * 功能描述: 初始状态默认向通道校准记录明细表中插入一行数据(默认K值全为1，零值全为0)
     ************************************************************************************/
    public void initChannelDetectionRecordTable(){
        int rowCount = DataSupport.count(ChannelDetectionRecordBean.class);
        if (rowCount == 0) {
            ChannelDetectionRecordBean chanDeteRecordBean= new ChannelDetectionRecordBean();
            Date date = new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");
            String detectionTime = sdf.format(date);
            chanDeteRecordBean.setDetectionTime(detectionTime);
            chanDeteRecordBean.setStatus(0);
            chanDeteRecordBean.setDetectionWay(0);
            chanDeteRecordBean.setNote("在用");
            try {
                for (int i = 0; i <30; i++) {
                    Method setK =setKMethodList.get(i);
                    Method setZero=setZMethhodList.get(i);
                    setK.invoke(chanDeteRecordBean,1);
                    setZero.invoke(chanDeteRecordBean,0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            chanDeteRecordBean.save();
        }
    }

    /**********************************************************************************************
     * 方法名  :  updateDetectionData
     * 功能描述: 更新校准数据,将状态为0的那一行数据取出，更新相应通道K值和零值,并将上一条置为历史
     **********************************************************************************************/
    public void updateDetectionData(List<Integer> indexList,List<Double> kValueList,List<Double> zeroValueList,int detectionWay) {
        if(kValueList.size()>0&&zeroValueList.size()>0) {
            ChannelDetectionRecordBean cDR = getStatusIsZeroBean();//得到上一条状态为再用的那一行
            long id = cDR.getId();
            cDR.setStatus(1);//将上一条置为历史
            cDR.setNote("历史");
            cDR.update(id);
            ChannelDetectionRecordBean cDRtemp = new ChannelDetectionRecordBean();//每次都new一个新对象
            try {
                //将上一条记录的K值和零值取出存储到新对象中
                for(int i=0;i<30;i++){
                    Method getK = getKMethodList.get(i);
                    Method getZ = getZMethhodList.get(i);
                    Method setK = setKMethodList.get(i);
                    Method setZ = setZMethhodList.get(i);
                    setK.invoke(cDRtemp,getK.invoke(cDR));
                    setZ.invoke(cDRtemp,getZ.invoke(cDR));
                }
                //通过校准获得的新数据更新此对象
                for (int i = 0, length = indexList.size(); i < length; i++) {
                    Method setK = setKMethodList.get(indexList.get(i));
                    Method setZ = setZMethhodList.get(indexList.get(i));
                    setK.invoke(cDRtemp, kValueList.get(i)); //更新相应通道K值
                    setZ.invoke(cDRtemp, zeroValueList.get(i));//更新相应通道零值
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String detectionTime = sdf.format(date);
            cDRtemp.setDetectionTime(detectionTime);//更新时间
            cDRtemp.setStatus(0);//将当前状态置为在用
            cDRtemp.setDetectionWay(detectionWay);//更新校准方式
            cDRtemp.setNote("在用");//备注
            cDRtemp.save();//插入一行新数据
        }else{
            System.out.println("KList.size==0");
        }
    }


    /**********************************************************************************************
     * 方法名  :  updateDetectionData_secondWay
     * 功能描述: 更新校准数据第二种方法,将状态为0的那一行数据取出，更新相应通道K值和零值,并将上一条置为历史
     **********************************************************************************************/
    public void updateDetectionData_secondWay(ChannelDetectionRecordBean bean ,int detectionWay){
        //更新上一条数据
        ChannelDetectionRecordBean cDR = getStatusIsZeroBean();
        long id =cDR.getId();
        cDR.setStatus(1);//将上一条置为历史
        cDR.setNote("历史");
        cDR.update(id);
        //插入新数据
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSS");
        String detectionTime = sdf.format(date);
        bean.setDetectionTime(detectionTime);
        bean.setStatus(0);//将当前状态置为在用
        bean.setDetectionWay(detectionWay);//更新校准方式
        bean.setNote("在用");
        bean.save();//插入一行新数据
    }


    /*****************************************************************
     * 方法名  :  delete
     * 功能描述: 根据ID删除一组数据(其实是将其状态置为2)
     ******************************************************************/
    public void delete(long id) {
        ContentValues cv = new ContentValues();
        cv.put("status", 2);
        DataSupport.update(ChannelDetectionRecordBean.class, cv, id);
    }


    /*****************************************************************
     * 方法名  :  getZeroChannelList
     * 功能描述: 返回状态为在用那一行的所有零值
     ******************************************************************/
    public List getZeroChannelList() {
        ChannelDetectionRecordBean cDR = getStatusIsZeroBean();
        List<Double> zeroChannelList = new ArrayList();
        try {
            for (int i = 0; i <30; i++) {
                Method get = getZMethhodList.get(i);
                zeroChannelList.add((double)get.invoke(cDR));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return zeroChannelList;
    }

    /*****************************************************************
     * 方法名  :  getKChannelList
     * 功能描述: 返回状态为在用那一行的所有K值
     ******************************************************************/
    public List getKChannelList() {
        ChannelDetectionRecordBean cDR = getStatusIsZeroBean();
        List<Double> kChannelList = new ArrayList();
        try {
            for (int i = 0; i <30; i++) {
                Method get = getKMethodList.get(i);
                kChannelList.add((double)get.invoke(cDR));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return kChannelList;
    }
}
