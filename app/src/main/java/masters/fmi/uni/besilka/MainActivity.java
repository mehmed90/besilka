package masters.fmi.uni.besilka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button multiplayer;
    Button singleplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiplayer = findViewById(R.id.id_button_multy);
        singleplayer=findViewById(R.id.id_button_single);

        multiplayer.setOnClickListener(onClick);
        singleplayer.setOnClickListener(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = null;

            switch(view.getId()){

                case R.id.id_button_single:
                    intent = new Intent(MainActivity.this, SingleplayerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.id_button_multy:
                    intent = new Intent(MainActivity.this, MultiplayerActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };
}
