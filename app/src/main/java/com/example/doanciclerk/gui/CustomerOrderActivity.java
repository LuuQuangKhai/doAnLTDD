package com.example.doanciclerk.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.non_gui.CustomerOrderItemAdapter;

public class CustomerOrderActivity extends AppCompatActivity {
    Order_DTO po;
    Orders_BLL orders_bll;

    Store_BLL store_bll;

    LinearLayout linearLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerorder_ui);

        orders_bll = new Orders_BLL(this);
        store_bll = new Store_BLL(this);

        po = (Order_DTO) getIntent().getSerializableExtra("PendingOrder");

        TextView orderstate = findViewById(R.id.orderstatetxt);
        linearLayout = findViewById(R.id.linearRecyclerView);
        recyclerView = findViewById(R.id.orderItem_recycler);
        TextView storename = findViewById(R.id.storenametxt);
        TextView sum = findViewById(R.id.sumtxt);
        Button btnCancel = findViewById(R.id.btn_Cancel);
        Button btnBuy = findViewById(R.id.btn_Buy);

        if(po != null){
            String orderstatetxt = "OrderID: " + po.getId();
            orderstate.setText(orderstatetxt);

            CustomerOrderItemAdapter orderItemAdapter = new CustomerOrderItemAdapter(po, this);
            recyclerView.setAdapter(orderItemAdapter);


            storename.setText(store_bll.findStoreByID(po.getStoreID()).getName());


            sum.setText(String.valueOf(po.getPriceSum()));

            btnCancel.setOnClickListener(v -> {
                cancelAlert();
            });

            btnBuy.setOnClickListener(v -> {
                orders_bll.addOrder(po);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                setResult(102);
                finish();
            });
        }
        else{
            String ort = "No pending order";
            orderstate.setText(ort);

            linearLayout.setVisibility(View.GONE);
            storename.setVisibility(View.GONE);
            sum.setVisibility(View.GONE);
            btnBuy.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);

            TextView storeLabel = findViewById(R.id.storenamelabel);
            storeLabel.setVisibility(View.GONE);
            TextView sumLabel = findViewById(R.id.sumlabel);
            sumLabel.setVisibility(View.GONE);
        }

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CustomerOrderItemAdapter orderItemAdapter = new CustomerOrderItemAdapter(po, this);
        recyclerView = findViewById(R.id.orderItem_recycler);
        recyclerView.setAdapter(orderItemAdapter);
    }

    private void cancelAlert(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_Material3_Dark_Dialog_Alert);
        b.setTitle("Cancel");
        b.setMessage("Do you want to cancel this order ?");
        b.setPositiveButton("Confirm", (dialog, which) -> {
            setResult(102);
            finish();
        });

        b.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog a = b.show();
    }
}