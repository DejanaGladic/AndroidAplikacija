package com.example.user.mit1pokusaj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mit1pokusaj.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    TextView desc;
    TextView tvBrend;
    TextView tvPrice;
    TextView tvTag;
    ImageView img;
    ListView listView;
    CollapsingToolbarLayout collapsingBar;
    Product product;
    FloatingActionButton actionButton;
    Toolbar mTopToolbar;

    TextView tvComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTopToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        product=(Product)getIntent().getSerializableExtra("product");

        //comments
        tvComments=findViewById(R.id.tvComments);
        getCommentsCount(product.getId().toString());
        tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intComment = new Intent(DetailsActivity.this, CommentActivity.class);
                intComment.putExtra("productId",product.getId().toString());
                startActivity(intComment);
            }
        });

        //naziv
        collapsingBar=findViewById(R.id.collapsBar);
        collapsingBar.setTitle(product.getName());
        collapsingBar.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryText));


        //brend
        tvBrend=findViewById(R.id.tvBrand);
        tvBrend.setText("Brand: "+product.getBrand());

        //price
        tvPrice=findViewById(R.id.tvPrice);
        if(product.getPriceSign()!=null)
            tvPrice.setText("Price: "+product.getPrice()+product.getPriceSign());
        else
            tvPrice.setText("Price: "+product.getPrice());

        //tags
        ArrayList<String>listTags= new ArrayList<String>();
        tvTag=findViewById(R.id.tvTag);
        if(!product.getTagList().isEmpty()) {
            tvTag.setText("Tag List: ");
            listTags.addAll(product.getTagList());
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(new ArrayAdapter<String>(
                    this, R.layout.tag_list_item, listTags));
        }else{
            tvTag.setText("Tag list: /");
        }

        //opis
        desc=findViewById(R.id.tvDesc);
        if(!product.getDescription().isEmpty())
            desc.setText("Description:\n"+product.getDescription());
        else
            desc.setText("This product doesnt have description");


        //slika
        img=findViewById(R.id.imageOfProduct);
        Picasso.Builder builder= new Picasso.Builder(DetailsActivity.this);
        builder.downloader(new OkHttp3Downloader(DetailsActivity.this));
        builder.build().load(product.getImageLink())
                .error(R.drawable.ic_launcher_background)
                .into(img);

        //send button
        actionButton=findViewById(R.id.actionButt);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,"Link for the product");
                    String body=product.getName()+"\n"+product.getProductLink()+"\n"+" Shared from Be Always Beautiful";
                    i.putExtra(Intent.EXTRA_TEXT,body);
                    startActivity(Intent.createChooser(i,"Share with: "));

                }catch(Exception e){
                    Toast.makeText(DetailsActivity.this,getResources().getString(R.string.share),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getCommentsCount(String productID){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("comments").child(productID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvComments.setText("View all "+dataSnapshot.getChildrenCount()+" comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailsActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
