/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private EarthquakeAdapter mAdapter;

    private TextView empty_text_view;

    private ProgressBar progressBar;

    private ListView earthquakeListView;

    ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate được implement");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

//        // Create a fake list of earthquake locations.
////        final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
//
//
//
        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        earthquakeListView.setAdapter(mAdapter);
        empty_text_view = (TextView) findViewById(R.id.list_empty);
        earthquakeListView.setEmptyView(empty_text_view);
        final Intent i = new Intent(Intent.ACTION_VIEW);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake resUrl = mAdapter.getItem(position);
                String url = resUrl.getmUrl();
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
//        AsyncTaskEarthquake asyncTaskEarthquake = new AsyncTaskEarthquake();
//        asyncTaskEarthquake.execute(USGS_REQUEST_URL);

        Log.i(LOG_TAG,"initLoader được dùng ");

        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(0, null, this);
        }else{
            progressBar.setVisibility(GONE);
            empty_text_view.setText("No internet connection");
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

//        Log.i(LOG_TAG,"onCreateLoader đang được implement" );
//        progressBar.setVisibility(View.VISIBLE);
//        empty_text_view.setVisibility(GONE);
//        earthquakeListView.setVisibility(GONE);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));


        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);


        Log.i(LOG_TAG, "loi o day "+ uriBuilder.toString());
        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
            Log.i(LOG_TAG, " onLoadFiníhed đang được implement");
            empty_text_view.setText(R.string.no_earthquakes);

            progressBar.setVisibility(GONE);
            earthquakeListView.setVisibility(View.VISIBLE);

            mAdapter.clear();

            if (earthquakes !=null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, " onLoaderReset đang được implement");
        mAdapter.clear();
    }


//    class AsyncTaskEarthquake extends AsyncTask<String, Void, List<Earthquake> > {
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            if(urls.length < 1 || urls[0] == null) return null;
//
//            List<Earthquake> earthquakes = null;
//            earthquakes = QueryUtils.FetchEarthquakeData(urls[0]);
//
//            return earthquakes;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> earthquakes) {
//            if(earthquakes == null) {
//                return ;
//            }
//            super.onPostExecute(earthquakes);
//            updateUI(earthquakes);
//
//        }
//    }
//
//    public void updateUI(final List<Earthquake> earthquake) {
//
//        final ArrayList<Earthquake> earthquakes = (ArrayList<Earthquake>) earthquake;
//
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        // Create a new {@link ArrayAdapter} of earthquakes
//        EarthquakeAdapter adapter =new EarthquakeAdapter(EarthquakeActivity.this , earthquakes);
//
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        earthquakeListView.setAdapter(adapter);
//
//        final Intent i = new Intent(Intent.ACTION_VIEW);
//
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Earthquake resUrl = earthquakes.get(position);
//                String url = resUrl.getmUrl();
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
