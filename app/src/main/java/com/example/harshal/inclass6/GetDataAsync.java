package com.example.harshal.inclass6;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Harshal on 2/19/2018.
 */

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<Articles>> {
    RequestParams mParams;
    HandleData handleData;

    public GetDataAsync(RequestParams params, HandleData handleData) {
        this.handleData = handleData;
        mParams = params;
    }

    @Override
    protected ArrayList<Articles> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        ArrayList<Articles> result = new ArrayList<>();
        try {
            URL url = new URL(mParams.getEncodedUrl(params[0]));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                Log.d("demo", "Url in doinBackgrous" + IOUtils.toString(connection.getInputStream(), "UTF8"));
                Log.d("demo", "json = " + json);
                JSONObject root = new JSONObject(json);
                JSONArray articlesJsonArray = root.getJSONArray("articles");

                for (int i = 0; i < articlesJsonArray.length(); i++) {
                    JSONObject articlesJson = articlesJsonArray.getJSONObject(i);
                    Articles articles = new Articles();
                    articles.author = articlesJson.getString("author");
                    articles.title = articlesJson.getString("title");
                    if(articlesJson.getString("description") == "null"){
                        articles.description = "";
                    }
                    else{
                        articles.description = articlesJson.getString("description");
                    }
                    articles.url = articlesJson.getString("url");
                    articles.urlToImage = articlesJson.getString("urlToImage");
                    articles.publishedAt = articlesJson.getString("publishedAt");

                    JSONObject sourceJson = articlesJson.getJSONObject("source");
                    Source source = new Source();
                    source.id = sourceJson.getString("id");
                    source.name = sourceJson.getString("name");

                    articles.source = source;

                    result.add(articles);
                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }  finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        MainActivity.progressDialog.setMessage("Loading News");
        MainActivity.progressDialog.setCancelable(false);
        MainActivity.progressDialog.setMax(10000);
        MainActivity.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        MainActivity.progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Articles> result) {

        MainActivity.progressDialog.dismiss();
        if (result.size() > 0) {
            Log.d("demo", "JSON Parsing" + result.toString());
            handleData.handledata(result);
        } else {
            Log.d("demo", "nothing to be returned");
        }
//        if(result != null && !result.matches(""))
//        {
//            tempUrl = result;
//            //Log.d("demo","Url in OnPostExecute GetParamsUsingAsync " + tempUrl);
//            //new GetImageAsync(MainActivity.this , result, count).execute();
//            new GetImageAsync(MainActivity.this , tempUrl).execute();
//            //for(int i = 0; i<100000 ; i++){}
//            //progressDialog.dismiss();
//        }
//        else
//        {
//            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();
//            Log.d("demo","nothing to be returned GetDataUsingParamsAsync");
//            imageView.setImageDrawable(null);
//            progressDialog.dismiss();
//            nextImageView.setEnabled(false);
//            previousImageView.setEnabled(false);
//        }
    }
    public static interface HandleData
    {
        public void handledata(ArrayList<Articles> articles);
    }


}
