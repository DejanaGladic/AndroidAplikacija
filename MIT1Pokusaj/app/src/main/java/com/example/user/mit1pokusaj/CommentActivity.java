package com.example.user.mit1pokusaj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mit1pokusaj.adapter.CommentAdapter;
import com.example.user.mit1pokusaj.models.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    ImageView profilePics;
    TextView post;
    EditText addComment;

    RecyclerView recViewComment;
    CommentAdapter commAdapter;
    List<Comment> commList;
    FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();

    private android.support.v7.widget.Toolbar mTopToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mTopToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //punimo listu komentarima iz baze pa onda recyclerview
        commList=new ArrayList<Comment>();
        readComments(getIntent().getStringExtra("productId"));

        recViewComment = (RecyclerView)findViewById(R.id.commRecView);
        recViewComment.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        recViewComment.setItemAnimator(new DefaultItemAnimator());
        commAdapter = new CommentAdapter(CommentActivity.this,commList,getIntent().getStringExtra("productId"));
        recViewComment.setAdapter(commAdapter);

        //postavljanje komentara
        profilePics=findViewById(R.id.profilePics);
        post=findViewById(R.id.tv_post);
        addComment=findViewById(R.id.add_comm);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this,"Empty comment cannot be posted",Toast.LENGTH_SHORT).show();
                }else{
                    addComment(getIntent().getStringExtra("productId"));
                }
            }
        });


    }

    private void addComment(String productId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("comments").child(productId);

        String commentId=reference.push().getKey();
        Comment comment=new Comment(addComment.getText().toString(),mFirebaseUser.getUid(),commentId);

        reference.child(commentId).setValue(comment);
        addComment.setText("");
    }

    private void readComments(String productID){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("comments").child(productID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //zato sto za svaki proizvod punimo njegove komentare
                commList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Comment comment=snapshot.getValue(Comment.class);
                    commList.add(comment);
                }
                commAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CommentActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
