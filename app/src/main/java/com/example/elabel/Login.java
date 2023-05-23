package com.example.elabel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentActivity;
import com.example.elabel.FragmentActivity;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import data.GlobalData;
import data.User;

public class Login extends AppCompatActivity {

    private Button btnLogin;
    private TextView edtAccount, edtPassword;
    private List<User> users;
    private GlobalData globalData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        globalData = (GlobalData) getApplicationContext(); // 獲取 GlobalData 的實例


        edtAccount = findViewById(R.id.edtAccount);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(onClickListener);

        users = new ArrayList<User>(); // 初始化使用者清單

        requestWriteExternalStorage();//檢查裝置儲存裝置權限
        openCSV();//讀取csv檔案

    }

    private void openCSV() {
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("dir", dir.getAbsolutePath());
            CSVReader reader = new CSVReader(new FileReader(dir.getAbsolutePath() + "/user.csv"));
            users.clear();

            String[] record;
            while ((record = reader.readNext()) != null) {
                User user = new User();
                user.setUserID(record[0]);
                user.setUserName(record[1]);
                user.setPassword(record[2]);
                user.setUGroupID(record[3]);
                user.setSetUserID(record[4]);
                user.setSetTime(record[5]);
                user.setStartDate(record[6]);
                user.setEndDate(record[7]);
                user.setStartTime(record[8]);
                user.setEndTime(record[9]);
                user.setUpdated(record[10]);
                users.add(user);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void requestWriteExternalStorage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            boolean permissionGranted = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

            if(!permissionGranted){
                requestPermissions(new String[]{permission},1000);
            }
        }
    }

    private String loginCheck(String account, String password) {
        boolean accountExists = users.stream()
                .anyMatch(user -> user.getUserID().equals(account));

        if (!accountExists) {
            return "無此使用者!!" + account + "不存在";
        }

        boolean passwordMatches = users.stream()
                .anyMatch(user -> user.getUserID().equals(account) && user.getPassword().equals(password));

        if (!passwordMatches) {
            return "密碼錯誤!! " + globalData.getLoginUserID() + " " + globalData.getLoginUserName();
        }

        Optional<User> loginUser = users.stream()
                .filter(user -> user.getUserID().equals(account) && user.getPassword().equals(password))
                .findFirst();

        globalData.setLoginUserID(loginUser.get().getUserID());
        globalData.setLoginUserName(loginUser.get().getUserName());
        return "";
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v.getContext());

            String account = edtAccount.getText().toString();
            String password = edtPassword.getText().toString();
            String loginResult = loginCheck(account, password);

            if (loginResult.isEmpty()) {
                String successMessage = globalData.getLoginUserID() + " " + globalData.getLoginUserName() + " 登入成功";
                Toast.makeText(Login.this, successMessage, Toast.LENGTH_LONG).show();
                startActivity(new Intent(v.getContext(), FragmentActivity.class));


            } else {
                Toast.makeText(Login.this, loginResult, Toast.LENGTH_LONG).show();
            }
        }
    };




    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
