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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultiplayerActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        usernameET=findViewById(R.id.usernameET);
        passwordET=findViewById(R.id.passwordET);
        login=findViewById(R.id.loginButton);
        register=findViewById(R.id.registerButton);
        login.setOnClickListener(onClick);
        register.setOnClickListener(onClick);
    }

private View.OnClickListener onClick=new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.registerButton:
                intent = new Intent(MultiplayerActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                new LoginAsycnTask(username, password).execute();

                break;
        }

    }
};
    private class LoginAsycnTask extends AsyncTask<Void, Void, Void>
    {
        String username;
        String password;
        User user;
        ProgressDialog dialog;

        LoginAsycnTask(String username, String password){
            this.username = username;
            this.password = password;
            dialog = new ProgressDialog(MultiplayerActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Login... please wait.");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/login_user_besilka.php/?username=%s&password=%s",
                            username, password);
            HttpURLConnection urlConnection = null;

            try{
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(
                        urlConnection.getInputStream());
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();

                if(line != null){

                    JSONObject object = new JSONObject(line);
                    user = new User();
                    user.setUsername(username);
                    user.setId(object.getInt("id"));
                    user.setPassword(password);


                }else{
                    user = null;
                }


            }catch (IOException e){
                Log.wtf("Error", e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(user != null){

                Intent intent = new Intent(MultiplayerActivity.this, PrincipalActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                dialog.hide();
                //Toast.makeText(MultiplayerActivity.this, "Login succesfull "+user.getId(),
                 //       Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MultiplayerActivity.this, "Wrong username or password",
                        Toast.LENGTH_LONG).show();
                dialog.hide();
            }
        }



    }
}
