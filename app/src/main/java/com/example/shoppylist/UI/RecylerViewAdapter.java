package com.example.shoppylist.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppylist.Activities.DetailsActivity;
import com.example.shoppylist.Activities.ListActivity;
import com.example.shoppylist.Activities.MainActivity;
import com.example.shoppylist.Data.Databasehandler;
import com.example.shoppylist.Model.Grocery;
import com.example.shoppylist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {
    private Context context;
    private List<Grocery> groceryitems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecylerViewAdapter(Context context, List<Grocery> groceryitems) {
        this.context = context;
        this.groceryitems = groceryitems;
    }

    @NonNull
    @Override
    public RecylerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);


        return new ViewHolder(view , context);

    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryitems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dataadded.setText(grocery.getDateitemAdded());


    }

    @Override
    public int getItemCount() {
        return groceryitems.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName , quantity, dataadded;
        public Button editButton, deleteButton;
        public int id;



        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            dataadded = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //going to next screen - Details Activity
                    int position = getAdapterPosition();

                    Grocery grocery = groceryitems.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class); //used to display new activity, thus implementing context
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity" , grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateitemAdded());

                    context.startActivity(intent); //because this is not an activity, we have to use context to start activity

                }
            }); */

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryitems.get(position);

                    editItem(grocery);


                    break;

                case R.id.deleteButton:
                    position = getAdapterPosition();
                     grocery = groceryitems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }
        public void deleteItem(final int id){

            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog , null);
            Button noButton = (Button) view.findViewById(R.id.nobutton);
            Button yesButton = (Button) view.findViewById(R.id.yesbutton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
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
                    Databasehandler db = new Databasehandler(context);

                    db.deletegrocery(id); // removing from database
                    groceryitems.remove(getAdapterPosition()); // removing from recycler view
                    notifyItemRemoved(getAdapterPosition()); // notifies recycler view to change

                    dialog.dismiss();
                    if(groceryitems.size()==0)
                    {
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();

                    }

                }
            });

        }


        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryitem = (EditText) view.findViewById(R.id.groceryitem);
            final EditText quantinty = (EditText) view.findViewById(R.id.groceryQty);
            final TextView title = (TextView) view.findViewById(R.id.tile);
            Button savebutton = (Button) view.findViewById(R.id.saveButton);

            title.setText("Edit Grocery");
            groceryitem.setText(grocery.getName());
            quantinty.setText(grocery.getQuantity());
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            savebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Databasehandler db = new Databasehandler(context);

                    //update item
                    grocery.setName(groceryitem.getText().toString());
                    grocery.setQuantity(quantinty.getText().toString());

                    if(!groceryitem.getText().toString().isEmpty() && !quantinty.getText().toString().isEmpty())
                    {
                        db.updategrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery); // tells recycler view it has changed
                    }
                    else
                    {
                        Snackbar.make(view, "Add grocery and quantity" ,Snackbar.LENGTH_LONG ).show();
                    }
                    dialog.dismiss();


                }
            });
        }

    }
}
