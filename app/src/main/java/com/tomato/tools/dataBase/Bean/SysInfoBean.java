package com.tomato.tools.dataBase.Bean;

import org.litepal.crud.DataSupport;

/**
 *  系统信息
 */
public class SysInfoBean extends DataSupport {
    private long id;// 系统ID
    private int deviceID;// 设备ID
    private String deviceName;// 设备名称
    private int channelCount;// 通道数量
    private int channelWeight;// 通道分量
    private int channelDistance;// 通道间距
    private int stepInterval;   //横向梯度步数间隔
    private double stepDistance;// 步长
    private int detectionID;// 检测对象ID
    private String note;// 备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getChannelWeight() {
        return channelWeight;
    }

    public void setChannelWeight(int channelWeight) {
        this.channelWeight = channelWeight;
    }

    public int getChannelDistance() {
        return channelDistance;
    }

    public void setChannelDistance(int channelDistance) {
        this.channelDistance = channelDistance;
    }

    public int getStepInterval() {
        return stepInterval;
    }

    public void setStepInterval(int stepInterval) {
        this.stepInterval = stepInterval;
    }

    public double getStepDistance() {
        return stepDistance;
    }

    public void setStepDistance(double stepDistance) {
        this.stepDistance = stepDistance;
    }

    public int getDetectionID() {
        return detectionID;
    }

    public void setDetectionID(int detectionID) {
        this.detectionID = detectionID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
