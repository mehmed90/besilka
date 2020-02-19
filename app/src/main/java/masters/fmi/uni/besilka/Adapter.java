package masters.fmi.uni.besilka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Word> wordList;

    public Adapter(Context context, ArrayList<Word> wordList){
        this.context=context;
        this.wordList=wordList;

    }
    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int i) {
        return wordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.word_item,null);
        }

        TextView user_sender=view.findViewById(R.id.TV_user_sender);
        TextView user_receiver=view.findViewById(R.id.TV_user_receiver);
        TextView status=view.findViewById(R.id.TV_status);

        user_sender.setText(wordList.get(i).getUsersender());
        user_receiver.setText(wordList.get(i).getUserreceiver());
        status.setText(wordList.get(i).getStatus());
        return view;
    }
}
