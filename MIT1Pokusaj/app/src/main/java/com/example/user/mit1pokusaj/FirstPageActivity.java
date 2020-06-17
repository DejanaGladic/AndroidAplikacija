package com.example.user.mit1pokusaj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.user.mit1pokusaj.adapter.CategoryAdapter;
import com.example.user.mit1pokusaj.adapter.ProductAdapter;
import com.example.user.mit1pokusaj.models.Category;
import com.example.user.mit1pokusaj.models.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirstPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList=new ArrayList<>();

    private ArrayList<Category> mCategList;
    private CategoryAdapter categoryAdapter;

    private android.support.v7.widget.Toolbar mTopToolbar;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        mTopToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        progressBar=findViewById(R.id.progressBar);
        recyclerView = (RecyclerView)findViewById(R.id.RecView);

        showProgress();

        initList();

        Spinner spinnerCate = findViewById(R.id.spinner_categ);

        categoryAdapter = new CategoryAdapter(this, mCategList);
        spinnerCate.setAdapter(categoryAdapter);


        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgress();
                Category clickedItem = (Category) parent.getItemAtPosition(position);
                String clickedCategory = clickedItem.getName();
                getData(clickedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getData("mascara");
            }
        });


    }


    public void getData(String productType){
        Api api = ApiClient.getApiClient().create(Api.class);

        Call<List<Product>> call = api.getProducts(productType);


        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                if(response.isSuccessful() && response.body()!=null){

                    hideProgress();

                    productList = response.body();
                    Log.i("Proradi",productList.get(0).getName());

                    recyclerView.setLayoutManager(new LinearLayoutManager(FirstPageActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //napunjenu listu prosledjujemo adapteru koji ce puniti recview
                    productAdapter = new ProductAdapter(productList);
                    recyclerView.setAdapter(productAdapter);

                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                hideProgress();

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showProgress(){
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initList() {
        mCategList = new ArrayList<>();
        mCategList.add(new Category(getResources().getString(R.string.mascara),R.drawable.mascara));
        mCategList.add(new Category(getResources().getString(R.string.eyeliner),R.drawable.eyeliner));
        mCategList.add(new Category(getResources().getString(R.string.lipstick),R.drawable.lipstick));
        mCategList.add(new Category(getResources().getString(R.string.blush),R.drawable.blush));
        mCategList.add(new Category(getResources().getString(R.string.foundation),R.drawable.foundation));
        mCategList.add(new Category(getResources().getString(R.string.bronzer),R.drawable.bronzer));
        mCategList.add(new Category(getResources().getString(R.string.eyebrow),R.drawable.eyebrow));
        mCategList.add(new Category(getResources().getString(R.string.eyeshadow),R.drawable.eyeshadow));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeM:
                Intent intToHome = new Intent(FirstPageActivity.this, HomeActivity.class);
                startActivity(intToHome);
                return true;
            case R.id.logoutM:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(FirstPageActivity.this, MainActivity.class);
                startActivity(intToMain);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
