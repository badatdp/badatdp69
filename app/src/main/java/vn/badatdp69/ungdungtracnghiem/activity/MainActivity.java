package vn.badatdp69.ungdungtracnghiem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.fragment.ScoreboardFragment;
import vn.badatdp69.ungdungtracnghiem.fragment.HomeFragment;
import vn.badatdp69.ungdungtracnghiem.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnlogout;
    Button btndatgio;
    Button btnsendtogmail;
    Button btnprofile;

    private NavigationView navigationView;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        btnlogout = (Button) findViewById(R.id.btnlogout);
        btndatgio = (Button) findViewById(R.id.btndatgio);
        btnsendtogmail = (Button) findViewById(R.id.btnsendtogmail);
        btnprofile = (Button) findViewById(R.id.btnprofile);

        navigationView = findViewById(R.id.navigation_view);

        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            showDefaultFragment();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        initNavigation();

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Học hành là chuyện cả đời!").
                                setMessage("Bạn có chắc là muốn đăng xuất không");
                        builder.setPositiveButton("Có!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(getApplicationContext(),
                                                loginActivity.class);
                                        startActivity(i);
                                        MainActivity.this.finish();
                                    }
                                });
                        builder.setNegativeButton("Không!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder.create();
                        alert11.show();

            }
        });
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private void initNavigation(){
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        View view = navigationView.getHeaderView(0);
        final TextView tvEmail = view.findViewById(R.id.tv_email);
        final TextView tvxinchao = view.findViewById(R.id.tv_xinchao);
        final Button btnprofile = view.findViewById(R.id.btnprofile);
        final Button btnsendtogmail = view.findViewById(R.id.btnsendtogmail);
        final Button btndatgio = view.findViewById(R.id.btndatgio);
        final Button btnlogout = view.findViewById(R.id.btnlogout);
        if (mAuth.getUid() != null){
            mRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        User user = snapshot.getValue(User.class);
                        tvEmail.setText(user.getEmail() + "\n" + user.getName());
                        tvxinchao.setText("Xin chào bạn,");
                        btnprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                                startActivity(intent);
                            }
                        });
                        btnsendtogmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =  new Intent(MainActivity.this, SendtogmailActivity.class);
                                startActivity(intent);
                            }
                        });
                        btndatgio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,SetalarmActivity.class);
                                startActivity(intent);
                            }
                        });

                        btnlogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseAuth.getInstance().signOut();

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Học hành là chuyện cả đời!").
                                        setMessage("Bạn có chắc là muốn đăng xuất không");
                                builder.setPositiveButton("Có!",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(getApplicationContext(),
                                                        loginActivity.class);
                                                startActivity(i);
                                                MainActivity.this.finish();
                                            }
                                        });
                                builder.setNegativeButton("Không!",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert11 = builder.create();
                                alert11.show();

                            }
                        });

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void onBackPressed() {
    }

    private void loadHomePage() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                showDefaultFragment();
                break;
            case R.id.nav_scoreboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ScoreboardFragment()).commit();
                toolbar.setTitle("Bảng điểm");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDefaultFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Trang Chủ");
        }
    }
}
