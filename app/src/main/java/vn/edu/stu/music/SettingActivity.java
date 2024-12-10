package vn.edu.stu.music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {
    Button btnAddSetting, btnCancelSetting;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbarSetting;
        toolbarSetting  = findViewById(R.id.toolBarSetting);
        setSupportActionBar(toolbarSetting);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnAddSetting=findViewById(R.id.btnAddSetting);
        btnCancelSetting=findViewById(R.id.btnCancelSetting);
    }

    private void addEvents() {
        btnCancelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,
                        R.string.s_succeeded,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= new MenuInflater(this);
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.genresBar)
        {
            Intent intent= new Intent(SettingActivity.this, genres.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(SettingActivity.this, About.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.musicBar)
        {
            Intent intent= new Intent(SettingActivity.this, Songs.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settingBar)
        {
            Intent intent= new Intent(SettingActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);

    }
}