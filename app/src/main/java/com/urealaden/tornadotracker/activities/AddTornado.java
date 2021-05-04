package com.urealaden.tornadotracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Tornado;
import com.urealaden.tornadotracker.R;

import javax.security.auth.login.LoginException;

public class AddTornado extends AppCompatActivity {

    public String TAG = "urealadenTornado.add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tornado);

        findViewById(R.id.submitTornadoButton).setOnClickListener(v -> {
            String name         = ((EditText) findViewById(R.id.editTextTornadoName)).getText().toString();
            String latitudeStr  = ((EditText) findViewById(R.id.editTextLatitude)).getText().toString();
            String longitudeStr = ((EditText) findViewById(R.id.editTextLongitude)).getText().toString();
            String category     = ((EditText) findViewById(R.id.editTextCategory)).getText().toString();

            double longitude = Double.parseDouble(longitudeStr);
            double latitude = Double.parseDouble(latitudeStr);

            Tornado tornado = Tornado.builder() // builder was created with the class when we ran 'codegen models'
                    .name(name)
                    .latitude(latitude)
                    .longitude(longitude)
                    .category(category)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(tornado),
                    response ->{
                        Log.i(TAG, "onCreate: successfully added");
                    },
                    response ->{
                        Log.i(TAG, "onCreate: add failed");
                    }
            );
        });
    }
}