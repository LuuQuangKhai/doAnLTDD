package com.example.doanciclerk.gui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.dto.Goods_DTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DetailStorageItemActivity_Add extends AppCompatActivity {
    ImageView image;
    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstorageitem_ui);

        String storeID = getIntent().getStringExtra("Account ID");

        Goods_BLL goods_bll = new Goods_BLL(this);

        EditText name = findViewById(R.id.txt_TenMatHang);
        EditText desc = findViewById(R.id.txt_MoTa);
        EditText price = findViewById(R.id.txt_GiaTien);
        EditText amount = findViewById(R.id.txt_SoLuong);
        image = findViewById(R.id.thumbnailImage);

        name.setText(null);
        desc.setText(null);
        price.setText(null);
        amount.setText(null);
        image.setImageResource(R.drawable.blank_image);

        image.setOnClickListener(v-> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityIfNeeded(Intent.createChooser(i, "Select image"), 102);
        });

        Button btnCon = findViewById(R.id.btn_XacNhan);
        btnCon.setOnClickListener(v-> {
            String nameText = name.getText().toString().trim();
            String descText = desc.getText().toString().trim();

            double priceText = 0;
            if(!price.getText().toString().isEmpty())
                priceText = Double.parseDouble(price.getText().toString());

            int amountText = 0;
            if(!amount.getText().toString().isEmpty())
                amountText = Integer.parseInt(amount.getText().toString());

            if(!imageName.isEmpty() && !nameText.isEmpty() && !descText.isEmpty()){
                goods_bll.addGoods(new Goods_DTO(goods_bll.getGoodsID_Auto(), imageName, nameText, descText, storeID, amountText, priceText));
                String t = nameText + " added";
                Toast.makeText(this, t, Toast.LENGTH_SHORT).show();

                finish();
            }
            else
                Toast.makeText(this, "Cannot add", Toast.LENGTH_SHORT).show();
        });
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