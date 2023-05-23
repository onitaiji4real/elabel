package com.example.elabel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import data.Drugstore;
import data.GlobalData;
//import okhttp3.MediaType;
public class FragmentActivity extends AppCompatActivity {



    Button btnLogOut;

    GlobalData globalData;
    List<Drugstore> Drugstores;
    //public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(onLogout);

    }

    private View.OnClickListener onLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            globalData.setLoginUserID("");
            globalData.setLoginUserName("");
            startActivity(new Intent(v.getContext(),Login.class));
        }
    };

}
