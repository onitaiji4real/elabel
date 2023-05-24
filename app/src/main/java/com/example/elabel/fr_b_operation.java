package com.example.elabel;

import com.example.elabel.Login;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import data.Drugstore;
import data.GlobalData;
import data.Inventory;
import data.User;

public class fr_b_operation extends Fragment {

    EditText labelCode,drugArea,drugCode,drugName,stockQTY;
    Button btnConfirm;
    GlobalData globalData;
    Login login = new Login();
    List<Drugstore>Drugstores;
    List<Inventory>Inventorys;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // 在這裡載入並返回該 Fragment 的佈局文件
        View view = inflater.inflate(R.layout.fr_b_operation, container, false);
        // 執行其他佈局相關的初始化操作
        return view;
    }

    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    public void openCSV_ReadDrugStore(){
        try {
            File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("dir", dir.getAbsolutePath());
            CSVReader reader = new CSVReader(new FileReader(dir.getAbsolutePath() + "/drugstore.csv"));
           String[] nextLine;
           int i = 0;
           String[] record = null;
            while ((record = reader.readNext()) != null) {
                Drugstore drugstore = new Drugstore();
                drugstore.setStoreID(record[0]);
                Log.d("drugstore",record[6]);
                drugstore.setAreaNo(record[1]);
                drugstore.setBlockNo(record[2]);
                drugstore.setBlockType(record[3]);
                drugstore.setDrugCode(record[4]);
                drugstore.setMakerID(record[5]);
                drugstore.setElabelNumber(record[6]);
                drugstore.setTemPt_Kind(record[7]);
                drugstore.setSafeStock(record[8]);
                drugstore.setTotalQty(record[9]);
                drugstore.setSetTime(record[10]);
                drugstore.setSetUserID(record[11]);
                drugstore.setInvQtyTime(record[12]);
                drugstore.setInvQtyUserID(record[13]);
                drugstore.setLotNumber(record[14]);
                drugstore.setEffectDate(record[15]);
                drugstore.setStockQty(record[16]);
                drugstore.setUpdateUserID(record[17]);
                drugstore.setUpdateTime(record[18]);
                Drugstores.add(drugstore);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void openCSV_ReadInventory(){
        try {
            File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("dir", dir.getAbsolutePath());
            CSVReader reader = new CSVReader(new FileReader(dir.getAbsolutePath() + "/inventory.csv"));
            String[] nextline;

            int i = 0;
            String[] record = null;
            while ((record = reader.readNext()) != null) {
                Inventory inventory = new Inventory();
                inventory .setInvDate(record[0]);
                inventory.setDrugCode(record[1]);
                inventory.setMakerID(record[2]);
                inventory.setStoreID(record[3]);
                inventory.setAreaNo(record[4]);
                inventory.setBlockNo(record[5]);
                inventory.setBlockType(record[6]);
                inventory.setLotNumber(record[7]);
                inventory.setStockQty(record[8]);
                inventory.setInventoryQty(record[9]);
                inventory.setAdjQty(record[10]);
                inventory.setShiftNo(record[11]);
                inventory.setInvTime(record[12]);
                inventory.setUserID(record[13]);
                inventory.setRemark(record[14]);

                Inventorys.add(inventory);
            }
            reader.close();
        }  catch (IOException | CsvValidationException e) {
            // 處理 I/O 相關異常或 CSV 驗證異常的程式碼
            e.printStackTrace();
        }
    }


    public static void  hideKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null){
            imm.hideSoftInputFromWindow(((Activity)context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}