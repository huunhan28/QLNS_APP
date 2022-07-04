package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food.Api.Api;
import com.example.food.Domain.Response.UpdatePasswordResponse;
import com.example.food.network.Listener.UpdatePasswordResponseListener;
import com.example.food.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPasswordActivity extends AppCompatActivity {

    EditText editTextNewPassword,editTextNewPassword2;
    Button buttonConfirmNewPassword;
    String gmail="";
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        gmail=getIntent().getStringExtra("gmail");
        api = new Api(this);
        setControl();
        setEvent();
    }
    private void setEvent() {
        buttonConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editTextNewPassword.getText())||TextUtils.isEmpty(editTextNewPassword2.getText())) {
                    Toast.makeText(NewPasswordActivity.this, "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editTextNewPassword.getText().toString().trim().equals(editTextNewPassword2.getText().toString().trim())) {
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                api.updatePassword(updatePasswordResponseListener,gmail,editTextNewPassword.getText().toString().trim());

            }
        });
    }
    private void setControl() {
        editTextNewPassword=findViewById(R.id.editTextNewPassword);
        editTextNewPassword2=findViewById(R.id.editTextNewPassword2);
        buttonConfirmNewPassword=findViewById(R.id.buttonConfirmNewPassword);
    }
    private  final UpdatePasswordResponseListener updatePasswordResponseListener=new UpdatePasswordResponseListener() {
        @Override
        public void didFetch(UpdatePasswordResponse response, String message) {
            Toast.makeText(NewPasswordActivity.this,"Update password successfully",Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(NewPasswordActivity.this, SigninActivity.class);
//            startActivity(intent);
        }

        @Override
        public void didError(String message) {

        }
    };
}