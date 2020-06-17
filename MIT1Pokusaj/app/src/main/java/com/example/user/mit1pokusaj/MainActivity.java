package com.example.user.mit1pokusaj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mit1pokusaj.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText emailID,password, name, userName;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth=FirebaseAuth.getInstance();

        emailID=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        name=findViewById(R.id.editText4);
        userName=findViewById(R.id.editText5);

        tvSignIn=findViewById(R.id.textView);
        btnSignUp=findViewById(R.id.button);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=emailID.getText().toString();
                final String psw=password.getText().toString();
                final String fullName=name.getText().toString();
                final String usNam=userName.getText().toString();

                if(psw.isEmpty() && email.isEmpty() && fullName.isEmpty()&& usNam.isEmpty()){
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.empty),Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailID.setError(getResources().getString(R.string.enterSmt)+" email");
                    emailID.requestFocus();
                }
                else if(psw.isEmpty()){
                    password.setError(getResources().getString(R.string.enterSmt)+" password");
                    password.requestFocus();
                }
                else if(fullName.isEmpty()){
                    name.setError(getResources().getString(R.string.enterSmt)+" full name");
                    password.requestFocus();
                }
                else if(usNam.isEmpty()){
                    userName.setError(getResources().getString(R.string.enterSmt)+" username");
                    password.requestFocus();
                }
                else if(!(psw.isEmpty() && email.isEmpty() && fullName.isEmpty()&& usNam.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                User user=new User(
                                        fullName,
                                        usNam,
                                        email
                                );

                                Log.i("MyActivity",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"SignUp "+getResources().getString(R.string.unsuccess),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(MainActivity.this,"SignUp "+getResources().getString(R.string.unsuccess),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
