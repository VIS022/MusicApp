package com.example.loonietunes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;

import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        Dexter.withContext(this).withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.RECORD_AUDIO
                )

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
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
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String songName = (String) listView.getItemAtPosition(position);

                    startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                            .putExtra("songs", mySongs)
                            .putExtra("songname", songName)
                            .putExtra("pos", position)
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
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            // Log when getView is called
            Log.d("customAdapter", "getView called for position: " + position);

            View view = getLayoutInflater().inflate(R.layout.item_list, null);
            TextView txtSong = view.findViewById(R.id.txtSong);
            txtSong.setSelected(true);
            txtSong.setText(songItems[position]);
            return view;
        }
    }
}
