package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.food.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OTPActivity extends AppCompatActivity {
    EditText editTextConfirmOTP;
    Button buttonConfirm;
    String otp="",gmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        otp=getIntent().getStringExtra("otp");
        gmail=getIntent().getStringExtra("gmail");
        setControl();
        setEvent();
    }
    private void setControl() {
        editTextConfirmOTP=findViewById(R.id.editTextConfirmOTP);
        buttonConfirm=findViewById(R.id.buttonConfirm);
    }
    private void setEvent() {
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextConfirmOTP.getText().toString().equals(otp)){
                    Intent intent = new Intent(OTPActivity.this, NewPasswordActivity.class);
                    intent.putExtra("gmail",gmail);
                    startActivity(intent);
                }
            }
        });
    }
}