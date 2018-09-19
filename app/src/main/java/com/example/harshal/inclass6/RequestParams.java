package com.example.harshal.inclass6;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Harshal on 2/19/2018.
 */

public class RequestParams {
    private HashMap<String,String> params;
    private StringBuilder stringBuilder;

    public RequestParams()
    {
        params = new HashMap<>();
        stringBuilder = new StringBuilder();

    }

    public RequestParams addParameter(String key, String value)
    {
        Log.d("demo","inside addParams");
        try {
            params.put(key, URLEncoder.encode(value,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  this ;
    }

    public  String getEncodedParameters()
    {
        for(String key:params.keySet())
        {
            if(stringBuilder.length()>0)
            {
                stringBuilder.append("&");
            }
            stringBuilder.append(key + "=" + params.get(key));
        }
        //Log.d("demo","stringbuilder value " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String getEncodedUrl (String url)
    {
        Log.d("demo", url + "?" + getEncodedParameters());
        return url + "?" + getEncodedParameters();

    }
}
