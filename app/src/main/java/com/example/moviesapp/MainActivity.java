package com.example.moviesapp;

/*import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.RecycleView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyMovieData[] myMovieData= new MyMovieData[]{
                new MyMovieData("Game Of Thrones", "2011",R.drawable.gameOfThrones),
                new MyMovieData("Avatar", "2011",R.drawable.avatar),
                new MyMovieData("Ratatouille", "2011",R.drawable.ratatouille),
                new MyMovieData("The Hobbit", "2011",R.drawable.thehobbit),
                new MyMovieData("The Vikings", "2011",R.drawable.vikings),
                new MyMovieData("UP", "2011",R.drawable.up)
        };

        MyMovieAdapter ma = new MyMovieAdapter(myMovieData, MainActivity.this);
        rv.setAdapter(ma);
    }
}*/

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {
    private static final String TMDB_API_KEY = "bbbc3cc455c7f44ca0e576721e926a30";
    private static final String BASE_URL ="https://api.themoviedb.org/3/movie/popular";
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MyMovieAdapter myMovieAdapter;
    private EditText searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEditText = findViewById(R.id.editTextSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL + "?api_key=" + TMDB_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results =
                                    response.getJSONArray("results");
                            MyMovieData[] movies = new
                                    MyMovieData[results.length()];
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movieObject =
                                        results.getJSONObject(i);
                                int id = movieObject.getInt("id");//Add movie ID
                                String title = movieObject.getString("title");
                                String releaseDate =
                                        movieObject.getString("release_date");
                                String imageUrl =
                                        movieObject.getString("poster_path");
                                movies[i] = new MyMovieData(id,title,
                                        releaseDate, imageUrl);
                            }
                            myMovieAdapter = new MyMovieAdapter(movies,
                                    MainActivity.this);
                            recyclerView.setAdapter(myMovieAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error occurred: " + error.getMessage());
                    }
                });
        queue.add(jsonObjectRequest);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {
                // Not needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // Filter movie list based on search input
                if (myMovieAdapter != null) {
                    myMovieAdapter.getFilter().filter(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }
}
