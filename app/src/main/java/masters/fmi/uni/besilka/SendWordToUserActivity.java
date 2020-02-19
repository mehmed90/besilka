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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendWordToUserActivity extends AppCompatActivity {

    TextView sendToUser;
    EditText wordToSend;
    Button send;

    String usertosendword;
    String userwhosend;

    static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_word_to_user);

        sendToUser=findViewById(R.id.sendUserTV);
        wordToSend=findViewById(R.id.wordToSendET);
        send=findViewById(R.id.sendWordButton);


        user = (User) getIntent().getExtras().getSerializable("userwhosend");
        usertosendword = getIntent().getExtras().getString("usertosend");

        userwhosend=user.getUsername();

        sendToUser.setText("Изпращане дума на "+usertosendword);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(wordToSend.getText().toString().isEmpty() || wordToSend==null)

                    Toast.makeText(getApplicationContext(),"Не сте въвели дума за изпращане",Toast.LENGTH_LONG).show();
                else
                    new RegisterAsyncTask(userwhosend,usertosendword,wordToSend.getText().toString()).execute();
            }
        });

    }
    private void goToPrincipal() {
        Intent intent = new Intent(
                SendWordToUserActivity.this, PrincipalActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("check", "false");
        startActivity(intent);
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void>
    {

        String usertosendword;
        String userwhosend;
        String wordToSend;
        ProgressDialog dialog;
        boolean success = false;

        RegisterAsyncTask(String userwhosend,String usertosendword,String wordToSend){
            this.usertosendword = usertosendword;
            this.userwhosend=userwhosend;
            this.wordToSend=wordToSend;
            dialog = new ProgressDialog(SendWordToUserActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Изпращане на дума на потребителя...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.format(
                    "http://android-projects.000webhostapp.com/besilka/register_word_besilka.php/?usersender=%s&userreceiver=%s&word=%s"
                    , userwhosend, usertosendword,wordToSend);

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
                goToPrincipal();
            }else{
                Toast.makeText(SendWordToUserActivity.this,
                        "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
