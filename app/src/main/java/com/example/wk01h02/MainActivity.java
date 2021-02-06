package com.example.wk01h02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private TextView userHelloText;
    private TextView textViewResult;

    private static final String MAIN = "com.example.intents.main";

    private String loginUserID;

    public static Intent newIntent(Context packageContext, String text) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(MAIN,text);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userHelloText = findViewById(R.id.userHelloText);
        textViewResult = findViewById(R.id.text_view_result);

        loginUserID = getIntent().getStringExtra(MAIN);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> user = jsonPlaceHolderApi.getUserByID(loginUserID);
        Call<List<Post>> call = jsonPlaceHolderApi.getPostsByUserID(loginUserID);

        user.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> user, Response<List<User>> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<User> users = response.body();

                for(User usere : users) {
                    userHelloText.setText("Hello there " + usere.getName());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for(Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}