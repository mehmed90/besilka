package masters.fmi.uni.besilka;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewChallengeActivity extends AppCompatActivity {

    ListView viewChallenge;
    Adapter adapter;

    ArrayList<Word> wordsChallenge= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_challenge);

        new GetUsersAsyncTask().execute();

        adapter = new Adapter(this,wordsChallenge);

        viewChallenge=findViewById(R.id.listViewChallenge);

        viewChallenge.setAdapter(adapter);


    }


    private class GetUsersAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog = new ProgressDialog(ViewChallengeActivity.this);

        Word word;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Getting all challenges...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/view_challenge.php");
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

                        Word word = new Word(json.getString("name_user_sender"),json.getString("name_user_receiver"),
                                json.getString("status"));

                        wordsChallenge.add(word);
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
}
