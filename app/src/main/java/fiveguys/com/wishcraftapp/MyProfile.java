package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.*;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MyProfile extends AppCompatActivity {

    private  MediaPlayer mp;
    public static final int GET_FROM_GALLERY =20;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        profilePicture = (ImageView) findViewById(R.id.profilePic);

        final ListView listView = (ListView) findViewById(R.id._dynamic_myWishlist);

        String demo[] = {"Champagne Bottles Qt: 7", "Diamonds Qt: 7", "ATM machine Qt: 1",
                "Gold watch Qt: 1", "Gold chain Qt: 1", "Rings Qt: 7"};

        ArrayList<String> demoList = new ArrayList<>(Arrays.asList(demo));


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        this.mp = MediaPlayer.create(this, R.raw.rings);

    }

    public void onClickEditPic(View v) {
        Intent galleryImageGrab = new Intent(Intent.ACTION_PICK);
        File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath =  picDirectory.getPath();
        Uri picData = Uri.parse(picDirPath);
        galleryImageGrab.setDataAndType(picData,"image/*");
        startActivityForResult(galleryImageGrab, GET_FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == GET_FROM_GALLERY){
                //successfully heard from image gallery if here
                Uri imageUri = data.getData();
                InputStream imgStream;
                try {
                    imgStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
                    profilePicture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to Open Image",Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    public void playSong(View view){
        if(!this.mp.isPlaying()){
            this.mp.start();
        } else {
            this.mp.pause();
        }

    }


    public void addItemDialogue(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Item");
//        builder.setMessage("Item to add: ");
//        builder.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //code to run when ok is pressed
//                    }
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();

        Intent itemSearchIntent = new Intent(this, ItemSearch.class);
        startActivity(itemSearchIntent);

    }





}