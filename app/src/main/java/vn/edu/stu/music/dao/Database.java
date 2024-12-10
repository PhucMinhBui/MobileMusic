package vn.edu.stu.music.dao;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database {
    public static SQLiteDatabase initDatabase(Activity activity, String databaseName)
    {
        try {
            String outFileName= activity.getApplicationInfo().dataDir + "/databases/" + databaseName;
            File f= new File(outFileName);
            if (!f.exists())
            {
                InputStream is = activity.getAssets().open(databaseName);
                File folder = new File(activity.getApplicationInfo().dataDir+ "/databases/");
                if (!folder.exists())
                {
                    folder.mkdir();
                }
                FileOutputStream myOutput= new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = is.read(buffer))> 0)
                {
                    myOutput.write(buffer, 0 ,length);
                }
                myOutput.flush();
                myOutput.close();
                is.close();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
        return activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    }

}
