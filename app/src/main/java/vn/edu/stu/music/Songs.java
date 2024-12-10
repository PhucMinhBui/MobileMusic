package vn.edu.stu.music;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;


import vn.edu.stu.music.adapter.BaihatAdapter;
import vn.edu.stu.music.dao.Database;
import vn.edu.stu.music.model.BaiHat;
import vn.edu.stu.music.model.Genres;

public class Songs extends AppCompatActivity {
    final String DATABASE_NAME="dbbaihat.sqlite";
    SQLiteDatabase database;
    ListView lvMusic;
    ArrayList<BaiHat> dsBaihat;
    BaihatAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_songs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbarSong;
        toolbarSong = findViewById(R.id.toolBarSong);
        setSupportActionBar(toolbarSong);
        addControls();
        addEvents();
        readData();
    }



    private void addControls() {
        lvMusic = findViewById(R.id.lvMusic);
        dsBaihat=new ArrayList<>();
        adapter= new BaihatAdapter(this, dsBaihat);
        lvMusic.setAdapter(adapter);

    }

    private void addEvents() {
        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaiHat baiHat = dsBaihat.get(i);
                Intent intent= new Intent(Songs.this,  EditSongActivity.class);
                intent.putExtra("id_Song",baiHat.getId_song());
                startActivity(intent);
            }
        });
        lvMusic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                new AlertDialog.Builder(Songs.this)
                        .setTitle("Xóa")
                        .setMessage("Bạn có chắc muốn xóa không ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteSongs(dsBaihat.get(position).getId_song());
                            }
                        })
                        .setNegativeButton("Không",null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });


        
    }


    private void readData() {
        database=Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor= database.rawQuery("SELECT * FROM baihat", null);
        dsBaihat.clear();
        for(int i=0; i<cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            int id_song = cursor.getInt(0);
            String name = cursor.getString(1);
            String author = cursor.getString(2);
            int id_genres = cursor.getInt(3);
            String time = cursor.getString(4);
            byte[] picture = cursor.getBlob(5);

            // Lấy thông tin từ bảng genres
            Genres genres = null;
            Cursor genresCur = database.rawQuery(
                    "SELECT * FROM genres WHERE id_genres = ?",
                    new String[]{String.valueOf(id_genres)}
            );

            if (genresCur != null && genresCur.moveToFirst()) {
                String genresName = genresCur.getString(1);
                genres = new Genres(id_genres, genresName);
                genresCur.close();
            } else {
                genres = new Genres(id_genres, "Không xác định");
            }

            dsBaihat.add(new BaiHat(id_song, name, author, genres, time, picture));
        }
        cursor.close();
        adapter.notifyDataSetChanged();

    }

    private void deleteSongs(int idSong) {
        database = Database.initDatabase(this, DATABASE_NAME);
        int rowsAffected = database.delete("baihat", "id_song = ?", new String[]{idSong + ""});
        if (rowsAffected > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Thành công")
                    .setMessage("Bài hát đã được xóa thành công.")
                    .setPositiveButton("OK", null)
                    .show();
            readData();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Lỗi")
                    .setMessage("Không thể xóa bài hát. Vui lòng thử lại.")
                    .setPositiveButton("OK", null)
                    .show();
        }
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
            Intent intent= new Intent(Songs.this, genres.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(Songs.this, About.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.musicBar)
        {
            Intent intent= new Intent(Songs.this, Songs.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settingBar)
        {
            Intent intent= new Intent(Songs.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);

    }


}