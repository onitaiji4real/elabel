package com.example.elabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class fr_a_record extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // 在這裡載入並返回該 Fragment 的佈局文件
        View view = inflater.inflate(R.layout.fr_a_record, container, false);
        // 執行其他佈局相關的初始化操作
        return view;
    }
}
