package com.example.thuhuong.appmyimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class listImage extends AppCompatActivity {
    ListView lvPhoto;
    private Button btnLoginFB;
    private TextView txtnameUser, txtemail;
    private ImageView profilePictureView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<Image> arrayImage;
    DatabaseReference mDB;
    ImageAdapter adapter = null;
    Bundle bundle;
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://appmyimage.appspot.com");

    ArrayList<String> arrName = new ArrayList<String>();
    ArrayList<String> arrUrl = new ArrayList<String>();
    ArrayList<String> arrTime = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image);
        mDB = FirebaseDatabase.getInstance().getReference();

        Mapping();
        getDataOfIntent();
        arrayImage = new ArrayList<Image>();

        loadData();
        onClickLogout();

    }

    private void onClickLogout() {
        btnLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(listImage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        int size = arrName.size();
        Toast.makeText(this, "size "+size, Toast.LENGTH_SHORT).show();
        for(int i = 0; i < size; i++){
            String name = arrName.get(i);
            String url = arrUrl.get(i);
            String time = arrTime.get(i);
            arrayImage.add(new Image(name, url, time));
        }
        adapter = new ImageAdapter(this, R.layout.rowlayout, arrayImage);
        lvPhoto.setAdapter(adapter);


//        mDB.child("Image").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Image hinhAnh = (Image)dataSnapshot.getValue(Image.class);
//                arrayImage.add(new Image(hinhAnh.getName(), hinhAnh.getUrl(), hinhAnh.getTimeCreate()));
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }


    private void Mapping() {
        lvPhoto = (ListView) findViewById(R.id.lvHinhAnh);
        profilePictureView = (ImageView) findViewById(R.id.imageProfilePictureView);
        txtnameUser = (TextView) findViewById(R.id.tvName);
        txtemail = (TextView) findViewById(R.id.tvEmail);
        btnLoginFB = (Button) findViewById(R.id.btnLogout);

    }

    public void getDataOfIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Packet");
        Bundle bundle1 = intent.getBundleExtra("List");

        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String uri = bundle.getString("uri");

        arrName = bundle1.getStringArrayList("nameImg");
        arrUrl = bundle1.getStringArrayList("urlImg");
        arrTime = bundle1.getStringArrayList("timeImg");

        txtemail.setText(email);
        txtnameUser.setText(name);
        Picasso.with(listImage.this).load(uri).into(profilePictureView);
    }
}
