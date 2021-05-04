package com.urealaden.tornadotracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Tornado;
import com.urealaden.tornadotracker.activities.AddTornado;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "urealadenTornado.main";
//    declare the string  joiner her and use that
//    Declare a list of tornados
    public List<Tornado> tornados = new ArrayList<>();

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Load and show Tornados
        Handler handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    StringJoiner sj = new StringJoiner(", ");
                    for(Tornado t: tornados){
                        sj.add(t.getName());
                    }
                    ((TextView) findViewById(R.id.allTheTornados)).setText(sj.toString());
                }
            }
        };
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }

        Amplify.API.query(
                ModelQuery.list(Tornado.class),
                response ->{
                    String x = "";
//                    StringJoiner sj = new StringJoiner(", ");
                    //Positive Response
                    for(Tornado t: response.getData()){
//                        Log.i(TAG, "Tornado: " + t.getName());
//                        sj.add(t.getName());
                        tornados.add(t);
                    }

                    handler.sendEmptyMessage(1); // all the tornados are loaded
                },
                // Negative response
                response -> Log.i(TAG,"retrievingTornados: " + response.toString())
        );

        ((Button) findViewById(R.id.addTornadoButton)).setOnClickListener(v ->{
            Intent intent = new Intent(this, AddTornado.class);
            startActivity(intent);
        });

    }
}