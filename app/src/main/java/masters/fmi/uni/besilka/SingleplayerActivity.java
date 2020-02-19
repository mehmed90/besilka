package masters.fmi.uni.besilka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SingleplayerActivity extends AppCompatActivity {

    ArrayList<String> wordsToGues= new ArrayList<String>();
    ArrayList<String> alreadyInputLetterr = new ArrayList<String>();

    Button checkButton;
    Button restartButton;
    EditText inputWord;
    TextView wordToGues;
    TextView numberOfTry;
    TextView alreadyInput;
    int intents=5,countSelectedWord, n;
    String selectedWord;
    ImageView picture;
    char[] a;
    char[] ab;
    Random rand;
    int num=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        wordsToGues.add("софия");
        wordsToGues.add("пловдив");
        wordsToGues.add("телефон");
        wordsToGues.add("университет");
        wordsToGues.add("информатика");
        wordsToGues.add("математика");
        wordsToGues.add("програмиране");
        wordsToGues.add("гел");
        wordsToGues.add("бира");
        wordsToGues.add("водка");

        checkButton=findViewById(R.id.button_check);
        inputWord=findViewById(R.id.inputWord_ET);
        wordToGues=findViewById(R.id.TV_word_to_gues);
        numberOfTry=findViewById(R.id.TV_try);
        alreadyInput=findViewById(R.id.TV_alreadyInput);
        picture=findViewById(R.id.imageView);
        restartButton=findViewById(R.id.button_restart);

        SelectWordFromList();

      //  wordsAlreadyGets.add(wordsToGues.get(n));
      //  selectedWord=wordsToGues.get(n);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(inputWord.getText().toString().isEmpty() || inputWord==null)
                {
                    Toast.makeText(getApplicationContext(),"Не сте въвели нито една буква",Toast.LENGTH_LONG).show();
                }

                else
                {
                    char letter=inputWord.getText().charAt(0);
                    letter = Character.toLowerCase(letter);
                    if (!selectedWord.contains(String.valueOf(letter)) && !alreadyInputLetterr.contains(String.valueOf(letter)))
                    {
                        intents--;
                    }


                    if(intents==0)
                    {
                        picture.setImageResource(R.drawable.besilka6);
                        numberOfTry.setText("ЗАГУБИХТЕ!!!");
                        checkButton.setEnabled(false);
                        wordToGues.setText(selectedWord);
                        return;
                    }

                    switch (intents){
                        case 4:
                            picture.setImageResource(R.drawable.besilka2);
                            break;
                        case 3:
                            picture.setImageResource(R.drawable.besilka3);
                            break;
                        case 2:
                            picture.setImageResource(R.drawable.besilka4);
                            break;
                        case 1:
                            picture.setImageResource(R.drawable.besilka5);
                            break;
                    }
                    for(int i=0;i<selectedWord.length();i++)
                    {
                        if(ab[i]==letter && !alreadyInputLetterr.contains(String.valueOf(letter)))
                        {
                            a[i]=letter;
                            countSelectedWord++;
                        }

                    }

                    wordToGues.setText(String.valueOf(a));
                    inputWord.setText("");
                    numberOfTry.setText("Брой опити до края на играта "+intents);

                    if(!alreadyInputLetterr.contains(String.valueOf(letter)))
                    {
                        alreadyInputLetterr.add(String.valueOf(letter));
                        alreadyInput.setText(alreadyInputLetterr.toString());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Тази буква вече е въведохте единпът",Toast.LENGTH_LONG).show();
                    }
                    if(countSelectedWord==selectedWord.length())
                    {
                        numberOfTry.setText("ПОЗДРАВЛЕНИЯ, СПЕЧЕЛИХТЕ!");
                        checkButton.setEnabled(false);
                    }

                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intents=5;
                picture.setImageResource(R.drawable.besilka1);
                alreadyInputLetterr.clear();
                alreadyInput.setText(alreadyInputLetterr.toString());
                countSelectedWord=0;
                inputWord.setText("");
                checkButton.setEnabled(true);
                SelectWordFromList();

            }
        });
    }

    public void SelectWordFromList()
    {
        if(wordsToGues.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Вие изчерпахте думите от списъка",Toast.LENGTH_LONG).show();
        }
        else
        {
            int n;
            rand = new Random();
            n= rand.nextInt(num);
            // wordsAlreadyGets.add(wordsToGues.get(n));
            selectedWord=wordsToGues.get(n);
            wordsToGues.remove(wordsToGues.get(n));
            num--;


      /*  while(!wordsAlreadyGets.contains(selectedWord))
        {
            rand = new Random();
            n = rand.nextInt(10);
            wordsAlreadyGets.add(wordsToGues.get(n));
            selectedWord=wordsToGues.get(n);
        }*/
            a=selectedWord.toCharArray();
            ab=selectedWord.toCharArray();
            for(int i=0;i<selectedWord.length();i++)
            {
                a[i]='-';
            }
            wordToGues.setText(String.valueOf(a));
            numberOfTry.setText("Брой опити до края на играта "+intents);
        }



    }
}
