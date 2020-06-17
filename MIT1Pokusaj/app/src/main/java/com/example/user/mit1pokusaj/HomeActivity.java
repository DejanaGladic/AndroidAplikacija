package com.example.user.mit1pokusaj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.mit1pokusaj.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
    private android.support.v7.widget.Toolbar mTopToolbar;
    private ProgressBar progressBar;
    private TextView tv_welcome;
    private TextView tv_user;
    private Button btn_start;
    User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTopToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        progressBar=findViewById(R.id.progressBar);
        tv_welcome=findViewById(R.id.tv_welcome);
        tv_user=findViewById(R.id.tv_user);
        btn_start=findViewById(R.id.start_btn);

        showProgress();
        readUser();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToProd = new Intent(HomeActivity.this, FirstPageActivity.class);
                startActivity(intToProd);
            }
        });


    }

    private void readUser() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                tv_user.setText(user.getFullName()+",");
                tv_welcome.setText(getResources().getString(R.string.welcome));

                hideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
                tv_welcome.setText("Hi!");

                hideProgress();
            }
        });
    }

    private void showProgress() {
        tv_welcome.setVisibility(View.INVISIBLE);
        tv_user.setVisibility(View.INVISIBLE);
        btn_start.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        tv_welcome.setVisibility(View.VISIBLE);
        tv_user.setVisibility(View.VISIBLE);
        btn_start.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.productM:
                Intent intToProd = new Intent(HomeActivity.this, FirstPageActivity.class);
                startActivity(intToProd);
                return true;
            case R.id.logoutM:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intToMain);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
