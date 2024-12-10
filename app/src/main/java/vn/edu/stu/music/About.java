package vn.edu.stu.music;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class About extends AppCompatActivity implements OnMapReadyCallback {
    Button btnCall;
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbarAbout;
        toolbarAbout = findViewById(R.id.toolBarAbout);
        setSupportActionBar(toolbarAbout);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnCall=findViewById(R.id.btnCall);
    }

    private void addEvents() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "0359128746";

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(About.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    // Kích hoạt Intent quay số
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
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
            Intent intent= new Intent(About.this, genres.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(About.this, About.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.musicBar)
        {
            Intent intent= new Intent(About.this, Songs.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settingBar)
        {
            Intent intent= new Intent(About.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng stu = new LatLng(10.738034010562478, 106.67787534467176);
        mMap.addMarker(new MarkerOptions().position(stu).title("Marker in Stu"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(stu));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stu, 17));
    }
}