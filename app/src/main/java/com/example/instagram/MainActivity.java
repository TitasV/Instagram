package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.databinding.ActivityProfileBinding;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<String> users;
    ArrayAdapter<String> arrayAdapter;
    ActivityMainBinding binding;
    ArrayList<Bitmap> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();
        }
        getInstagramFeed();
    }
    public void getUserList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseUser user : objects){
                            users.add(user.getUsername().toString());
                        }
                        try {
                            myListView.setAdapter(arrayAdapter);
                        } catch (Exception h){
                            System.out.println("Mistake on setting empty list view");
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void goToProfile(View view){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void getInstagramFeed(){
        images = new ArrayList<>();
        users = new ArrayList<>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        images.add(bitmap);
                                        users.add(object.getString("username"));
                                    } else Log.i("Error", e.getLocalizedMessage());
                                    MyListViewAdapter myListViewAdapter = new MyListViewAdapter(MainActivity.this, images, users);
                                    binding.feedListView.setAdapter(myListViewAdapter);
                                }
                            });
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Empty feed", Toast.LENGTH_SHORT).show();
                    }
                } else Log.i("Error", e.getLocalizedMessage());
            }
        });
    }
}