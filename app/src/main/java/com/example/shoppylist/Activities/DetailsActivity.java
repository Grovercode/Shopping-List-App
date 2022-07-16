package com.example.shoppylist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shoppylist.Data.Databasehandler;
import com.example.shoppylist.Model.Grocery;
import com.example.shoppylist.R;
import com.example.shoppylist.UI.RecylerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView quantity, Dateadded, itemname;
    private int groceryID;

    private List<Grocery> groceryitems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Button editBUtton, deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemname = (TextView) findViewById(R.id.itemnameDetails);
        quantity = (TextView) findViewById(R.id.quantitydetails);
        Dateadded = (TextView) findViewById(R.id.dateaddedDetails);
        editBUtton = (Button) findViewById(R.id.editButtonDetails);
        deleteButton = (Button) findViewById(R.id.deleteButtonDetails);

        editBUtton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras(); // extra intent values

        if (bundle != null) {
            itemname.setText(bundle.getString("name"));

            quantity.setText(bundle.getString("quantity"));

            Dateadded.setText(bundle.getString("date"));

            groceryID = bundle.getInt("id");


        }


    }

    @Override
    public void onClick(View v) {
        Grocery grocery = new Grocery();
        grocery.setName(itemname.getText().toString());
        grocery.setQuantity(quantity.getText().toString());
        grocery.setDateitemAdded(Dateadded.getText().toString());
        grocery.setId(groceryID);

        switch (v.getId()) {

            case R.id.editButtonDetails:
                Log.d("clicked EDIT" , "YES");
                editItem(grocery);
                break;

            case R.id.deleteButtonDetails:

                break;
        }

    }


    public void editItem(final Grocery grocery){

        alertDialogBuilder = new AlertDialog.Builder(this);
        inflater = LayoutInflater.from(this);

        final View view = inflater.inflate(R.layout.popup, null);
        final EditText quantinty = (EditText) view.findViewById(R.id.groceryQty);
        final EditText groceryitem = (EditText) view.findViewById(R.id.groceryitem);
        final TextView title = (TextView) view.findViewById(R.id.tile);
        Button savebutton = (Button) view.findViewById(R.id.saveButton);

        title.setText("Edit Grocery");
        groceryitem.setText(grocery.getName());
        quantinty.setText(grocery.getQuantity().substring(4));
        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Databasehandler db = new Databasehandler(getApplicationContext());

                //update item
                grocery.setName(groceryitem.getText().toString());
                grocery.setQuantity(quantinty.getText().toString());

                Log.d("Quantity set to : " , grocery.getQuantity().toString());
                if(!groceryitem.getText().toString().isEmpty() && !quantinty.getText().toString().isEmpty())
                {
                    db.updategrocery(grocery);
                }
                else
                {
                    Snackbar.make(view, "Add grocery and quantity" ,Snackbar.LENGTH_LONG ).show();
                }

                dialog.dismiss();
                Intent i;
                i = new Intent(DetailsActivity.this, ListActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void deleteItem(final int id){


    }
}