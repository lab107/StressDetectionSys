package com.tomato.tools.dataBase.Bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by liuhdme on 2017/5/6.
 */

/**
 * 通道校准记录
 */
public class ChannelDetectionRecordBean extends DataSupport {
    private long id;// 编号
    private String detectionTime;//检测时间
    private int status; //状态（0再用，1历史，2删除）
    private int detectionWay;//校准方式（0标准磁场校准，1地磁场校准）
    private double zeroChannel1 = 0;
    private double zeroChannel2 = 0;
    private double zeroChannel3 = 0;
    private double zeroChannel4 = 0;
    private double zeroChannel5 = 0;
    private double zeroChannel6 = 0;
    private double zeroChannel7 = 0;
    private double zeroChannel8 = 0;
    private double zeroChannel9 = 0;
    private double zeroChannel10 = 0;
    private double zeroChannel11 = 0;
    private double zeroChannel12 = 0;
    private double zeroChannel13 = 0;
    private double zeroChannel14 = 0;
    private double zeroChannel15 = 0;
    private double zeroChannel16 = 0;
    private double zeroChannel17 = 0;
    private double zeroChannel18 = 0;
    private double zeroChannel19 = 0;
    private double zeroChannel20 = 0;
    private double zeroChannel21 = 0;
    private double zeroChannel22 = 0;
    private double zeroChannel23 = 0;
    private double zeroChannel24 = 0;
    private double zeroChannel25 = 0;
    private double zeroChannel26 = 0;
    private double zeroChannel27 = 0;
    private double zeroChannel28 = 0;
    private double zeroChannel29 = 0;
    private double zeroChannel30 = 0;
    private double kChannel1 = 1;
    private double kChannel2 = 1;
    private double kChannel3 = 1;
    private double kChannel4 = 1;
    private double kChannel5 = 1;
    private double kChannel6 = 1;
    private double kChannel7 = 1;
    private double kChannel8 = 1;
    private double kChannel9 = 1;
    private double kChannel10 = 1;
    private double kChannel11 = 1;
    private double kChannel12 = 1;
    private double kChannel13 = 1;
    private double kChannel14 = 1;
    private double kChannel15 = 1;
    private double kChannel16 = 1;
    private double kChannel17 = 1;
    private double kChannel18 = 1;
    private double kChannel19 = 1;
    private double kChannel20 = 1;
    private double kChannel21 = 1;
    private double kChannel22 = 1;
    private double kChannel23 = 1;
    private double kChannel24 = 1;
    private double kChannel25 = 1;
    private double kChannel26 = 1;
    private double kChannel27 = 1;
    private double kChannel28 = 1;
    private double kChannel29 = 1;
    private double kChannel30 = 1;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(String detectionTime) {
        this.detectionTime = detectionTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDetectionWay() {
        return detectionWay;
    }

    public void setDetectionWay(int detectionWay) {
        this.detectionWay = detectionWay;
    }

    public double getZeroChannel1() {
        return zeroChannel1;
    }

    public void setZeroChannel1(double zeroChannel1) {
        this.zeroChannel1 = zeroChannel1;
    }

    public double getZeroChannel2() {
        return zeroChannel2;
    }

    public void setZeroChannel2(double zeroChannel2) {
        this.zeroChannel2 = zeroChannel2;
    }

    public double getZeroChannel3() {
        return zeroChannel3;
    }

    public void setZeroChannel3(double zeroChannel3) {
        this.zeroChannel3 = zeroChannel3;
    }

    public double getZeroChannel4() {
        return zeroChannel4;
    }

    public void setZeroChannel4(double zeroChannel4) {
        this.zeroChannel4 = zeroChannel4;
    }

    public double getZeroChannel5() {
        return zeroChannel5;
    }

    public void setZeroChannel5(double zeroChannel5) {
        this.zeroChannel5 = zeroChannel5;
    }

    public double getZeroChannel6() {
        return zeroChannel6;
    }

    public void setZeroChannel6(double zeroChannel6) {
        this.zeroChannel6 = zeroChannel6;
    }

    public double getZeroChannel7() {
        return zeroChannel7;
    }

    public void setZeroChannel7(double zeroChannel7) {
        this.zeroChannel7 = zeroChannel7;
    }

    public double getZeroChannel8() {
        return zeroChannel8;
    }

    public void setZeroChannel8(double zeroChannel8) {
        this.zeroChannel8 = zeroChannel8;
    }

    public double getZeroChannel9() {
        return zeroChannel9;
    }

    public void setZeroChannel9(double zeroChannel9) {
        this.zeroChannel9 = zeroChannel9;
    }

    public double getZeroChannel10() {
        return zeroChannel10;
    }

    public void setZeroChannel10(double zeroChannel10) {
        this.zeroChannel10 = zeroChannel10;
    }

    public double getZeroChannel11() {
        return zeroChannel11;
    }

    public void setZeroChannel11(double zeroChannel11) {
        this.zeroChannel11 = zeroChannel11;
    }

    public double getZeroChannel12() {
        return zeroChannel12;
    }

    public void setZeroChannel12(double zeroChannel12) {
        this.zeroChannel12 = zeroChannel12;
    }

    public double getZeroChannel13() {
        return zeroChannel13;
    }

    public void setZeroChannel13(double zeroChannel13) {
        this.zeroChannel13 = zeroChannel13;
    }

    public double getZeroChannel14() {
        return zeroChannel14;
    }

    public void setZeroChannel14(double zeroChannel14) {
        this.zeroChannel14 = zeroChannel14;
    }

    public double getZeroChannel15() {
        return zeroChannel15;
    }

    public void setZeroChannel15(double zeroChannel15) {
        this.zeroChannel15 = zeroChannel15;
    }

    public double getZeroChannel16() {
        return zeroChannel16;
    }

    public void setZeroChannel16(double zeroChannel16) {
        this.zeroChannel16 = zeroChannel16;
    }

    public double getZeroChannel17() {
        return zeroChannel17;
    }

    public void setZeroChannel17(double zeroChannel17) {
        this.zeroChannel17 = zeroChannel17;
    }

    public double getZeroChannel18() {
        return zeroChannel18;
    }

    public void setZeroChannel18(double zeroChannel18) {
        this.zeroChannel18 = zeroChannel18;
    }

    public double getZeroChannel19() {
        return zeroChannel19;
    }

    public void setZeroChannel19(double zeroChannel19) {
        this.zeroChannel19 = zeroChannel19;
    }

    public double getZeroChannel20() {
        return zeroChannel20;
    }

    public void setZeroChannel20(double zeroChannel20) {
        this.zeroChannel20 = zeroChannel20;
    }

    public double getZeroChannel21() {
        return zeroChannel21;
    }

    public void setZeroChannel21(double zeroChannel21) {
        this.zeroChannel21 = zeroChannel21;
    }

    public double getZeroChannel22() {
        return zeroChannel22;
    }

    public void setZeroChannel22(double zeroChannel22) {
        this.zeroChannel22 = zeroChannel22;
    }

    public double getZeroChannel23() {
        return zeroChannel23;
    }

    public void setZeroChannel23(double zeroChannel23) {
        this.zeroChannel23 = zeroChannel23;
    }

    public double getZeroChannel24() {
        return zeroChannel24;
    }

    public void setZeroChannel24(double zeroChannel24) {
        this.zeroChannel24 = zeroChannel24;
    }

    public double getZeroChannel25() {
        return zeroChannel25;
    }

    public void setZeroChannel25(double zeroChannel25) {
        this.zeroChannel25 = zeroChannel25;
    }

    public double getZeroChannel26() {
        return zeroChannel26;
    }

    public void setZeroChannel26(double zeroChannel26) {
        this.zeroChannel26 = zeroChannel26;
    }

    public double getZeroChannel27() {
        return zeroChannel27;
    }

    public void setZeroChannel27(double zeroChannel27) {
        this.zeroChannel27 = zeroChannel27;
    }

    public double getZeroChannel28() {
        return zeroChannel28;
    }

    public void setZeroChannel28(double zeroChannel28) {
        this.zeroChannel28 = zeroChannel28;
    }

    public double getZeroChannel29() {
        return zeroChannel29;
    }

    public void setZeroChannel29(double zeroChannel29) {
        this.zeroChannel29 = zeroChannel29;
    }

    public double getZeroChannel30() {
        return zeroChannel30;
    }

    public void setZeroChannel30(double zeroChannel30) {
        this.zeroChannel30 = zeroChannel30;
    }

    public double getkChannel1() {
        return kChannel1;
    }

    public void setkChannel1(double kChannel1) {
        this.kChannel1 = kChannel1;
    }

    public double getkChannel2() {
        return kChannel2;
    }

    public void setkChannel2(double kChannel2) {
        this.kChannel2 = kChannel2;
    }

    public double getkChannel3() {
        return kChannel3;
    }

    public void setkChannel3(double kChannel3) {
        this.kChannel3 = kChannel3;
    }

    public double getkChannel4() {
        return kChannel4;
    }

    public void setkChannel4(double kChannel4) {
        this.kChannel4 = kChannel4;
    }

    public double getkChannel5() {
        return kChannel5;
    }

    public void setkChannel5(double kChannel5) {
        this.kChannel5 = kChannel5;
    }

    public double getkChannel6() {
        return kChannel6;
    }

    public void setkChannel6(double kChannel6) {
        this.kChannel6 = kChannel6;
    }

    public double getkChannel7() {
        return kChannel7;
    }

    public void setkChannel7(double kChannel7) {
        this.kChannel7 = kChannel7;
    }

    public double getkChannel8() {
        return kChannel8;
    }

    public void setkChannel8(double kChannel8) {
        this.kChannel8 = kChannel8;
    }

    public double getkChannel9() {
        return kChannel9;
    }

    public void setkChannel9(double kChannel9) {
        this.kChannel9 = kChannel9;
    }

    public double getkChannel10() {
        return kChannel10;
    }

    public void setkChannel10(double kChannel10) {
        this.kChannel10 = kChannel10;
    }

    public double getkChannel11() {
        return kChannel11;
    }

    public void setkChannel11(double kChannel11) {
        this.kChannel11 = kChannel11;
    }

    public double getkChannel12() {
        return kChannel12;
    }

    public void setkChannel12(double kChannel12) {
        this.kChannel12 = kChannel12;
    }

    public double getkChannel13() {
        return kChannel13;
    }

    public void setkChannel13(double kChannel13) {
        this.kChannel13 = kChannel13;
    }

    public double getkChannel14() {
        return kChannel14;
    }

    public void setkChannel14(double kChannel14) {
        this.kChannel14 = kChannel14;
    }

    public double getkChannel15() {
        return kChannel15;
    }

    public void setkChannel15(double kChannel15) {
        this.kChannel15 = kChannel15;
    }

    public double getkChannel16() {
        return kChannel16;
    }

    public void setkChannel16(double kChannel16) {
        this.kChannel16 = kChannel16;
    }

    public double getkChannel17() {
        return kChannel17;
    }

    public void setkChannel17(double kChannel17) {
        this.kChannel17 = kChannel17;
    }

    public double getkChannel18() {
        return kChannel18;
    }

    public void setkChannel18(double kChannel18) {
        this.kChannel18 = kChannel18;
    }

    public double getkChannel19() {
        return kChannel19;
    }

    public void setkChannel19(double kChannel19) {
        this.kChannel19 = kChannel19;
    }

    public double getkChannel20() {
        return kChannel20;
    }

    public void setkChannel20(double kChannel20) {
        this.kChannel20 = kChannel20;
    }

    public double getkChannel21() {
        return kChannel21;
    }

    public void setkChannel21(double kChannel21) {
        this.kChannel21 = kChannel21;
    }

    public double getkChannel22() {
        return kChannel22;
    }

    public void setkChannel22(double kChannel22) {
        this.kChannel22 = kChannel22;
    }

    public double getkChannel23() {
        return kChannel23;
    }

    public void setkChannel23(double kChannel23) {
        this.kChannel23 = kChannel23;
    }

    public double getkChannel24() {
        return kChannel24;
    }

    public void setkChannel24(double kChannel24) {
        this.kChannel24 = kChannel24;
    }

    public double getkChannel25() {
        return kChannel25;
    }

    public void setkChannel25(double kChannel25) {
        this.kChannel25 = kChannel25;
    }

    public double getkChannel26() {
        return kChannel26;
    }

    public void setkChannel26(double kChannel26) {
        this.kChannel26 = kChannel26;
    }

    public double getkChannel27() {
        return kChannel27;
    }

    public void setkChannel27(double kChannel27) {
        this.kChannel27 = kChannel27;
    }

    public double getkChannel28() {
        return kChannel28;
    }

    public void setkChannel28(double kChannel28) {
        this.kChannel28 = kChannel28;
    }

    public double getkChannel29() {
        return kChannel29;
    }

    public void setkChannel29(double kChannel29) {
        this.kChannel29 = kChannel29;
    }

    public double getkChannel30() {
        return kChannel30;
    }

    public void setkChannel30(double kChannel30) {
        this.kChannel30 = kChannel30;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
