package com.example.wk01h02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewResult;

    EditText usernameField;
    EditText emailField;
    Button loginButton;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewResult = findViewById(R.id.text_view_result);
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        loginButton = findViewById(R.id.loginButton);

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getUsers();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = usernameField.getText().toString();
                String emailInput = emailField.getText().toString();

                getUserByCredentials(usernameInput, emailInput);
            }
        });
    }

    private void getUsers() {
        Call<List<User>> call = jsonPlaceHolderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<User> users = response.body();

                for (User user : users) {
                    String content = "";
                    content += "ID: " + user.getId() + "\n";
                    content += "Name: " + user.getName() + "\n";
                    content += "Username: " + user.getUsername() + "\n";
                    content += "Email: " + user.getEmail() + "\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getUserByCredentials(String username, String email) {
        Call<List<User>> call = jsonPlaceHolderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<User> users = response.body();

                for (User user : users) {
                    Log.d("Hello",user.getUsername() + ", " + user.getEmail());
                    if(user.getUsername().equals(username) && user.getEmail().equals(email)) {
                        Intent intent = MainActivity.newIntent(LoginActivity.this, user.getId());
                        startActivity(intent);
                    }
                }
                Log.d("Hello","HIHI " + username + ", " + email);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}