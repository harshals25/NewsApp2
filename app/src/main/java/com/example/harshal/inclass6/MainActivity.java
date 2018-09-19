package com.example.harshal.inclass6;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Group 30
// InClass06
// Sunidhi Kabra, Harshal Sharma

public class MainActivity extends AppCompatActivity implements GetDataAsync.HandleData{

    static int count = 0;
    static ProgressDialog progressDialog;
    static int flag=1;
    static ArrayList<Articles> articles = new ArrayList<>();
    TextView keywordTextView  ;
    ImageView nextImageView ;
    ImageView previousImageView ;
    TextView textViewTotalNumber;
    TextView textViewTitle;
    TextView textViewPublishedAt;
    TextView textViewDescription;
    TextView textViewNumber;
    TextView textViewOutOf;
    ImageView imageView;
    String[] arr = {"Business" , "Entertainment", "General", "Health", "Science", "Sports", "Technology"};
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageViewNews);
        keywordTextView = findViewById(R.id.keywordTextView);
        nextImageView = findViewById(R.id.nextImageView);
        previousImageView = findViewById(R.id.previousImageView);
        progressDialog = new ProgressDialog(MainActivity.this);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPublishedAt = findViewById(R.id.textViewPublishedAt);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewTotalNumber = findViewById(R.id.textViewTotalNumber);
        textViewOutOf = findViewById(R.id.textViewOutOf);
        nextImageView.setEnabled(false);
        previousImageView.setEnabled(false);
        findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Select Keyword");
                    builder.setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            category = arr[i];
                            keywordTextView.setText(arr[i]);
                            RequestParams params = new RequestParams();
                            params
                                    .addParameter("category", category)
                                    .addParameter("country","us")
                                    .addParameter("apiKey" ,"063b6066bc8b4bc889e9a4285228096f")
                                    ;
                          new GetDataAsync(params,MainActivity.this).execute("https://newsapi.org/v2/top-headlines");

                        }
                    }).show();
                    count = 0;
                    nextImageView.setEnabled(true);
                    previousImageView.setEnabled(true);
                    if(flag==1)
                    {
                        nextImageView.setEnabled(false);
                        previousImageView.setEnabled(false);
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No internet Access", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                ++count;
                if(count <= articles.size()-1)
                {
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(articles.get(count).getTitle());
                    textViewPublishedAt.setText(articles.get(count).getPublishedAt());
                    Picasso.with(MainActivity.this).load(articles.get(count).getUrlToImage()).into(imageView);

                    textViewDescription.setText(articles.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(articles.size()));

                }
                else
                {
                    count=0;
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(articles.get(count).getTitle());
                    textViewPublishedAt.setText(articles.get(count).getPublishedAt());
                    Picasso.with(MainActivity.this).load(articles.get(count).getUrlToImage()).into(imageView);

                    textViewDescription.setText(articles.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(articles.size()));
                }
            }
        });

        previousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                --count;
                if(count >= 0)
                {
                    textViewOutOf.setVisibility(View.VISIBLE);
                    textViewTitle.setText(articles.get(count).getTitle());
                    textViewPublishedAt.setText(articles.get(count).getPublishedAt());
                    Picasso.with(MainActivity.this).load(articles.get(count).getUrlToImage()).into(imageView);
                    textViewDescription.setText(articles.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(articles.size()));
                }
                else
                {
                    count = articles.size() - 1;
                    textViewOutOf.setVisibility(View.VISIBLE);
                    count=articles.size()-1;
                    textViewTitle.setText(articles.get(count).getTitle());
                    textViewPublishedAt.setText(articles.get(count).getPublishedAt());
                    Picasso.with(MainActivity.this).load(articles.get(count).getUrlToImage()).into(imageView);
                    textViewDescription.setText(articles.get(count).getDescription());
                    textViewNumber.setText("" + (String.valueOf(count + 1)));
                    textViewTotalNumber.setText(String.valueOf(articles.size()));
                 }
            }
        });
    }

    private boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwi = cm.getActiveNetworkInfo();

        if(nwi == null || !nwi.isConnected() ||
                ((nwi.getType() != ConnectivityManager.TYPE_WIFI) &&
                        nwi.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            Toast.makeText(this, "No internet access", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void handledata(ArrayList<Articles> articles) {
        if(articles.size() < 2) {
            nextImageView.setEnabled(false);
            previousImageView.setEnabled(false);
        }
        else{
            nextImageView.setEnabled(true);
            previousImageView.setEnabled(true);
        }
        if(articles.size() != 0) {
            flag=0;
            MainActivity.articles = articles;
            textViewTitle.setText(articles.get(count).getTitle());
            textViewOutOf.setVisibility(View.VISIBLE);
            Picasso.with(MainActivity.this).load(articles.get(count).getUrlToImage()).into(imageView);
            textViewPublishedAt.setText(articles.get(count).getPublishedAt());
            textViewDescription.setText(articles.get(count).getDescription());
            textViewNumber.setText("" + (String.valueOf(count + 1)));
            textViewTotalNumber.setText(String.valueOf(articles.size()));
        }
        else{
            Toast.makeText(this, "No News Found", Toast.LENGTH_SHORT).show();
        }
    }
}
