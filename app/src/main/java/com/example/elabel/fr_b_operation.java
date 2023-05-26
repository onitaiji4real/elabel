package com.example.elabel;

import com.example.elabel.Login;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.ToneGenerator;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    TextView txtInventoryQty, totalQty, drugArea, drugCode, drugName;
    EditText labelCode, stockQTY, stockBox, stockLine, stockNumPill;
    Button btnConfirm;
    GlobalData globalData;
    Login login = new Login();

    List<Drugstore> Drugstores;
    List<Inventory> Inventorys;
    List<Druginfo> Druginfos;

    String ElabelCode, StoreID, AreaNo, BlockNo, BlockType,
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
        btnConfirm = view.findViewById(R.id.btnConfirm);//確認按鈕



        stockBox = view.findViewById(R.id.stockBox);//盒
        stockLine = view.findViewById(R.id.stockLine);//排
        stockNumPill = view.findViewById(R.id.stockNumPill);//顆

        btnConfirm.setOnClickListener(onConfirm);



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
        String ShowNum = " " + InventoryNum + " / " + DrugStore;
        txtInventoryQty.setText(ShowNum);

        labelAfterScanListener();


        return view;
    }



    private View.OnClickListener onConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v.getContext());
            try {
                exportDataToCSV();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void labelAfterScanListener() {
        labelCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }//以上不用

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }//以上不用

            @Override
            public void afterTextChanged(Editable s) {

                String input = s.toString();

                if (input.length() == 12) {
                    lsDrugInfo();
                }
            }
        });
    }

    private void lsDrugInfo() {

        //ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, ToneGenerator.MAX_VOLUME);
        String elabelCode = labelCode.getText().toString();

        Drugstore target_DrugStore = null;
        Druginfo target_DrugInfo = null;

        for (Drugstore drugstore : Drugstores) {
            if (drugstore.getElabelNumber().equals(elabelCode)) {
                target_DrugStore = drugstore;
                break;
            }
        }

        for (Druginfo druginfo : Druginfos) {
            if (druginfo.getDrugCode().equals(target_DrugStore.getDrugCode())) {
                target_DrugInfo = druginfo;
                break;
            }
        }

        String drugAreaSetText = target_DrugStore.getStoreID() + " - " + target_DrugStore.getAreaNo() + " - " + target_DrugStore.getBlockNo() + " - " + target_DrugStore.getBlockType();
        drugArea.setText(drugAreaSetText);
        drugCode.setText(target_DrugStore.getDrugCode());
        drugName.setText(target_DrugInfo.getDrugName());
        totalQty.setText(target_DrugStore.getStockQty());

        Toast.makeText(getActivity(), "掃描成功", Toast.LENGTH_SHORT).show();
        //


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
                druginfo.setDrugName(record[2]);
                druginfo.setDrugEnglish(record[3]);
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

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        Calendar c = Calendar.getInstance();
        String date = sdfDate.format(c.getTime());
        String time = sdfTime.format(c.getTime());

        // Create an inventory with simple data
        Inventory i = new Inventory();
        i.setInvDate(date);
        i.setDrugCode(drugCode.getText().toString());
        i.setMakerID("ok");
        i.setStoreID(drugArea.getText().toString());
        i.setAreaNo("0");
        i.setBlockNo("0");
        i.setBlockType("0");
        i.setLotNumber("0");
        i.setStockQty(stockNumPill.getText().toString());
        i.setInventoryQty("0");
        i.setAdjQty("0");
        i.setShiftNo(time);
        i.setInvTime(globalData.getLoginUserID());
//        i.setUserID(globalData.getLoginUserID());
        i.setRemark("15");

        // Add simpleInventory to the Inventorys list
        Inventorys.add(i);

        for (Inventory inv : Inventorys) {
            String currentLine = inv.getRowString();
            String[] cells = currentLine.split(",");
            csvData += toCSV(cells) + "\n";
        }

        File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String uniqueFileName = "/inventory.csv";
        File file = new File(directory.getAbsolutePath(), uniqueFileName);

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(csvData);
        fileWriter.flush();
        fileWriter.close();

        Toast.makeText(getActivity(), "File Exported Successfully", Toast.LENGTH_SHORT).show();
    }

    public void getRequestWithHeaderAndBody() {
    }                       //原先小柏宇未使用到該function

    private View.OnClickListener onGetElabelArticle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String ElabelCode = labelCode.getText().toString();
            String url = "AIMS 網址 未完成" + ElabelCode;//未完成
        }
    };            //原先小柏宇未使用到該function

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v.getContext());
            try {
                exportDataToCSV();
            } catch (IOException e) {
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
