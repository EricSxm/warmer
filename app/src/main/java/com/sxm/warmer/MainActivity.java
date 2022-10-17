package com.sxm.warmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sxm.warmer.adapter.TempListAdapter;
import com.sxm.warmer.util.TempList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        displayTemperature();
    }

    private Button stl;
    private TempListAdapter tempListAdapter;

    public void initialize() {
        TempList.getTempList();
        stl = findViewById(R.id.stl);
        ListView tempList = findViewById(R.id.tempList);
        tempListAdapter = new TempListAdapter(MainActivity.this, R.layout.temp_list, new ArrayList<TempList>());
        tempList.setAdapter(tempListAdapter);
    }

    public void displayTemperature() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final TextView t1 = findViewById(R.id.t1);
                File file = TempList.getBmsTemp();
                try {
                    while (true) {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        final byte[] temp = new byte[(int) file.length()];
                        fileInputStream.read(temp);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t1.setText("手机当前温度：" + Float.parseFloat(new String(temp)) / 1000 + "℃");
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private volatile boolean isStart = false;

    public void start(View view) {
        TextView t2 = findViewById(R.id.t2);
        Button s = findViewById(R.id.s);
        if (isStart = !isStart) {
            for (int i = 0; new File("/sys/devices/system/cpu/cpu" + i).canExecute(); i++) {
                Log.i("start", i + "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isStart) ;
                    }
                }).start();
            }
            t2.setText("当前状态：正在加热");
            s.setText("停止加热");
        } else {
            t2.setText("当前状态：停止加热");
            s.setText("开始加热");
        }
    }

    private boolean isShow = false;
    private Thread loop;

    public void showTempList(View view) {
        if (isShow = !isShow) {
            stl.setText("隐藏温度列表");
            loop = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isShow) {
                            tempListAdapter.update(TempList.getTempList());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tempListAdapter.notifyDataSetChanged();
                                }
                            });
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            loop.start();
        } else {
            loop.interrupt();
            try {
                loop.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stl.setText("查看温度列表");
            tempListAdapter.update(new ArrayList<TempList>());
            tempListAdapter.notifyDataSetChanged();
        }
    }
}
