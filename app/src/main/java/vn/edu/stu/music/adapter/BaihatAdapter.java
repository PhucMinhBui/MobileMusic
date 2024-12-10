package vn.edu.stu.music.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.stu.music.EditSongActivity;
import vn.edu.stu.music.R;
import vn.edu.stu.music.model.BaiHat;

public class BaihatAdapter extends BaseAdapter {
    Activity context;
    ArrayList<BaiHat> list;
    ListView lvMusic;
    public BaihatAdapter(Activity context, ArrayList<BaiHat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= inflater.inflate(R.layout.custom_listview, null);
        ImageView imgSong= row.findViewById(R.id.imgSong);
        TextView txtId=row.findViewById(R.id.txtId);
        TextView txtName=row.findViewById(R.id.txtName);
        TextView txtAuthor=row.findViewById(R.id.txtAuthor);
        TextView txtGenres=row.findViewById(R.id.txtgenres);
        TextView txtTime=row.findViewById(R.id.txtTime);


        BaiHat baiHat=list.get(i);
        txtId.setText(baiHat.getId_song()+"");
        txtName.setText(baiHat.getName());
        txtAuthor.setText(baiHat.getAuthor());
        txtGenres.setText(baiHat.getId_genres()+"");
        txtTime.setText(baiHat.getTime());

        Bitmap bmSong= BitmapFactory.decodeByteArray(baiHat.getPicture(), 0 ,baiHat.getPicture().length);
        if (bmSong == null) {
            Log.e("BaiHatAdapter", "Bitmap is null");
        } else {
            Log.e("BaiHatAdapter", "Bitmap loaded successfully");
        }
        imgSong.setImageBitmap(bmSong);



        return row;
    }
}
