package com.example.doanciclerk.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Customer_BLL;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Store_DTO;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    DatabaseHelper db;

    Customer_BLL customer_bll;
    Store_BLL store_bll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this, "db_a", null, 1);
        customer_bll = new Customer_BLL(this);
        store_bll = new Store_BLL(this);

        int type = getIntent().getIntExtra("Type", 1);

        if(type == 1)
            setContentView(R.layout.signup_customer_ui);
        else
            setContentView(R.layout.signup_store_ui);

        TextView txtID = findViewById(R.id.txt_Username);
        TextView txtPassword = findViewById(R.id.txt_Password);

        TextView txtName = findViewById(R.id.txt_Name);
        TextView txtAddress = findViewById(R.id.txt_Address);
        TextView txtWallet = findViewById(R.id.txt_Wallet);

        Button btnCon = findViewById(R.id.btn_XacNhan);
        btnCon.setOnClickListener(v -> {
            String id = txtID.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String name = txtName.getText().toString().trim();
            String address = txtAddress.getText().toString().trim();
            double wallet = Double.parseDouble(txtWallet.getText().toString().trim());

            if(!id.isEmpty() && !password.isEmpty() && !name.isEmpty() && !address.isEmpty() && !String.valueOf(wallet).isEmpty()) {
                db.addAccount(db.getWritableDatabase(), new Account_DTO(id, password, type));

                if(type == 0){
                    store_bll.addStore(new Store_DTO(id, name, address, wallet));

                    String text = "Store " + name + " added";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                else{
                    customer_bll.addCustomer(new Customer_DTO(id, name, address, wallet));

                    String text = "Customer " + name + " added";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }

                db.close();

                finish();
            }
            else
                Toast.makeText(this, "Not enough information", Toast.LENGTH_SHORT).show();
        });

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });
    }
}