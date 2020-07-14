package vn.badatdp69.ungdungtracnghiem.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import vn.badatdp69.ungdungtracnghiem.R;

public class kqActivity extends AppCompatActivity {

    TextView tvtrove;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kq);

        tvtrove = (TextView) findViewById(R.id.tv_trove);

        tvtrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inttonha = new Intent(kqActivity.this,loginActivity.class);
                startActivity(inttonha);
            }
        });

    }
}
