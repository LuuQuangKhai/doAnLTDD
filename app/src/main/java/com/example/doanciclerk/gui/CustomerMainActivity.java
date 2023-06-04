package com.example.doanciclerk.gui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Customer_BLL;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Order_Details_DTO;
import com.example.doanciclerk.dto.Store_DTO;
import com.example.doanciclerk.non_gui.CustomerMainItemAdapter;
import com.example.doanciclerk.non_gui.StorageItemAdapter;
import com.example.doanciclerk.non_gui.StoreCustomerMainItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CustomerMainActivity extends AppCompatActivity {
    Customer_BLL customer_bll;
    Customer_DTO thisCustomer;
    Context context;
    String storeID; //Default store filter
    Order_DTO po;
    Orders_BLL orders_bll;
    Goods_BLL goods_bll;
    Store_BLL store_bll;

    List<Goods_DTO> recyclerList;
    StoreCustomerMainItemAdapter storeCustomerMainItemAdapter;
    CustomerMainItemAdapter customerMainItemAdapter;
    RecyclerView recyclerView;

    List<Store_DTO> storeList;
    RecyclerView storeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customermain_ui);

        Account_DTO acc = (Account_DTO) getIntent().getSerializableExtra("Account");
        orders_bll = new Orders_BLL(this);
        customer_bll = new Customer_BLL(this);
        thisCustomer = customer_bll.findCustomerByID(acc.getId());

        po = (Order_DTO) getIntent().getSerializableExtra("PendingOrder");

        context = this;

        goods_bll = new Goods_BLL(this);
        recyclerView = findViewById(R.id.recyclerViewCustomer);
        store_bll = new Store_BLL(this);
        storeRecyclerView = findViewById(R.id.storeRecyclerView);

        storeList = store_bll.getStore_List_All();
        storeID = storeList.get(0).getId();

        recyclerList = goods_bll.getGoods_List_StoreOnly(storeID);
        customerMainItemAdapter = new CustomerMainItemAdapter(recyclerList, this, po, thisCustomer.getId());
        recyclerView.setAdapter(customerMainItemAdapter);

        storeCustomerMainItemAdapter = new StoreCustomerMainItemAdapter(this, storeList, recyclerView, storeID, po, thisCustomer.getId());
        storeRecyclerView.setAdapter(storeCustomerMainItemAdapter);

        FloatingActionButton btnBackTop = findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(v -> {
            recyclerView.scrollToPosition(0);

            btnBackTop.setEnabled(false);
            btnBackTop.setVisibility(View.INVISIBLE);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.computeVerticalScrollOffset() == 0){
                    btnBackTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    btnBackTop.setEnabled(true);
                    btnBackTop.setVisibility(View.VISIBLE);
                }
            }
        });

        SearchView searchView = findViewById(R.id.searchViewCustomer);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerList = goods_bll.search(newText, storeID);
                customerMainItemAdapter = new CustomerMainItemAdapter(recyclerList, context, po, thisCustomer.getId());
                recyclerView.setAdapter(customerMainItemAdapter);
                return true;
            }
        });

        ImageButton btnLogout = findViewById(R.id.imageButton3);
        btnLogout.setOnClickListener(v -> {
            logoutAlert();
        });

        ImageButton btnInfo = findViewById(R.id.btn_ThongTinNguoiDung);
        btnInfo.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerInfoActivity.class);

            i.putExtra("Customer", thisCustomer);
            startActivity(i);
        });

        ImageButton btnToCart = findViewById(R.id.btn_GioHang);
        btnToCart.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerOrderActivity.class);

            i.putExtra("PendingOrder", po);
            i.putExtra("CustomerID", thisCustomer.getId());

            startActivityIfNeeded(i, 12);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        thisCustomer = customer_bll.findCustomerByID(thisCustomer.getId());
    }

    @Override
    public void onBackPressed() {
        logoutAlert();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12){
            if(resultCode == 102){
                po = null;
            }
        }

        if(requestCode == 11){
            if(resultCode == RESULT_OK) {
                if(data != null){
                    po = (Order_DTO) data.getSerializableExtra("PendingOrder");
                }
            }
        }

        storeCustomerMainItemAdapter = new StoreCustomerMainItemAdapter(this, storeList, recyclerView, storeID, po, thisCustomer.getId());
        storeRecyclerView.setAdapter(storeCustomerMainItemAdapter);

        customerMainItemAdapter = new CustomerMainItemAdapter(recyclerList, this, po, thisCustomer.getId());
        recyclerView.setAdapter(customerMainItemAdapter);
    }

    private void logoutAlert(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_Material3_Dark_Dialog_Alert);
        b.setTitle("Log out");
        b.setMessage("Do you want to log out ?");
        b.setPositiveButton("Confirm", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Logged Account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        b.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog a = b.show();
    }
}