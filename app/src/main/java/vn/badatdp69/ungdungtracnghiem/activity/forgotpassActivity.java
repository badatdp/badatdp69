package vn.badatdp69.ungdungtracnghiem.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import vn.badatdp69.ungdungtracnghiem.R;

public class forgotpassActivity extends AppCompatActivity {

    EditText userEmail;
    Button userPass;
    Button btnback;
    FirebaseAuth firebaseAuth;
    TextView tv_title;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btn_forgot);
        btnback = findViewById(R.id.btn_back);
        tv_title = findViewById(R.id.tv_title);

        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        tv_title.setTypeface(typeface);

        firebaseAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(forgotpassActivity.this,
                                    "Link đổi mật khẩu đã được gửi tới Email của bạn", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(forgotpassActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intback = new Intent(forgotpassActivity.this, loginActivity.class);
                startActivity(intback);
            }
        });
    }
}