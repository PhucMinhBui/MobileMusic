package vn.edu.stu.music;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import vn.edu.stu.music.dao.Database;
import vn.edu.stu.music.model.BaiHat;
import vn.edu.stu.music.model.Genres;


public class genres extends AppCompatActivity {
    final String DATABASE_NAME="dbbaihat.sqlite";
    SQLiteDatabase database;
    ListView lvGenres;
    ArrayList<Genres> dsTheloai;
    ArrayAdapter adapter;
    Button btnAddGenres, btnDeleteGenres, btnUpdateGenres;
    EditText edtNameGenres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_genres);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        addControls();
        readData();
        addEvents();
    }



    private void addControls() {
        lvGenres=findViewById(R.id.lvGenres);
        dsTheloai=new ArrayList<>();
        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, dsTheloai);
        lvGenres.setAdapter(adapter);
        edtNameGenres=findViewById(R.id.edtNameGenres);
        btnAddGenres=findViewById(R.id.btnAddGenres);

        btnUpdateGenres=findViewById(R.id.btnUpdateGenres);
    }

    private void readData() {

        database = Database.initDatabase(this, DATABASE_NAME);

        Cursor cursor = database.rawQuery("SELECT * FROM genres", null);

        dsTheloai.clear();

        while (cursor.moveToNext()) {
            int id_genres = cursor.getInt(0);
            String name = cursor.getString(1);


            dsTheloai.add(new Genres(id_genres, name));
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }
    private void addEvents() {


        btnAddGenres.setOnClickListener(v -> addGenre());
        btnUpdateGenres.setOnClickListener(v -> updateGenre());

        // Sự kiện chọn thể loại từ danh sách
        lvGenres.setOnItemClickListener((parent, view, position, id) -> {
            selectedGenre = dsTheloai.get(position);
            edtNameGenres.setText(selectedGenre.getName());
        });

        lvGenres.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                xacnhanXoa(i);
                return true;
            }


        });
    }
    private Genres selectedGenre; // Khai báo biến selectedGenre

    private void xacnhanXoa(int i) {
        Genres genres= dsTheloai.get(i);
//         Hiển thị hộp thoại xác nhận xóa
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn xóa thể loại này?")
                .setNegativeButton("Không", null)
                .setPositiveButton("Có",(dialog,which)->{
                        // Kiểm tra xem thể loại có bài hát nào không
                        Cursor genresCur = database.rawQuery("SELECT * FROM baihat WHERE id_genres = ?", new String[]{String.valueOf(genres.getId_genres())});
                        if(genresCur.getCount()>0)
                        {
                            new android.app.AlertDialog.Builder(this)
                                    .setTitle("Thất bại")
                                    .setMessage("Không thể xóa. Thể loại đã chứa bài hát")
                                    .setPositiveButton("OK",null)
                                    .setCancelable(false)
                                    .show();
                        }
                        else{
                            xoaGenres(genres.getId_genres());
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void xoaGenres(int idGenres) {
        int row= database.delete("genres", "id_genres=?", new String[]{String.valueOf(idGenres)});
        if (row>0)
        {
            Toast.makeText(this,"Xóa thể loại thành công", Toast.LENGTH_SHORT).show();
            readData();
        }
        else
        {
            Toast.makeText(this,"Xóa thể loại thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void addGenre() {
        String name = edtNameGenres.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Tên thể loại không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        String sql = "INSERT INTO genres (name) VALUES (?)";
        database.execSQL(sql, new Object[]{name});

        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
        readData();
        edtNameGenres.setText("");
    }
    private void updateGenre() {
        if (selectedGenre == null) {
            Toast.makeText(this, "Vui lòng chọn thể loại cần sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtNameGenres.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Tên thể loại không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        String sql = "UPDATE genres SET name = ? WHERE id_genres = ?";
        database.execSQL(sql, new Object[]{name, selectedGenre.getId_genres()});

        Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
        readData();
        edtNameGenres.setText(""); // Xóa nội dung nhập
        selectedGenre = null; // Hủy chọn
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
        if (id == R.id.musicBar)
        {
            Intent intent= new Intent(genres.this, Songs.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(genres.this, About.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.genresBar)
        {
            Intent intent= new Intent(genres.this, genres.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settingBar)
        {
            Intent intent= new Intent(genres.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


}