package com.example.shoppylist.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.shoppylist.Data.Databasehandler;
import com.example.shoppylist.Model.Grocery;
import com.example.shoppylist.UI.RecylerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.shoppylist.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecylerViewAdapter recylerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems; //will hold
    private Databasehandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button clearButton;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();
                createPopDialog();
            }
        });

        db = new Databasehandler(this);
        clearButton = (Button) findViewById(R.id.clearALLbuttonID);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewid);
        recyclerView.setHasFixedSize(true); // to make all items fixed correctly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        //getting items from database
        groceryList = db.getALLGroceries();

        for (Grocery C : groceryList) {
            Grocery grocery = new Grocery();
            grocery.setName(C.getName());
            grocery.setQuantity(C.getQuantity());
            grocery.setId(C.getId());
            grocery.setDateitemAdded("Added on: " + C.getDateitemAdded());


            listItems.add(grocery);
        }

        recylerViewAdapter = new RecylerViewAdapter(this, listItems);
        recyclerView.setAdapter(recylerViewAdapter);
        recylerViewAdapter.notifyDataSetChanged(); // device gets ready

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteALL();

            }
        });

    }

    public void deleteALL() {
        dialogBuilder= new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.confirmation_dialog , null);
        Button noButton = (Button) view.findViewById(R.id.nobutton);
        Button yesButton = (Button) view.findViewById(R.id.yesbutton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();


        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleting item
                for (Grocery C : groceryList) {
                    db.deletegrocery(C.getId());
                }
                dialog.dismiss();

                Intent i = new Intent(ListActivity.this, MainActivity.class);
                startActivity(i);


            }
        });

    }


        private void createPopDialog(){

            dialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.popup, null);
            groceryItem = (EditText) view.findViewById(R.id.groceryitem);
            quantity = (EditText) view.findViewById(R.id.groceryQty);
            saveButton = (Button) view.findViewById(R.id.saveButton);


            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveGroceryToDB(v);
                }
            });


        }

        private void saveGroceryToDB (View v) {

            Grocery grocery = new Grocery();

            String newGrocery = groceryItem.getText().toString();
            String newGroceryQuantity = quantity.getText().toString();

            grocery.setName(newGrocery);
            grocery.setQuantity(newGroceryQuantity);

            //Save to DB
            db.addgrocery(grocery);

            Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

            // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    //start a new activity
                    startActivity(new Intent(ListActivity.this, ListActivity.class));
                    finish();
                }
            }, 1200); //  1 second.

    }

}