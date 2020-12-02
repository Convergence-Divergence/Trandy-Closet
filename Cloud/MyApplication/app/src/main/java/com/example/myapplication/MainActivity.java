package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity<ApolloException, CreateTextInput, CreateTextMutation> extends AppCompatActivity {

    ListView mListView;
    Button mButton;
    EditText mEditText;

    private ArrayList<ListTextsQuery.Item> mText;
    private ArrayAdapter<ListTextsQuery.Item> Adapter;
    private final String TAG = MainActivity.class.getSimpleName();
    private Object AppSyncResponseFetchers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.TestList);
        mButton = findViewById(R.id.addText);
        mEditText = findViewById(R.id.editText);

        ClientTest.init(this);

        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                save();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                query();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Query list data when we start application
        query();
    }

    public void query() {
        ClientTest.appSyncClient().query(ListTextsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListTextsQuery.Data> queryCallback = new GraphQLCall.Callback<ListTextsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTextsQuery.Data> response) {

            mText = new ArrayList<>(response.data().listTexts().items());

            Log.i(TAG, "Retrieved list items: " + mText.toString());

            Adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mText);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListView.setAdapter(Adapter);
                    Toast.makeText(MainActivity.this, "Print text", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    private void save() {
        final String text = mEditText.getText().toString();

        CreateTextInput input = CreateTextInput.builder().text(text).build();
        CreateTextMutation addTextMutation = CreateTextMutation.builder().input(input).build();

        ClientTest.appSyncClient().mutate(addTextMutation).enqueue(mutateCallback);

        mEditText.setText("");
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateTextMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateTextMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateTextMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Added text", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddTextMutation", e);
                    Toast.makeText(MainActivity.this, "Failed to add text", Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
            });
        }
    };
}