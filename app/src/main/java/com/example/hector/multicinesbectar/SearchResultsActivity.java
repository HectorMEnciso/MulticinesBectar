package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Hector on 03/04/2015.
 */
public class SearchResultsActivity extends Activity {

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
        }

@Override
protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        }

private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                /**
                 * Use this query to display search results like
                 * 1. Getting the data from SQLite and showing in listview
                 * 2. Making webrequest and displaying the data
                 * For now we just display the query only
                 */
                Log.e("query",query.toString());

        }
        }

}
