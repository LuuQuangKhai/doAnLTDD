package com.example.doanciclerk.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doanciclerk.R;

public class LoginTapFragment extends Fragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.login_tap_fragment, container,false);

        return root;
    }

    public void checkLogin(){
        String username = ((EditText) getView().findViewById(R.id.txt_SDT)).toString();
        String password = ((EditText) getView().findViewById(R.id.txt_MatKhau)).toString();

        if(username.equals("111") && password.equals("123"))
            Toast.makeText(getContext(), "Ok dang nhap thanh cong", Toast.LENGTH_SHORT).show();
    }
}
