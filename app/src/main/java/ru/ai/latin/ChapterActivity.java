package ru.ai.latin;
/**
 *  * @author Sergey Grushin <sgrushin70@gmail.com>
 *  * @version     1.0
 *  * @since
 * <p>
 * MVC Controller class. Activity with ChapterList
 *
 * </p>
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    ListView chaptersList;
    List<String> chapters;
    int id_book;

    VocabularyModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        chaptersList = findViewById(R.id.ListChapters);
        Intent intent = getIntent();
        id_book= intent.getIntExtra("id_book", 0);
        //controller = new VocabularyController(this);
        model = new VocabularyModel(this);
        chapters = model.getChapters(id_book);
        chaptersList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,chapters.toArray(new String[] {}) ));
        chaptersList.invalidateViews();

        this.chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Study.class);
                intent.putExtra("id_book", id_book);
                intent.putExtra("id_chapter", position);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStop (){
        model.stop();
        super.onStop();
    }
}
