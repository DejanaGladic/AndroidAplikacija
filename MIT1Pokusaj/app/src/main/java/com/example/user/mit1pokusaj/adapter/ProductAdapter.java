package com.example.user.mit1pokusaj.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mit1pokusaj.DetailsActivity;
import com.example.user.mit1pokusaj.FirstPageActivity;
import com.example.user.mit1pokusaj.R;
import com.example.user.mit1pokusaj.models.Product;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    List<Product> ProductList;
    Context context;
    Product product;


    public ProductAdapter(List<Product>ProductList)
    {
        this.ProductList = ProductList;
    }

    @Override
    //postavlja cardview(stavke) na view holder da ni ovaj pristupio komponentama UI tog card view-a
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    //setuje reference pogleda view holdera i upravlja njima(cuva reference na onaj list item); poziva se za svaku stavkuiz liste
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, final int position) {
        product = ProductList.get(position);

        holder.nameProd.setText(product.getName());

        Picasso.Builder builder= new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(product.getImageLink())
                .into(holder.imgProduct);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("product",ProductList.get(position));
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgProduct;
        TextView nameProd;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imgProduct = (ImageView)itemView.findViewById(R.id.imgProduct);
            nameProd = (TextView)itemView.findViewById(R.id.prName);
            cv = (CardView)itemView.findViewById(R.id.cv);
        }

    }

}
