package com.tomato.main.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import com.tomato.main.MainActivity;
import com.tomato.main.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zch22 on 2017/5/23.
 */
public class CopyDBToSDCard {

    private String DATABASE_NAME = "tomato_leak_android";
    private String oldPath = "/data/data/com.tomato/databases/" + DATABASE_NAME+".db";//注意这里包名是com.tomato不是com.tomato.main
    public String newPath = Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME+".db";//复制后路径

    public void copyDBToSDcrad()throws Exception {
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fouStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fouStream.write(buffer, 0, byteread);
                }
                inStream.close();
                fouStream.close();
            }
    }
}
