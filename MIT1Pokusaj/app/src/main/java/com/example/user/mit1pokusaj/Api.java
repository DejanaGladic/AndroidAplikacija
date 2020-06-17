package com.example.user.mit1pokusaj;

import com.example.user.mit1pokusaj.models.Product;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("products.json")
    Call<List<Product>> getProducts(
            @Query("product_type") String productType
    );
}
