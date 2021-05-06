package com.example.myoriginalmalapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonQueue
{
    private static com.example.myoriginalmalapp.SingletonQueue instance;
    private static Context ctx;
    private RequestQueue requestQueue;

    private SingletonQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized com.example.myoriginalmalapp.SingletonQueue getSingleton(Context context)
    {
        if (instance == null)
        {
            instance = new com.example.myoriginalmalapp.SingletonQueue(context);
        }
        ctx = context;
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
