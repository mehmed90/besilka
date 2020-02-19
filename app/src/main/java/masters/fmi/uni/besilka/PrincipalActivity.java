package masters.fmi.uni.besilka;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class PrincipalActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();


    static User user;
    ListView listUsers;
    ArrayAdapter adapter;
    Button viewChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        viewChallenge=findViewById(R.id.button);

        user = (User) getIntent().getExtras().getSerializable("user");
        String check=getIntent().getExtras().getString("check");

        if(check==null)
        {
            new CheckIfWordExist(user.getUsername()).execute();
        }

        listUsers = findViewById(R.id.listUsers);
        adapter = new ArrayAdapter(PrincipalActivity.this, android.R.layout.simple_list_item_1, users);

        listUsers.setAdapter(adapter);

        new GetUsersAsyncTask().execute();


        listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(PrincipalActivity.this, SendWordToUserActivity.class);
                intent.putExtra("usertosend", users.get(i));
                intent.putExtra("userwhosend",user);
                startActivity(intent);
            }
        });

        viewChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChallenge= new Intent(PrincipalActivity.this,ViewChallengeActivity.class);
                startActivity(intentChallenge);
            }
        });

    }


    private class GetUsersAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog = new ProgressDialog(PrincipalActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Getting all registered users...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/user_list_besilka.php?username=%s",
                            user.getUsername());
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedInputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();


                if (line != null) {
                    JSONArray array = new JSONArray(line);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        users.add(json.getString("username"));

                    }

                }

            } catch (java.io.IOException e) {
                Log.wtf("Error", e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.hide();

            adapter.notifyDataSetChanged();
        }


    }

    //get word from data base if exist

    private class CheckIfWordExist extends AsyncTask<Void, Void, Void>
    {
        Word word;
        String usernameReceiver;
        ProgressDialog dialog = new ProgressDialog(PrincipalActivity.this);

        CheckIfWordExist(String usernameReceiver){
            this.usernameReceiver=usernameReceiver;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/get_word.php/?username=%s",
                            usernameReceiver);
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
                    word = new Word(object.getString("name_user_sender"),object.getString("name_user_receiver"),
                            object.getString("status"));
                    word.setId(object.getInt("id"));
                  //  word.setUserreceiver(object.getString("name_user_receiver"));
                   // word.setUsersender(object.getString("name_user_sender"));
                    word.setWord(object.getString("word"));
                 //   word.setStatus(object.getString("status"));

                }else{
                    word = null;
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

            if(word!= null){

                AlertDialog.Builder checkWord= new AlertDialog.Builder(PrincipalActivity.this);
                checkWord.setMessage("Имате предизвикателство изпратено от "+word.getUsersender()+" искате ли да приемете").setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(PrincipalActivity.this, MultiPlayerGameActivity.class);
                                intent.putExtra("word",word);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Не", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int id=word.getId();
                                new RejectStatus(id).execute();
                                Toast.makeText(getApplicationContext(),"Вие отхвърлихте предизвикателството от "+word.getUsersender(),Toast.LENGTH_LONG).show();
                              //  dialog.hide();
                            }
                        });
                AlertDialog title =checkWord.create();
                title.setTitle("Предизвикателство");
                title.show();

            }
        }



    }

    //othvyrlqne na predizvikatelstvo
    private class RejectStatus extends AsyncTask<Void, Void, Void>
    {
        int id;
        boolean success = false;

        RejectStatus(int id){
            this.id=id;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/reject_word_status.php/?id=%s",
                            id);
            HttpURLConnection urlConnection = null;

            try{
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(
                        urlConnection.getInputStream());
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();

                if(line != null && line.contains("successful")){
                    success=true;

                }else{
                    success=false;
                }


            }catch (IOException e){
                Log.wtf("Error", e.getMessage());
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }
    }

}
