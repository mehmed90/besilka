package masters.fmi.uni.besilka;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MultiPlayerGameActivity extends AppCompatActivity {

    ArrayList<String> alreadyInputLetterr = new ArrayList<String>();

    Button checkButton_M;
    EditText inputWord_M;
    TextView wordToGues_M;
    TextView numberOfTry_M;
    TextView alreadyInput_M;
    int intents_M=5,countSelectedWord_M;
    ImageView picture_M;
    char[] a_M;
    char[] ab_M;
    Word word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_game);

        checkButton_M=findViewById(R.id.button_check_M);
        inputWord_M=findViewById(R.id.inputWord_ET_M);
        wordToGues_M=findViewById(R.id.TV_word_to_gues_M);
        numberOfTry_M=findViewById(R.id.TV_try_M);
        alreadyInput_M=findViewById(R.id.TV_alreadyInput_M);
        picture_M=findViewById(R.id.imageView_M);

        word = (Word) getIntent().getExtras().getSerializable("word");

        a_M=word.getWord().toCharArray();
        ab_M=word.getWord().toCharArray();
        for(int i=0;i<word.getWord().length();i++)
        {
            a_M[i]='-';
        }
        wordToGues_M.setText(String.valueOf(a_M));
        numberOfTry_M.setText("Брой опити до края на играта "+intents_M);

        checkButton_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputWord_M.getText().toString().isEmpty() || inputWord_M==null)
                {
                    Toast.makeText(getApplicationContext(),"Не сте въвели нито една буква",Toast.LENGTH_LONG).show();
                }
                else
                {
                    char letter=inputWord_M.getText().charAt(0);
                    letter = Character.toLowerCase(letter);
                    if (!word.getWord().contains(String.valueOf(letter)) && !alreadyInputLetterr.contains(String.valueOf(letter)))
                    {
                        intents_M--;
                    }


                    if(intents_M==0)
                    {
                        picture_M.setImageResource(R.drawable.besilka6);
                        numberOfTry_M.setText("ЗАГУБИХТЕ!!!");
                        new UpdateStatus(word.getId(),"Обесен").execute();
                        wordToGues_M.setText(word.getWord());
                        checkButton_M.setEnabled(false);
                        return;
                    }

                    switch (intents_M){
                        case 4:
                            picture_M.setImageResource(R.drawable.besilka2);
                            break;
                        case 3:
                            picture_M.setImageResource(R.drawable.besilka3);
                            break;
                        case 2:
                            picture_M.setImageResource(R.drawable.besilka4);
                            break;
                        case 1:
                            picture_M.setImageResource(R.drawable.besilka5);
                            break;
                    }
                    for(int i=0;i<word.getWord().length();i++)
                    {
                        if(ab_M[i]==letter  && !alreadyInputLetterr.contains(String.valueOf(letter)))
                        {
                            a_M[i]=letter;
                            countSelectedWord_M++;
                        }

                    }

                    wordToGues_M.setText(String.valueOf(a_M));
                    inputWord_M.setText("");
                    numberOfTry_M.setText("Брой опити до края на играта "+intents_M);

                    if(!alreadyInputLetterr.contains(String.valueOf(letter)))
                    {
                        alreadyInputLetterr.add(String.valueOf(letter));
                        // alreadyInput.setText(alreadyInputLetter);
                        alreadyInput_M.setText(alreadyInputLetterr.toString());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Тази буква вече е въведохте единпът",Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(),word.getWord().length()+"",Toast.LENGTH_LONG);
                    if(countSelectedWord_M==word.getWord().length())
                    {
                        numberOfTry_M.setText("ПОЗДРАВЛЕНИЯ, СПЕЧЕЛИХТЕ!");
                        new UpdateStatus(word.getId(),"Отгатнал").execute();
                        checkButton_M.setEnabled(false);
                    }
                }
            }
        });
    }

    private class UpdateStatus extends AsyncTask<Void, Void, Void>
    {
        int id;
        String status;
        boolean success = false;

        UpdateStatus(int id, String status){
            this.id=id;
            this.status=status;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.
                    format("http://android-projects.000webhostapp.com/besilka/update_word_status.php/?id=%s&status=%s",
                            id,status);
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
