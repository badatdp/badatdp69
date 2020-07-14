package vn.badatdp69.ungdungtracnghiem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.model.User;

public class loginActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private TextView tvfb;
    private ImageView ivuser;
    private LoginButton loginButton;
    private static final String TAG = "Facebook authecation";
    private AccessTokenTracker accesstokentracker;
    private TextView tv_user;

    EditText etEmail, etPassword;
    TextView tvRegister;
    Button btnLogin;
    TextView tv_forgotpass;
    TextView tv_title;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mRef;
    CheckBox showpass3;

    RelativeLayout relay1, relay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
            relay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        FacebookSdk.sdkInitialize(getApplicationContext());
        tvfb = findViewById(R.id.tv_user);
        ivuser = findViewById(R.id.iv_user);
        loginButton = findViewById(R.id.login_btn);
        tv_user = findViewById(R.id.tv_user);
        loginButton.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        showpass3 = (CheckBox) findViewById(R.id.showpass3);
        tv_title = findViewById(R.id.tv_title);

        Typeface  typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        tv_title.setTypeface(typeface);

        final LoadingDialog loadingDialog = new LoadingDialog(loginActivity.this);

        showpass3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());

                Intent intSignUp = new Intent(loginActivity.this, MainActivity.class);
                startActivity(intSignUp);

                {
                    Toast.makeText(loginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error);
            }
        });


        relay1 = (RelativeLayout) findViewById(R.id.relay1);
        relay2 = (RelativeLayout) findViewById(R.id.relay2);
        handler.postDelayed(runnable, 2000);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        tvRegister = (TextView) findViewById(R.id.tv_dangky);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(loginActivity.this, "Đang chuyển sang đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

        accesstokentracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String pwd = etPassword.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Mời bạn nhập email");
                    etEmail.requestFocus();
                } else if (pwd.isEmpty()) {
                    etPassword.setError("Mời bạn nhập mật khẩu!");
                    etPassword.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(loginActivity.this, "Email hoặc mật khẩu đang trống!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(loginActivity.this, "Đăng nhập bị lỗi, mời bạn kiểm tra lại!", Toast.LENGTH_SHORT).show();
                            } else {
                                {
                                    loadingDialog.startLoadingDialog();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            saveUser(email, pwd);
                                            loadingDialog.dimissDialog();
                                            Intent intToMain = new Intent(loginActivity.this, MainActivity.class);
                                            startActivity(intToMain);
                                            Toast.makeText(loginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }, 5000);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(loginActivity.this, "Đã xảy ra lỗi, mời kiểm tra lại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intTofor = new Intent(loginActivity.this, forgotpassActivity.class);
                startActivity(intTofor);

                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dimissDialog();
                    }
                }, 5000);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(loginActivity.this, RegisterActivity.class);
                startActivity(intSignUp);

                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dimissDialog();
                    }
                }, 5000);
            }
        });
    }

    private void createUser(String uid, User user) {
        mRef.child(uid).setValue(user);
    }

    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
        loadUser();
    }

    private void loadUser() {
        SharedPreferences pref = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String email = pref.getString("email", null);
        String password = pref.getString("password", null);
        etEmail.setText(email);
        etPassword.setText(password);
    }


    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "sign in with crediantal : successfully");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.d(TAG, "sign in with crediantal : faillu", task.getException());
                    Toast.makeText(loginActivity.this, "Authecation fail", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            tvfb.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(ivuser);
            }
        } else {
            tvfb.setText("");
            ivuser.setImageResource(R.drawable.quiz);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    ;

    protected void onStop() {
        super.onStop();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void saveUser(String email, String password) {
        SharedPreferences pref = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        //Lưu giữ liệu vào SharedPrefs
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}

