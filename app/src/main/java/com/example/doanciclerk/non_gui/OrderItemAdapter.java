package com.example.doanciclerk.non_gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Customer_BLL;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.dto.Customer_DTO;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.gui.DetailStorageItemActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemVH> {
    List<Order_DTO> list;

    Orders_BLL orders_bll;
    Goods_BLL goods_bll;
    Customer_BLL customer_bll;
    Context context;

    public OrderItemAdapter(List<Order_DTO> list, Context context) {
        this.list = list;
        this.context = context;

        goods_bll = new Goods_BLL(context);
        customer_bll = new Customer_BLL(context);
        orders_bll = new Orders_BLL(context);
    }

    @NonNull
    @Override
    public OrderItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_layout, parent, false);
        OrderItemVH vh = new OrderItemVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemVH holder, int position) {
        Order_DTO o = list.get(position);

        String sumText = String.format("Sum: %.0f", o.getPriceSum());
        holder.sum.setText(sumText);

        Customer_DTO c = customer_bll.findCustomerByID(o.getCustomerID());

        String customerNameText = "Name: " + c.getName();
        holder.customerName.setText(customerNameText);

        String addressText = "Address: " + c.getAddress();
        holder.address.setText(addressText);

        Goods_DTO g = goods_bll.findGoodsByID(o.getGoodsID(), o.getStoreID());

        String imageName = g.getImage().split("\\.", 2)[0];
        InputStream is = null;
        try{
            is = context.getResources().openRawResource(context.getResources().getIdentifier(imageName, "raw", context.getPackageName()));
        }
        catch (Exception e){
            try {
                Path p = Paths.get(context.getFilesDir().getPath() + "/" + g.getImage());
                is = context.getContentResolver().openInputStream(Uri.parse(p.toUri().toString()));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        Bitmap img = BitmapFactory.decodeStream(is);

        holder.image.setImageBitmap(img);

        String goodsNameText = "Product: " + g.getName();
        holder.goodsName.setText(goodsNameText);

        String amountText = "Amount: " + g.getAmount();
        holder.amount.setText(amountText);

        String priceText = String.format("Price: %.0f",g.getPrice()) + "₫";
        holder.price.setText(priceText);

        holder.yes.setOnClickListener(v->{
            orders_bll.deleteOrder(o.getId());
            list.remove(o);
            notifyItemRemoved(holder.getAdapterPosition());

            Toast.makeText(context, "Order delivered", Toast.LENGTH_SHORT).show();
        });

        holder.no.setOnClickListener(v -> {
            orders_bll.deleteOrder(o.getId());
            list.remove(o);
            notifyItemRemoved(holder.getAdapterPosition());

            Toast.makeText(context, "Order canceled", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();

        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class OrderItemVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView customerName, address, goodsName, amount, price, sum;

        private ImageButton yes, no;

        public OrderItemVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.thumbnailImage);
            customerName = itemView.findViewById(R.id.txtCustomerName);
            amount = itemView.findViewById(R.id.txtAmount);
            price = itemView.findViewById(R.id.txtPrice);
            address = itemView.findViewById(R.id.txtAddress);
            goodsName = itemView.findViewById(R.id.txtProduct);
            sum = itemView.findViewById(R.id.txtSum);

            yes = itemView.findViewById(R.id.btnYes);
            no = itemView.findViewById(R.id.btnNo);
        }
    }
}
