package com.example.android.petsshelter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.petsshelter.data.PetCursorAdapter;
import com.example.android.petsshelter.data.PetContract.PetEntry;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // To access our database, we instantiate our subclass of SQLiteOpenHelper
    // and pass the context, which is the current activity.
//    PetDbHelper mDbHelper;
//    SQLiteDatabase db;
    PetCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewPetIntent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(addNewPetIntent);
            }
        });

        ListView petListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        adapter = new PetCursorAdapter(this,null);
        petListView.setAdapter(adapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent editPetIntent = new Intent(CatalogActivity.this,EditorActivity.class);
                editPetIntent.setData(ContentUris.withAppendedId(PetEntry.CONTENT_URI,id));
                startActivity(editPetIntent);
            }
        });

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        LoaderManager.getInstance(this).initLoader(0, null, this);
        //mDbHelper = new PetDbHelper(this);
    }

//    /**
//     * Temporary helper method to display information in the onscreen TextView about the state of
//     * the pets database.
//     */
//    private void displayDatabaseInfo() {
//        // Create and/or open a database to read from it
//        //db = mDbHelper.getReadableDatabase();
//        //db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
//
//        String[] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_WEIGHT,
//                PetEntry.COLUMN_PET_GENDER};
////        String selection = PetEntry.COLUMN_PET_GENDER + "=?";
////        String[] selectionArgs = new String[] { String.valueOf(PetEntry.GENDER_FEMALE) };
//
//        Cursor cursor = getContentResolver().query(
//                PetEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//
////        try {
////            // Display the number of rows in the Cursor (which reflects the number of rows in the
////            // pets table in the database).
////            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
////            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
////            displayView.append(PetEntry._ID + " - " +
////                    PetEntry.COLUMN_PET_NAME +" - "+ PetEntry.COLUMN_PET_BREED +" - "+
////                    PetEntry.COLUMN_PET_GENDER +" - "+ PetEntry.COLUMN_PET_WEIGHT +"\n\n");
////
////            int idColumnindex = cursor.getColumnIndex(PetEntry._ID);
////            int nameColumnindex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
////            int breedColumnindex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
////            int genderColumnindex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
////            int weightColumnindex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
////
////            while(cursor.moveToNext())
////            {
////                displayView.append(
////                        cursor.getInt(idColumnindex) + " - " +
////                        cursor.getString(nameColumnindex) +" - "+
////                        cursor.getString(breedColumnindex) +" - "+
////                        cursor.getString(genderColumnindex) +" - "+
////                        cursor.getInt(weightColumnindex) +"\n");
////            }
////        } finally {
////            // Always close the cursor when you're done reading from it. This releases all its
////            // resources and makes it invalid.
////            cursor.close();
////        }
//    }

    /**
      A helper method to insert a pet in the pets database.
     */
    private void insertPet()
    {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
        //check if the dummy pet was inserted
        if(newUri == null)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.catalog_insert_dummy_failed),
                    Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), getString(R.string.catalog_insert_dummy_successful),
                    Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                if(getPetsCount() == 0)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.catalog_db_is_empty),
                            Toast.LENGTH_SHORT).show();
                }
                else
                    showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED};

        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);

        if(rowsDeleted == 0)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.catalog_delete_all_pets_failed),
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), getString(R.string.catalog_delete_all_pets_successful),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete all pets" button, so delete the pets.
                deleteAllPets();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    int getPetsCount()
    {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };
        return (getContentResolver().query(PetEntry.CONTENT_URI,projection,
                null,null,null).getCount());
    }

}