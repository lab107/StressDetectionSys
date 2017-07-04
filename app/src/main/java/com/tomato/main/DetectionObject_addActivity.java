package com.tomato.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.tools.dataBase.Bean.DetectionObjectInfoBean;
import com.tomato.tools.dataBase.Dao.DetectionObjectInfoDao;


public class DetectionObject_addActivity extends Activity implements View.OnClickListener{

    private EditText input_name;
    private EditText input_material;
    private EditText input_model;
    private TextView input_defect_level;
    private EditText input_magnetic_gradient;
    private EditText input_stress_value;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detection_object_add);

        input_name = (EditText) findViewById(R.id.name);
        input_material = (EditText) findViewById(R.id.material);
        input_model = (EditText) findViewById(R.id.model);
        input_defect_level = (TextView) findViewById(R.id.defectLevel);
        input_magnetic_gradient = (EditText) findViewById(R.id.magneticGradient);
        input_stress_value = (EditText) findViewById(R.id.stressValue);

        TextView finish = (TextView) findViewById(R.id.btn_finish);
        finish.setOnClickListener(this);
        ImageView returnKey = (ImageView) findViewById(R.id.return_key);
        returnKey.setOnClickListener(this);
        LinearLayout select = (LinearLayout) findViewById(R.id.select_defectLevel);
        select.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish://完成
                if (input_name.getText().length() == 0
                        || input_material.getText().length() == 0
                        || input_model.getText().length() == 0
                        || input_defect_level.getText().length() == 0
                        || input_magnetic_gradient.getText().length() == 0
                        || input_stress_value.getText().length() == 0) {
                    Toast.makeText(this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
                    break;
                }

                DetectionObjectInfoBean detectionObjectInfoBean = new DetectionObjectInfoBean();//新建检测对象类
                detectionObjectInfoBean.setDetectionName(input_name.getText().toString());//名字
                detectionObjectInfoBean.setMaterial(input_material.getText().toString());//材质
                detectionObjectInfoBean.setModel(input_model.getText().toString());//型号
                if (input_defect_level.getText().equals("一般"))
                    detectionObjectInfoBean.setDefectLevel(1);
                else if (input_defect_level.getText().equals("严重"))
                    detectionObjectInfoBean.setDefectLevel(2);
                else
                    detectionObjectInfoBean.setDefectLevel(3);
                detectionObjectInfoBean.setMagneticGradient(
                        Float.parseFloat(input_magnetic_gradient.getText().toString()));//梯度值
                detectionObjectInfoBean.setStressValue(Float.parseFloat(
                        input_stress_value.getText().toString()));//应力值

                DetectionObjectInfoDao detectionObjectInfoDao = DetectionObjectInfoDao.getInstance();
                detectionObjectInfoDao.insert(detectionObjectInfoBean);//插入数据库

                finish();
                break;
            case R.id.select_defectLevel://选择缺陷等级
                final ImageView select = (ImageView) findViewById(R.id.img_select);
                select.setImageResource(R.drawable.strigon);
                dialog = new Dialog(this, R.style.common_dialog);
                //对话框布局
                View inflate = LayoutInflater.from(this).inflate(R.layout.select_defectlevel, null);
                //初始化控件
                RadioButton normal = (RadioButton) inflate.findViewById(R.id.rbt_normal);
                RadioButton bad = (RadioButton) inflate.findViewById(R.id.rbt_bad);
                RadioButton terrible = (RadioButton) inflate.findViewById(R.id.rbt_terrible);
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
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        select.setImageResource(R.drawable.strigon1);
                    }
                });
                normal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input_defect_level.setText("一般");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                        select.setImageResource(R.drawable.strigon1);
                    }
                });
                bad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input_defect_level.setText("严重");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                        select.setImageResource(R.drawable.strigon1);
                    }
                });
                terrible.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input_defect_level.setText("特别严重");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 500);
                        select.setImageResource(R.drawable.strigon1);
                    }
                });
                break;
            case R.id.return_key://返回
                finish();
        }
    }
}
