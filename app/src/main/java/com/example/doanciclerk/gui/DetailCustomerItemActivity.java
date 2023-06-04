package com.example.doanciclerk.gui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.OrderDetails_BLL;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Order_Details_DTO;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DetailCustomerItemActivity extends AppCompatActivity {
    ImageView image;
    String imageName = "";
    String customerID;

    Order_DTO po;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodsdetail_ui);

        po = (Order_DTO) getIntent().getSerializableExtra("PendingOrder");
        customerID = getIntent().getStringExtra("CustomerID");
        Goods_DTO goods_dto = (Goods_DTO) getIntent().getSerializableExtra("Goods");
        imageName = goods_dto.getImage();

        Orders_BLL orders_bll = new Orders_BLL(this);
        Store_BLL store_bll = new Store_BLL(this);
        OrderDetails_BLL orderDetails_bll = new OrderDetails_BLL(this);

        TextView name = findViewById(R.id.nametxt);
        TextView desc = findViewById(R.id.desctxt);
        TextView storeName = findViewById(R.id.storenametxt);
        TextView price = findViewById(R.id.pricetxt);
        TextView discount = findViewById(R.id.discounttxt);
        TextView amount = findViewById(R.id.amounttxt);
        TextView sum = findViewById(R.id.sumtxt);
        image = findViewById(R.id.thumbnailImage);

        name.setText(goods_dto.getName());
        desc.setText(goods_dto.getDescription());
        storeName.setText(store_bll.findStoreByID(goods_dto.getStoreID()).getName());
        price.setText(String.valueOf(goods_dto.getPrice()));
        discount.setText(String.valueOf(goods_dto.getDiscount()));
        amount.setText("0");

        int a = Integer.parseInt(amount.getText().toString());

        double sumNumm;
        if(goods_dto.getDiscount() != 0)
            sumNumm = a * goods_dto.getDiscount();
        else
            sumNumm = a * goods_dto.getPrice();

        sum.setText("" + sumNumm);

        int num;
        if((num = getIntent().getIntExtra("Amount", 0)) == 0)
            amount.setText("0");
        else
            amount.setText(num);

        image.setImageBitmap(getBitmapFromImage(goods_dto.getImage(), getApplicationContext()));

        image.setOnClickListener(v-> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityIfNeeded(Intent.createChooser(i, "Select image"), 102);
        });

        ImageButton plus = findViewById(R.id.btnPlus);
        ImageButton minus = findViewById(R.id.btnMinus);
        plus.setOnClickListener(v -> {
            int o = Integer.parseInt(amount.getText().toString());
            o++;
            amount.setText("" + o);

            double sumNum;
            if(goods_dto.getDiscount() != 0)
                sumNum = o * goods_dto.getDiscount();
            else
                sumNum = o * goods_dto.getPrice();

            sum.setText("" + sumNum);
        });

        minus.setOnClickListener(v -> {
            int o = Integer.parseInt(amount.getText().toString());
            if(o>0) {
                o--;
                amount.setText("" + o);

                double sumNum;
                if(goods_dto.getDiscount() != 0)
                    sumNum = o * goods_dto.getDiscount();
                else
                    sumNum = o * goods_dto.getPrice();

                sum.setText("" + sumNum);
            }
        });


        Button btnToCart = findViewById(R.id.btn_ToCart);
        btnToCart.setOnClickListener(v-> {
            if(po == null){
                String id = orders_bll.getOrderID_Auto();
                po = new Order_DTO(id, new Order_Details_DTO(id), goods_dto.getStoreID(), customerID);
            }

            orderDetails_bll.addToOrderDetails(po, goods_dto, Integer.parseInt(amount.getText().toString().trim()));

            String message = goods_dto.getName() + " added to cart";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            getIntent().putExtra("PendingOrder", po);
            setResult(RESULT_OK, getIntent());
            finish();
        });

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });
    }

    private Bitmap getBitmapFromImage(String imageName, Context context){
        String i = imageName.split("\\.", 2)[0];
        InputStream is;

        try {
            is = context.getResources().openRawResource(context.getResources().getIdentifier(i, "raw", context.getPackageName()));
        }
        catch (Exception e){
            try {
                Path p = Paths.get(context.getFilesDir().getPath() + "/" + imageName);
                is = context.getContentResolver().openInputStream(Uri.parse(p.toUri().toString()));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        return BitmapFactory.decodeStream(is);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 102)
        {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                    image.setImageBitmap(bitmap);

                    imageName = getFilenameFromURI(data.getData());

                    InputStream is = getContentResolver().openInputStream(data.getData());
                    File file = new File(getFilesDir(), imageName);

                    OutputStream os = new FileOutputStream(file);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    os.write(bytes);

                    os.close();
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String getFilenameFromURI(Uri data){
        Uri uri = data;
        Cursor c = getContentResolver().query(uri, null,null,null);
        int name = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        c.moveToFirst();
        String nameS = c.getString(name);
        c.close();
        return nameS;
    }
}