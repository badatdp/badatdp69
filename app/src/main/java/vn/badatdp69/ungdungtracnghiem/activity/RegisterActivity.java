package vn.badatdp69.ungdungtracnghiem.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.model.User;

public class RegisterActivity extends AppCompatActivity {

    EditText userEmail,userPassword,username,userRepass;
    TextView tvLogin, tv_title;
    Button btnRegister;
    FirebaseAuth mFirebaseAuth;
    ImageView iv_user;
    String name,email,pwd,repass;
    private DatabaseReference mRef;
    CheckBox showpass,showpass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        userPassword = (EditText) findViewById(R.id.et_password);
        userEmail = (EditText) findViewById(R.id.et_email) ;
        userRepass = (EditText) findViewById(R.id.et_repassword);
        username = (EditText) findViewById(R.id.username);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvLogin = (TextView) findViewById(R.id.tv_dangnhap);
        iv_user = (ImageView) findViewById(R.id.iv_user);
        showpass = (CheckBox) findViewById(R.id.showpass);
        showpass2 = (CheckBox) findViewById(R.id.showpass2);
        tv_title = findViewById(R.id.tv_title);

        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        tv_title.setTypeface(typeface);

        final LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterActivity.this,loginActivity.class);
                startActivity(it);
            }
        });

        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        showpass2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userRepass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    userRepass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 email = userEmail.getText().toString();
                 pwd = userPassword.getText().toString();
                 name = username.getText().toString();
                 repass = userRepass.getText().toString();



                if (email.isEmpty()) {
                    userEmail.setError("Mời bạn nhập email!");
                    userEmail.requestFocus();
                }
                else if (pwd.isEmpty()){
                    userPassword.setError("Mời bạn nhập mật khẩu!");
                    userPassword.requestFocus();
                }
                else if (repass.isEmpty()){
                    userRepass.setError("Mời bạn nhập mật khẩu xác nhận!");
                    userRepass.requestFocus();
                }
                else if (name.isEmpty()){
                    username.setError("Mời bạn nhập tên!");
                    username.requestFocus();
                }
                else if (email== null && pwd.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Mời nhập email hoặc mật khẩu!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                FirebaseUser fs = mFirebaseAuth.getCurrentUser();
                                User user = new User(fs.getUid(), email, name);
                                mRef.child(fs.getUid()).setValue(user);
                                loadingDialog.startLoadingDialog();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dimissDialog();
                                        saveUser(email, pwd);
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                                        finish();
                                    }
                                }, 5000);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký không thành công, mời bạn thử lại",Toast.LENGTH_SHORT).show();
                            }
                        }

                        private void updateUI(FirebaseUser user) {
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Đã xuất hiện lỗi!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        {
            Toast.makeText(RegisterActivity.this, "Mời bạn đăng ký!",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser(String email, String password){
        SharedPreferences pref = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        //Lưu giữ liệu vào SharedPrefs
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

}
