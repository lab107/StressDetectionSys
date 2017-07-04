package com.tomato.tools.dataBase.Dao;

import com.tomato.tools.dataBase.Bean.DetectionObjectInfoBean;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * 对检测对象信息的数据库操作类
 */
public class DetectionObjectInfoDao {

    private static DetectionObjectInfoDao detectionObjectInfoDao = null;
    private DetectionObjectInfoDao() {
        LitePal.getDatabase();
    }
    public static DetectionObjectInfoDao getInstance() {
        if (detectionObjectInfoDao == null) {
            detectionObjectInfoDao = new DetectionObjectInfoDao();
        }
        return detectionObjectInfoDao;
    }

    /*****************************************************************
     * 方法名  :  initDetectionObjectTable
     * 功能描述: 初始状态默认向检测对象表中插入一行数据
     * 修改历史：
     ******************************************************************/
    public void initDetectionObjectTable(){
        int rowCount = DataSupport.count(DetectionObjectInfoBean.class);
        if (rowCount == 0) {
            DetectionObjectInfoBean deteObjectInfoBean= new DetectionObjectInfoBean();
            deteObjectInfoBean.setDetectionName("钢管");
            deteObjectInfoBean.setMagneticGradient(50);
            deteObjectInfoBean.setStressValue(1000);
            deteObjectInfoBean.setDefectLevel(0);
            deteObjectInfoBean.setMaterial("Q235");
            deteObjectInfoBean.setModel("18管");
            deteObjectInfoBean.setNote("");
            deteObjectInfoBean.save();
        }
    }

    /*****************************************************************
     * 方法名  :  insert
     * 输入参数：DetectionObjectInfoBean:需要插入的DetectionObjectInfoBean对象的引用
     * 输出参数：无
     * 功能描述: 将HistoryDataRecordDetailBean对象存入数据库
     ******************************************************************/
    public void insert(DetectionObjectInfoBean detectionObjectInfoBean) {
        detectionObjectInfoBean.save();
    }

    /*****************************************************************
     * 方法名  :  delete
     * 输入参数：detectionId:需要删除的HistoryDataRecordDetailBean对象的ID
     * 输出参数：无
     * 功能描述:  删除系统信息
     * 修改历史：
     ******************************************************************/
    public void delete(long id) {
        DataSupport.deleteAll(DetectionObjectInfoBean.class, "id == ?", String.valueOf(id));
    }

    /*****************************************************************
     * 方法名  :  update
     * 输入参数：无
     * 输出参数：无
     * 功能描述:   更新系统信息
     * 修改历史：
     ******************************************************************/
    public void update(DetectionObjectInfoBean bean) {
        bean.update(bean.getId());
    }

    /*****************************************************************
     * 方法名  :  query
     * 输入参数：detectionId:需要查询的记录的ID
     * 输出参数：DetectionObjectInfoBean对象
     * 功能描述:  通过检测对象的ID得到检测对象的梯度值
     * 修改历史：
     ******************************************************************/
    public float query(int detectionId){
        //如果需要查询的数据量很大，建议使用事务
        List<DetectionObjectInfoBean> detectBeans = DataSupport
                .where("id == ?", String.valueOf(detectionId))
                .find(DetectionObjectInfoBean.class);
        return detectBeans.get(0).getMagneticGradient();
    }
}