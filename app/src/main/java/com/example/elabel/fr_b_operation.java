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
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.Druginfo;
import data.Drugstore;
import data.GlobalData;
import data.Inventory;
import data.User;

public class fr_b_operation extends Fragment {


    TextView txtInventoryQty,totalQty;
    EditText labelCode, drugArea, drugCode, drugName, stockQTY,stockBox,stockLine,stockNumPill;
    Button btnConfirm;
    GlobalData globalData;
    Login login = new Login();

    List<Drugstore> Drugstores;
    List<Inventory> Inventorys;
    List<Druginfo> Druginfos;





    String ElabelCode,StoreID, AreaNo, BlockNo, BlockType,
            DrugCode, MakerID, TemPt_kind,
            SafeStock, TotalQty, SetTime, SetUserID,
            InvQtyTime, InvQtyUserID, LotNumber, EffectDate,
            StockQty, UpdateUserID, UpdateTime;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 在這裡載入並返回該 Fragment 的佈局文件
        View view = inflater.inflate(R.layout.fr_b_operation, container, false);

        globalData = (GlobalData) getActivity().getApplicationContext();
        labelCode = view.findViewById(R.id.labelCode);

        drugArea = view.findViewById(R.id.drugArea);//儲位
        drugCode = view.findViewById(R.id.drugCode);//儲位代碼
        drugName = view.findViewById(R.id.drugName);//藥物名稱

        stockBox = view.findViewById(R.id.stockBox);//盒
        stockLine = view.findViewById(R.id.stockLine);//排
        stockNumPill = view.findViewById(R.id.stockNumPill);//顆

        txtInventoryQty = view.findViewById(R.id.txtInventoryQty);//盤點數
        totalQty = view.findViewById(R.id.totalQty);//總數

        labelCode.requestFocus();

        Drugstores = new ArrayList<>();
        openCSV_ReadDrugStore();

        Inventorys = new ArrayList<>();
        openCSV_ReadInventory();

        Druginfos = new ArrayList<>();
        openCSV_ReadDrugInfo();

        String InventoryNum = String.valueOf(Inventorys.size());
        String DrugStore = String.valueOf(Drugstores.size());
        String ShowNum = " "+InventoryNum + " / " + DrugStore;
        txtInventoryQty.setText(ShowNum);
        totalQty.setText("6000");

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void openCSV_ReadDrugStore() {
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
                Log.d("drugstore", record[6]);
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
        } catch (IOException | CsvValidationException e) {
            // 處理 I/O 相關異常或 CSV 驗證異常的程式碼
            e.printStackTrace();
        }
    }

    public void openCSV_ReadInventory() {
        try {
            File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("dir", dir.getAbsolutePath());
            CSVReader reader = new CSVReader(new FileReader(dir.getAbsolutePath() + "/inventory.csv"));
            String[] nextline;

            int i = 0;
            String[] record = null;
            while ((record = reader.readNext()) != null) {
                Inventory inventory = new Inventory();
                inventory.setInvDate(record[0]);
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
        } catch (IOException | CsvValidationException e) {
            // 處理 I/O 相關異常或 CSV 驗證異常的程式碼
            e.printStackTrace();
        }
    }

    public void openCSV_ReadDrugInfo() {
        try {
            File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            Log.d("dir", dir.getAbsolutePath());
            CSVReader reader = new CSVReader(new FileReader(dir.getAbsolutePath() + "/druginfo.csv"));
            String[] nextLine;
            int i = 0;
            String[] record = null;
            while ((record = reader.readNext()) != null) {
                Druginfo druginfo = new Druginfo();
                druginfo.setMakerID(record[0]);
                druginfo.setDrugCode(record[1]);
                druginfo.setDrugEnglish(record[2]);
                druginfo.setDrugName(record[3]);
                druginfo.setDrugLabel(record[10]);
                Druginfos.add(druginfo);
            }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            // 處理 I/O 相關異常或 CSV 驗證異常的程式碼
            e.printStackTrace();
        }
    }

    public static String toCSV(String[] array) {

        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s.trim()).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;

    }

    private void exportDataToCSV() throws IOException {
        String csvData = "";
        int count = 0;

        for (Inventory inventory : Inventorys) {
            if (StoreID.equals(inventory.getStoreID()) &&
                    AreaNo.equals(inventory.getAreaNo()) &&
                    BlockNo.equals(inventory.getBlockNo()) &&
                    BlockType.equals(inventory.getBlockType())) {

                count++;
                Log.d("count", String.valueOf(count));

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                String date = sdfDate.format(Calendar.getInstance().getTime());
                String time = sdfTime.format(Calendar.getInstance().getTime());

                int inStockQty = Integer.parseInt(StockQty);
                int inventoryQty = Integer.parseInt(txtInventoryQty.getText().toString());
                int adjQty = inventoryQty - inStockQty;

                inventory.setInventoryQty(String.valueOf(inventoryQty));
                inventory.setInvDate(date);
                inventory.setInvTime(time);
                inventory.setUserID(globalData.getLoginUserID());
            }
        }
    }

    public void getRequestWithHeaderAndBody(){}                       //原先小柏宇未使用到該function

    private View.OnClickListener onGetElabelArticle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String ElabelCode = labelCode.getText().toString();
            String url = "AIMS 網址 未完成"+ElabelCode;//未完成
        }
    };            //原先小柏宇未使用到該function

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v.getContext());
            try {
                exportDataToCSV();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener onLight = new View.OnClickListener() {//原先小柏宇未使用到該function 未完成
        @Override
        public void onClick(View v) {

        }
    };

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
