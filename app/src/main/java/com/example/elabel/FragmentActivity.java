package com.example.elabel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import data.Drugstore;
import data.GlobalData;

public class FragmentActivity extends AppCompatActivity {

    Button btnLogOut;
    GlobalData globalData;
    List<Drugstore> Drugstores;
    BottomNavigationView bottomNavigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(onLogout);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_inventory_work);

        Fragment fragment = new fr_b_operation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            // 根據按鈕的 id 執行相應的切換 Fragment 的邏輯
            switch (item.getItemId()) {
                case R.id.navigation_inventory_record:
                    switchToFragment(new fr_a_record());
                    return true;
                case R.id.navigation_inventory_work:
                    switchToFragment(new fr_b_operation());
                    return true;
                case R.id.navigation_income_work:
                    switchToFragment(new fr_c_income());
                    return true;
                case R.id.navigation_expense_work:
                    switchToFragment(new fr_d_expenditure());
                    return true;
                case R.id.navigation_light_guide:
                    switchToFragment(new fr_e_blink());
                    return true;
            }
            return false;
        });



    }

    private View.OnClickListener onLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            globalData.setLoginUserID("");
            globalData.setLoginUserName("");
            startActivity(new Intent(v.getContext(), Login.class));
        }
    };

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
