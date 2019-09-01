package ru.ai.latin;
/**
 *  * @author Sergey Grushin <sgrushin70@gmail.com>
 *  * @version     1.0
 *  * @since
 * <p>
 * MVC Controller class
 * </p>
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG ="RV";
    VocabularyModel model;
    ArrayList<String> books;
    private RecyclerView booksRecyclerView;
    private RecyclerView.Adapter bookAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets of Books
        this.model = new VocabularyModel(this);
        books = this.model.getBooks();
        booksRecyclerView = findViewById(R.id.booksRecycler);
        layoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(layoutManager);
        bookAdapter = new BookAdapter(books);
        ((BookAdapter) bookAdapter).setOnItemClickListener(new BookAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.v(TAG,"Click");
                Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
                intent.putExtra("id_book", position);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(int position, View view) {
                Log.v(TAG,"Long click");
            }
        });
        booksRecyclerView.setAdapter(bookAdapter);

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
        Boolean b = this.model.stop();
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
        int idMenu = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (idMenu ==R.id.load) {
            {//Load data from FireBase
                books.clear();
                books.add("Loading data");
                bookAdapter.notifyDataSetChanged();
                this.model.synchronizeDB(1,this);
                //TODO test and explore
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        books.clear();
                        books= model.getBooks();
                        if (books.size()>8) {
                            bookAdapter.notifyDataSetChanged();
                            handler.removeCallbacks(runnable);
                            Log.d("ML","runnable stop");
                        } else {
                            books.clear();
                            books.add("Loading data");
                            bookAdapter.notifyDataSetChanged();
                            Log.d("ML","runnable continue");
                            handler.postDelayed(runnable,100);
                        }
                    }
                };
                handler.postDelayed(runnable,100);
            }
            return true;
        } /* else if (idMenu == R.id.about) {
            return true;
        } else if (idMenu == R.id.action_settings) {
            return true;
        } */
        return super.onOptionsItemSelected(item);
    }
}


