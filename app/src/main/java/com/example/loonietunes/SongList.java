package com.example.loonietunes;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.util.ArrayList;

public class SongList extends AppCompatActivity {

    ListView listView;
    String[] songItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        listView = findViewById(R.id.listView);

        // Request runtime permissions before attempting to display songs
        runTimePermission();
    }

    public void runTimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // Display songs only after obtaining necessary permissions
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // Handle permission denied if needed
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            PermissionRequest permissionRequest,
                            PermissionToken permissionToken
                    ) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (File singleFile : files) {
                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
                        Log.d("SongList", "findSong - Directory: " + singleFile.getAbsolutePath());
                        arrayList.addAll(findSong(singleFile));
                    } else {
                        Log.d("SongList", "findSong - File: " + singleFile.getAbsolutePath());
                        if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                            arrayList.add(singleFile);
                        }
                    }
                }
            }
        }

        return arrayList;
    }

    void displaySong() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));

        if (mySongs != null && mySongs.size() > 0) {
            // Log the number of songs
            Log.d("SongList", "Number of songs: " + mySongs.size());

            for (File file : mySongs) {
                Log.d("SongList", "File Path: " + file.getAbsolutePath());
            }

            songItems = new String[mySongs.size()];
            for (int i = 0; i < mySongs.size(); i++) {
                songItems[i] = mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
            }

            // Log the number of items in the array
            Log.d("SongList", "Number of items in songItems: " + songItems.length);

            customAdapter customAdapter = new customAdapter();
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String songName = (String) listView.getItemAtPosition(i);

                    startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                            .putExtra("songs", mySongs)
                            .putExtra("songname", songName)
                            .putExtra("pos", i)
                    );
                }
            });
        } else {
            // Handle the case where no songs are found
            Log.d("SongList", "No songs found.");
        }
    }

    class customAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // Log the number of items
            Log.d("customAdapter", "Number of items: " + (songItems != null ? songItems.length : 0));
            return songItems != null ? songItems.length : 0;
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            // Log when getView is called
            Log.d("customAdapter", "getView called for position: " + i);

            View view = getLayoutInflater().inflate(R.layout.item_list, null);
            TextView txtSong = view.findViewById(R.id.txtSong);
            txtSong.setSelected(true);
            txtSong.setText(songItems[i]);
            return view;
        }
    }
}
