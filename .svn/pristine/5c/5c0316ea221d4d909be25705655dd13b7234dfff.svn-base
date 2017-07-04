package com.tomato.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tomato.main.spinner.TableAdapter;
import com.tomato.tools.Parameter.SystemParameter;
import com.tomato.tools.dataBase.Bean.DetectionObjectInfoBean;
import com.tomato.tools.dataBase.Bean.HistoryDataRecordBean;
import com.tomato.tools.dataBase.Bean.SysInfoBean;
import com.tomato.tools.dataBase.Dao.DetectionObjectInfoDao;
import com.tomato.tools.dataBase.Dao.HistoryDataRecordDao;
import com.tomato.tools.dataBase.Dao.SysInfoDao;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

/**
 * @author liuhdme
 * 检测对象页面
 */
public class DetectionObjectActivity extends Activity implements View.OnClickListener,TableAdapter.MyClickListener{
    private ImageView finish;

    private ArrayList<Map<String, Object>> list;//数据
    private TableAdapter adapter;//适配器
    ListView tableListView;//布局

    private int position;
    private long id;
    private boolean selected;// 判断是否有 RadioButton 被选中
    private boolean whetherFirst = true; // 判断是否是第一次进入选择对象界面
    List<DetectionObjectInfoBean> detectionList = DataSupport.findAll(DetectionObjectInfoBean.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detection_object);

        initList();

        Button choose;
        Button revise;
        Button delete;
        TextView add;

        choose = (Button) findViewById(R.id.btn_choose);
        revise = (Button) findViewById(R.id.btn_revise);
        delete = (Button) findViewById(R.id.btn_delete);
        add = (TextView) findViewById(R.id.tv_add);

        choose.setOnClickListener(this);
        revise.setOnClickListener(this);
        delete.setOnClickListener(this);
        add.setOnClickListener(this);

