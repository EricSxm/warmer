package com.sxm.warmer.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TempList {
    public String type;
    public String temp;

    private static File bmsTemp;

    public static List<TempList> getTempList() {
        File thermal = new File("/sys/class/thermal/");
        ArrayList<TempList> tempLists = new ArrayList<>();
        for (int i = 0; new File(thermal, "thermal_zone" + i).canExecute(); i++) {
            File type = new File(thermal, "thermal_zone" + i + "/type/");
            File temp = new File(thermal, "thermal_zone" + i + "/temp/");
            try {
                FileInputStream typeStream = new FileInputStream(type);
                byte[] typeBytes = new byte[(int) type.length()];
                int end = typeStream.read(typeBytes);
                TempList tempList = new TempList();
                tempList.type = new String(typeBytes, 0, end - 1);
                FileInputStream tempStream = new FileInputStream(temp);
                byte[] tempBytes = new byte[6];
                tempStream.read(tempBytes);
                tempList.temp = String.format("%.1f", Float.parseFloat(new String(tempBytes)) / 1000) + "℃";
                tempLists.add(tempList);
                typeStream.close();
                if ("bms".equals(tempList.type) || "battery".equals(tempList.type)) {
                    bmsTemp = temp;
                }
            } catch (IOException e) {
                Log.w(temp+"", "文件读取出错!");
//                e.printStackTrace();
            }
        }
        return tempLists;
    }

    public static File getBmsTemp() {
        return bmsTemp;
    }
}
