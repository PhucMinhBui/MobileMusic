package vn.edu.stu.music;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import vn.edu.stu.music.adapter.BaihatAdapter;
import vn.edu.stu.music.dao.Database;
import vn.edu.stu.music.model.BaiHat;
import vn.edu.stu.music.model.Genres;

public class EditSongActivity extends AppCompatActivity {
    final String DATABASE_NAME = "dbbaihat.sqlite";
    SQLiteDatabase database;
    Button btnChupHinh, btnChonHinh, btnUpdateSong, btnHuy, btnAddSong;
    EditText edtIdSong, edtNameSong, edtAuthorSong, edtGenres, edtTimeSong;
    ImageView imgSongUpdate;
    ArrayList<BaiHat> dsBaihat;
    BaihatAdapter adapter;
    int id_Song;
    final int REQUEST_TAKE_PHOTO=123;
    final int REQUEST_CHOOSE_PHOTO=321;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_song);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbarUpdate;
        toolbarUpdate = findViewById(R.id.toolBarUpdate);
        setSupportActionBar(toolbarUpdate);
        addControls();
        readData();
        intiUI();
        addEvents();
    }

    private void intiUI() {
        Intent intent = getIntent();
        int id_song = intent.getIntExtra("id_Song", -1);
        if (id_song == -1) {
            Log.e("EditSongActivity", "Invalid song ID passed in Intent.");
            Toast.makeText(this, "ID bài hát không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM baihat WHERE id_song = ?", new String[]{id_song + ""});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        String author = cursor.getString(2);
        int id_genres = cursor.getInt(3);
        String time = cursor.getString(4);
        byte[] picture = cursor.getBlob(5);


        // Lấy thông tin từ bảng genres
        String genresName = "Không xác định";
        Cursor genresCur = database.rawQuery(
                "SELECT name FROM genres WHERE id_genres = ?",
                new String[]{String.valueOf(id_genres)}
        );

        if (genresCur != null && genresCur.moveToFirst()) {
            genresName = genresCur.getString(0); // Lấy tên thể loại
            genresCur.close();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        imgSongUpdate.setImageBitmap(bitmap);
        edtNameSong.setText(name);
        edtAuthorSong.setText(author);
        edtGenres.setText(genresName);
        edtTimeSong.setText(time);
    }

    private void addControls() {
        btnChonHinh = findViewById(R.id.btnChonHinh);
        btnChupHinh = findViewById(R.id.btnChupHinh);
        btnUpdateSong = findViewById(R.id.btnUpdateSong);
        btnHuy = findViewById(R.id.btnHuy);
        btnAddSong=findViewById(R.id.btnAddMusic);
        edtIdSong = findViewById(R.id.edtIdSong);
        edtNameSong = findViewById(R.id.edtNameSong);
        edtAuthorSong = findViewById(R.id.edtAuthorSong);
        edtGenres = findViewById(R.id.edtGenres);
        edtTimeSong = findViewById(R.id.edtTimeSong);
        imgSongUpdate = findViewById(R.id.imgSongUpdate);
    }
    private void addEvents() {
        btnUpdateSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_song = edtIdSong.getText().toString();
                String name_song = edtNameSong.getText().toString();
                String author = edtAuthorSong.getText().toString();
                String genres_name = edtGenres.getText().toString();
                String time = edtTimeSong.getText().toString();
                byte[] picture = getByteFromImageView(imgSongUpdate);

                database = Database.initDatabase(EditSongActivity.this, DATABASE_NAME);

                Cursor genresCur = database.rawQuery("SELECT id_genres FROM genres WHERE name=?", new String[]{genres_name});
                if (genresCur != null && genresCur.moveToFirst()) {
                    String id_genres = genresCur.getString(0);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name_song);
                    contentValues.put("author", author);
                    contentValues.put("id_genres", id_genres);
                    contentValues.put("time", time);
                    contentValues.put("picture", picture);

                    database.update("baihat", contentValues, "id_song = ?", new String[]{id_Song + ""});
                    Toast.makeText(EditSongActivity.this, getString(R.string.s_UpdateSuc), Toast.LENGTH_SHORT).show();

                } else {
                    new AlertDialog.Builder(EditSongActivity.this)
                            .setTitle(getString(R.string.s_error))
                            .setMessage(getString(R.string.s_GenresInvalid))
                            .setPositiveButton("OK", null)
                            .setCancelable(false)
                            .show();
                }
                if (genresCur != null) {
                    genresCur.close();
                }


            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddSong.setOnClickListener(v -> addSong());

        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

    }

    private void readData() {
        id_Song = getIntent().getIntExtra("id_Song", -1);
        if (id_Song == -1) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM baihat WHERE  id_song=?", new String[]{String.valueOf(id_Song)});
        if (cursor != null && cursor.moveToFirst()) {

            int id_song = cursor.getInt(0);
            String name = cursor.getString(1);
            String author = cursor.getString(2);
            int id_genres = cursor.getInt(3);
            String time = cursor.getString(4);
            byte[] picture = cursor.getBlob(5);

            edtIdSong.setText(id_song + "");
            edtNameSong.setText(name);
            edtAuthorSong.setText(author);
            edtTimeSong.setText(time);


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

            if (picture != null) {
                Bitmap imgSongBm = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                imgSongUpdate.setImageBitmap(imgSongBm);
            } else {

            }
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private byte[] getByteFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void addSong() {
        String name = edtNameSong.getText().toString().trim();
        String author = edtAuthorSong.getText().toString().trim();
        String genres_name = edtGenres.getText().toString().trim();
        String time = edtTimeSong.getText().toString().trim();
        byte[] picture = getByteFromImageView(imgSongUpdate);


        if (name.isEmpty() || author.isEmpty() || genres_name.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, getString(R.string.s_notnull), Toast.LENGTH_SHORT).show();
            return;
        }


        String sqlCheckGenres = "SELECT id_genres FROM genres WHERE name = ?";
        Cursor cursor = database.rawQuery(sqlCheckGenres, new String[]{genres_name});
        int id_genres;

        if (cursor.moveToFirst()) {
            // Nếu thể loại đã tồn tại, lấy id_genres
            id_genres = cursor.getInt(0);
        } else {

//            String sqlInsertGenres = "INSERT INTO genres (name) VALUES (?)";
//            database.execSQL(sqlInsertGenres, new Object[]{genres_name});
//
//
//            Cursor newCursor = database.rawQuery(sqlCheckGenres, new String[]{genres_name});
//            if (newCursor.moveToFirst()) {
//                id_genres = newCursor.getInt(0);
//            } else {
//                Toast.makeText(this, getString(R.string.s_error), Toast.LENGTH_SHORT).show();
//                return;
//            }
//            newCursor.close();
            Toast.makeText(this, "genres not found", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();


        String sqlInsertSong = "INSERT INTO baihat (name, author, id_genres, time, picture) VALUES (?, ?, ?, ?, ?)";
        database.execSQL(sqlInsertSong, new Object[]{name, author, id_genres, time, picture});


        Toast.makeText(this, getString(R.string.s_addsuccess), Toast.LENGTH_SHORT).show();


        edtNameSong.setText("");
        edtAuthorSong.setText("");
        edtGenres.setText("");
        edtTimeSong.setText("");


        readData();

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
            Intent intent= new Intent(EditSongActivity.this, genres.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(EditSongActivity.this, About.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.musicBar)
        {
            Intent intent= new Intent(EditSongActivity.this, Songs.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.settingBar)
        {
            Intent intent= new Intent(EditSongActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    private void takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto()
    {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            if(requestCode==REQUEST_CHOOSE_PHOTO)
            {

                try {
                    Uri imgUri= data.getData();
                    InputStream is = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    imgSongUpdate.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else if(requestCode==REQUEST_TAKE_PHOTO)
            {
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                imgSongUpdate.setImageBitmap(bitmap);
            }
        }
    }
}