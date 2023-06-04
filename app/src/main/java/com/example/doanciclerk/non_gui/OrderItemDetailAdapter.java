package com.example.doanciclerk.non_gui;

import android.content.Context;
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
import com.example.doanciclerk.bll.OrderDetails_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.OrderItemDetailVH> {
    Order_DTO po;
    List<Goods_DTO> list;
    List<Integer> amountList;

    OrderDetails_BLL orderDetails_bll;
    Context context;

    public OrderItemDetailAdapter(Order_DTO po, Context context) {
        this.list = new ArrayList<>();
        this.amountList = new ArrayList<>();
        if(po != null) {
            list = po.getGoodsList();
            amountList = po.getAmountList();
        }

        this.context = context;

        orderDetails_bll = new OrderDetails_BLL(context);
    }

    @NonNull
    @Override
    public OrderItemDetailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_detail_layout, parent, false);
        OrderItemDetailVH vh = new OrderItemDetailVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemDetailVH holder, int position) {
        Goods_DTO g = list.get(position);
        int amount = amountList.get(position);
        double sumNum = 0;

        if(po != null)
            sumNum = po.getPriceSum();

        String goodsNameText = "Name: " + g.getName();
        holder.goodsName.setText(goodsNameText);

        String descriptionText = "Description: " + g.getDescription();
        holder.desc.setText(descriptionText);

        String imageName = g.getImage().split("\\.", 2)[0];
        InputStream is;
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

        String amountText = "Amount: " + amount;
        holder.amount.setText(amountText);

        String priceText = String.format("Price: %.0f",g.getPrice()) + " ₫";
        if (g.getDiscount() > 0)
            priceText = String.format("Price: %.0f",g.getDiscount()) + " ₫";
        holder.price.setText(priceText);

        double sss;
        if(g.getDiscount() > 0)
            sss = g.getDiscount() * amount;
        else
            sss = g.getPrice() * amount;

        String sumText = String.format("Sum: %.0f", sss);
        holder.sum.setText(sumText);
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

    public class OrderItemDetailVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView desc, goodsName, amount, price, sum;

        public OrderItemDetailVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.thumbnailImage);
            goodsName = itemView.findViewById(R.id.txtGoodsName);
            desc = itemView.findViewById(R.id.txtDescription);
            amount = itemView.findViewById(R.id.txtAmount);
            price = itemView.findViewById(R.id.txtPrice);
            sum = itemView.findViewById(R.id.txtSum);
        }
    }
}
