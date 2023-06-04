package com.example.doanciclerk.gui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Customer_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Store_DTO;

public class CustomerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_ui);

        Customer_BLL customer_bll = new Customer_BLL(this);
        Customer_DTO thisCustomer = (Customer_DTO) getIntent().getSerializableExtra("Customer");

        EditText name = findViewById(R.id.txt_Ten);
        EditText address = findViewById(R.id.txt_DiaChi);
        TextView wallet = findViewById(R.id.txt_ViTien);

        name.setText(thisCustomer.getName());
        address.setText(thisCustomer.getAddress());

        String walletText = String.format("Wallet: %.0f", thisCustomer.getWallet());
        wallet.setText(walletText);

        ImageButton btnPlus = findViewById(R.id.btnPlus);

        btnPlus.setOnClickListener(v -> {
            String walletNum = wallet.getText().toString().split("\\:", 2)[1].trim();
            double num = Double.parseDouble(walletNum) + 1000;

            String t = String.format("Wallet: %.0f", num);
            wallet.setText(t);
        });

        Button btnCon = findViewById(R.id.btn_XacNhan);
        btnCon.setOnClickListener(v -> {
            String nameText = name.getText().toString().trim();
            String addressText = address.getText().toString().trim();

            String w = wallet.getText().toString().split(":", 2)[1].trim();
            double walletDouble = Double.parseDouble(w);

            if(!nameText.isEmpty() && !addressText.isEmpty()){
                customer_bll.updateCustomer(new Customer_DTO(thisCustomer.getId(), nameText, addressText, walletDouble));
                Toast.makeText(this, "Changed successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(this, "Cannot change", Toast.LENGTH_SHORT).show();
        });

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });
    }
}