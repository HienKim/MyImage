package com.example.thuhuong.appmyimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class AlbumActivity extends AppCompatActivity implements Serializable {
    private Button btnLoginFB;
    private Button btnsaveAlbum;
    private ImageButton btnCreateAlbum;
    private ListView lvAlbum;
    private EditText edtNameAlbum;
    private TextView txtnameUser, txtemail;
    private ImageView profilePictureView;
    private ArrayList<String> listAlbumName;
    Account ac;
    int COMPARE = 0;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_album);
        Mapping();
        getDataOfIntent();
        edtNameAlbum.setVisibility(View.INVISIBLE);
        btnsaveAlbum.setVisibility(View.INVISIBLE);
        loadDatabase();
        if (COMPARE == 1) {
            mDatabase.child("Account").child(ac.getUsername()).setValue(ac);
        }


        onClickCreateAlbum();
        onClickSaveAlbum();
        onClickLogout();
    }

    private void onClickSaveAlbum() {
        btnsaveAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtNameAlbum.getText().toString();
                ArrayList<Image> listImage = new ArrayList<Image>();
                Album album = new Album(name, listImage);
                album.setCreateTime();
                Toast.makeText(AlbumActivity.this, "Tạo album thành công", Toast.LENGTH_SHORT).show();
                ac.setAlbum(album);
                mDatabase.child("Account").child(ac.getId()).setValue(ac);
            }
        });

    }


    private void onClickLogout() {
        btnLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AlbumActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDatabase() {
        loadNodeUser();
        loadNodeAlbum();

    }

    private void loadNodeAlbum() {

    }

    private void loadNodeUser() {
        mDatabase.child("Account").child(ac.getId()).child("Album").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickCreateAlbum() {
        btnCreateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNameAlbum.setVisibility(View.VISIBLE);
                btnsaveAlbum.setVisibility(View.VISIBLE);
            }
        });
    }


    private void Mapping() {
        profilePictureView = (ImageView) findViewById(R.id.imageProfilePictureView);
        btnCreateAlbum = (ImageButton) findViewById(R.id.imgBtnCreateAlbum);
        btnsaveAlbum = (Button) findViewById(R.id.btnSaveAlbum);
        lvAlbum = (ListView) findViewById(R.id.lvAlbum);
        edtNameAlbum = (EditText) findViewById(R.id.edtNameAlbumNew);
        txtnameUser = (TextView) findViewById(R.id.tvName);
        txtemail = (TextView) findViewById(R.id.tvEmail);
        btnLoginFB = (Button) findViewById(R.id.btnLogout);

    }

    private void getDataOfIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Packet");

        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String uri = bundle.getString("uri");

        txtemail.setText(email);
        txtnameUser.setText(name);
        Picasso.with(AlbumActivity.this).load(uri).into(profilePictureView);
        if (email == null) {
            ac = new Account(name, name + "@gmail.com");
        } else {
            ac = new Account(name, email);
        }
        ac.setId(bundle.getString("id"));
    }
}
