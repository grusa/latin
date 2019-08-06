package ru.ai.latin;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView booksList;
    VocabularyController controller;
    List<String> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.booksList = findViewById(R.id.listBooks);
        setSupportActionBar(toolbar);
        //Sets of Books
        this.controller = new VocabularyController(this);
        books = this.controller.getBooks();
        this.booksList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,books.toArray(new String[] {}) ));
        this.booksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
                    intent.putExtra("id_book", position);
                    startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void btStartClicked(View view){
        Toast.makeText(this,"START Learning",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,Study.class);
        startActivity(intent);

    }
    @Override
    public void onStop(){
        this.controller.close();
        super.onStop();
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id ==R.id.load) {
            {//Load data from FireBase
                Boolean loaded = this.controller.synchronizeDB(1);
                if (loaded) {
                    books.clear();
                    books=this.controller.getBooks();
                    books.no
                    this.booksList.setAdapter(new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,books.toArray(new String[] {}) ));

                    this.booksList.no
                    Toast.makeText(this,"Loaded",Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


