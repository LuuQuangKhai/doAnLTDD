package com.example.doanciclerk.gui;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.non_gui.OrderItemAdapter;
import com.example.doanciclerk.non_gui.OrderItemDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderManageActivity extends AppCompatActivity {
    List<Order_DTO> list;
    String storeID;

    Orders_BLL orders_bll;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordermanager_ui);

        storeID = getIntent().getStringExtra("Account ID");

        orders_bll = new Orders_BLL(this);
        list = orders_bll.getOrders_List(storeID);
        if(list == null)
            list = new ArrayList<>();

        OrderItemAdapter adapter = new OrderItemAdapter(list, this);
        recyclerView = findViewById(R.id.orderItem_recycler);
        recyclerView.setAdapter(adapter);

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        list = orders_bll.getOrders_List(storeID);
        OrderItemAdapter adapter = new OrderItemAdapter(list, this);
        recyclerView = findViewById(R.id.orderItem_recycler);
        recyclerView.setAdapter(adapter);
    }
}