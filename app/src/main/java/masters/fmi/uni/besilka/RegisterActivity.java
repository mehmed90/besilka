package masters.fmi.uni.besilka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText reg_userET;
    EditText reg_passwordET;
    EditText reg_repeatPasswordET;
    Button okButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        reg_userET=findViewById(R.id.usernameEditText);
        reg_passwordET=findViewById(R.id.passwordEditText);
        reg_repeatPasswordET=findViewById(R.id.secondPasswordEditText);
        okButton=findViewById(R.id.okButton);
        cancelButton=findViewById(R.id.cancelButton);

        okButton.setOnClickListener(onClick);
        cancelButton.setOnClickListener(onClick);

    }

    View.OnClickListener onClick= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId())
            {
                case R.id.okButton:

                    if(reg_userET.getText().length() == 0 ||
                            reg_passwordET.getText().length() == 0 ||
                            !reg_passwordET.getText().toString().
                                    equals(reg_repeatPasswordET.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        return;
                    }
                    User user = new User( reg_userET.getText().toString(),
                            reg_passwordET.getText().toString());
                    new RegisterAsyncTask(user).execute();

                case R.id.cancelButton:
                    goToLogin();
                    break;
            }

        }
    };

    private void goToLogin() {
        Intent intent = new Intent(
                RegisterActivity.this, MultiplayerActivity.class);
        startActivity(intent);
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void>
    {

        User user;
        ProgressDialog dialog;
        boolean success = false;

        RegisterAsyncTask(User user){
            this.user = user;
            dialog = new ProgressDialog(RegisterActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Регистриране...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.format(
                    "http://android-projects.000webhostapp.com/besilka/register_user_besilka.php/?username=%s&password=%s"
                    , user.getUsername(), user.getPassword());

            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedInputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();

                if(line != null && line.contains("true")){
                    success = true;
                }else{
                    success = false;
                }

            }catch(java.io.IOException e){
                Log.wtf("Error" , e.getMessage());
            }finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.hide();

            if(success){
                goToLogin();
            }else{
                Toast.makeText(RegisterActivity.this,
                        "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