        initView();
        initEvent();
    }

    public void initView(){
        finish=(ImageView)findViewById(R.id.return_key);
    }
    public void initEvent(){
        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = detectionList.get(0).getId();
                SysInfoBean sysInfoBean = new SysInfoBean();
                SysInfoDao sysInfoDao = SysInfoDao.getInstance();
                sysInfoBean.setDetectionID((int)id);
                sysInfoDao.update(sysInfoBean);
                SystemParameter systemParameter = SystemParameter.getInstance();
                systemParameter.nDetectionObjectID = (int)id;
                finish();
            }
        });
    }

    private void initList() {
        detectionList = DataSupport.findAll(DetectionObjectInfoBean.class);
        list = new ArrayList<>();
        for (int i =0; i<detectionList.size(); i++ ) {
            String defectLevel;
            Map<String, Object> map = new HashMap<>();
            map.put("radioButton", "");
            map.put("name", detectionList.get(i).getDetectionName());
            map.put("material", detectionList.get(i).getMaterial());
            map.put("model", detectionList.get(i).getModel());
            if (detectionList.get(i).getDefectLevel() == 1)
                defectLevel = "一般";
            else if (detectionList.get(i).getDefectLevel() == 2)
                defectLevel = "严重";
            else
                defectLevel = "特别严重";
            map.put("defectLevel", defectLevel);
            map.put("magneticGradient", "" + detectionList.get(i).getMagneticGradient());
            map.put("stressValue", "" + detectionList.get(i).getStressValue());
            //添加一行数据
            list.add(map);
        }

        tableListView = (ListView) findViewById(R.id.list);
        adapter = new TableAdapter(this, list, this);
        // 如果是第一次进入检测对象界面
        if (whetherFirst) {
            List<SysInfoBean> sysInfoBeanList = DataSupport.findAll(SysInfoBean.class);
            boolean has = false;
            // 每次显示 ListView 前，先找到状态为"选取"的 item，并将其 RadioButton 设为选中状态
            for (int i = 0; i < detectionList.size(); i++) {
                if ((int) detectionList.get(i).getId() == sysInfoBeanList.get(0).getDetectionID()) {
                    adapter.clearStates(i);
                    adapter.changeSelector(i);
                    has = true;
                }
            }
            if (!has) {
                adapter.clearStates(0);
                adapter.changeSelector(0);
            }
        } else {   // 如果是由增加或修改界面返回
            adapter.clearStates(position);
            adapter.changeSelector(position);
        }
        tableListView.setAdapter(adapter);

        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.text_radioButton);
                adapter.clearStates(i);
                radioButton.setChecked(adapter.getStates(i));
                adapter.changeSelector(i);
                position = i;
                id = detectionList.get(position).getId();
                selected = true;
            }
        });

        // 判断是否有 RadioButton 被选中，若有，则 selected 为true
        for (int i=0; i<detectionList.size(); i++) {
            if (!adapter.getStates(i))
                selected = false;
            else {
                selected = true;
                id = detectionList.get(i).getId();
                position = i;
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose:
                final Dialog selectSuccessfully = new Dialog(this, R.style.select_successfully);
                if (selected) {
                    selectSuccessfully.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            selectSuccessfully.dismiss();
                        }
                    }, 500);
                    SysInfoBean sysInfoBean = new SysInfoBean();
                    SysInfoDao sysInfoDao = SysInfoDao.getInstance();
                    sysInfoBean.setDetectionID((int)id);
                    sysInfoDao.update(sysInfoBean);
                    SystemParameter systemParameter = SystemParameter.getInstance();
                    systemParameter.nDetectionObjectID = (int)id;

                    selectSuccessfully.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Intent intent = new Intent(DetectionObjectActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(this, "请先选择！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_revise:
                if (selected) {
                    Intent intent_revise = new Intent(this, DetectionObject_reviseActivity.class);
                    intent_revise.putExtra("position", position);
                    intent_revise.putExtra("id", id);
                    startActivityForResult(intent_revise, 1);
                } else {
                    Toast.makeText(this, "请先选择！", Toast.LENGTH_SHORT).show();
                }
                selected = false;
                break;
            case R.id.btn_delete:
                if (selected) {
                    if (DataSupport.count(DetectionObjectInfoBean.class) == 1) {
                        Toast.makeText(DetectionObjectActivity.this, "最少要有一个检测对象", Toast.LENGTH_SHORT).show();
                    } else {
                        List<HistoryDataRecordBean> historyDataRecordBeanList = DataSupport.
                                where("detectionID = ?", String.valueOf(id)).
                                find(HistoryDataRecordBean.class);
                        if (historyDataRecordBeanList.size() == 0) {
                            // 如果没被测过
                            final Dialog confirmToDelete = new Dialog(this, R.style.common_dialog);
                            //对话框布局
                            View inflate1 = LayoutInflater.from(this).inflate(R.layout.confirm_to_delete, null);
                            //初始化控件
                            TextView deleteN = (TextView) inflate1.findViewById(R.id.delete_n);
                            TextView deleteY = (TextView) inflate1.findViewById(R.id.delete_y);
                            //将布局设置给 dialog
                            confirmToDelete.setContentView(inflate1);
                            confirmToDelete.show();
                            deleteN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    confirmToDelete.dismiss();
                                }
                            });
                            deleteY.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SysInfoBean sysInfoBean = new SysInfoBean();
                                    sysInfoBean.setDetectionID(-1);
                                    sysInfoBean.update(1);
                                    DetectionObjectInfoDao detectionObjectInfoDao = DetectionObjectInfoDao.getInstance();
                                    detectionObjectInfoDao.delete(id);
                                    initList();
                                    confirmToDelete.dismiss();
                                }
                            });
                            adapter.clearStates(-1);
                        } else {
                            Toast.makeText(DetectionObjectActivity.this, "该检测对象已被检测，不可删除", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "请先选择！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_add:
                Intent intent_add = new Intent(this, DetectionObject_addActivity.class);
                startActivity(intent_add);
                break;
        }
    }

    @Override
    public void clickListener(View v) {
        position = (Integer) v.getTag();
        id = detectionList.get(position).getId();
        selected = true;
    }

    //由另一个页面返回之后刷新 ListView
    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                whetherFirst = data.getBooleanExtra("second", false);
                position = data.getIntExtra("position", 0);
                id = data.getLongExtra("id", 0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        id = detectionList.get(0).getId();
        SysInfoBean sysInfoBean = new SysInfoBean();
        SysInfoDao sysInfoDao = SysInfoDao.getInstance();
        sysInfoBean.setDetectionID((int)id);
        sysInfoDao.update(sysInfoBean);
        SystemParameter systemParameter = SystemParameter.getInstance();
        systemParameter.nDetectionObjectID = (int)id;
        finish();
        return true;
    }
}