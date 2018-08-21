package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BookContract.BookEntry;
import com.example.android.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new BookDbHelper(this);
        insertData("Book One", 1999, 5, "Books R Us", "1-800-867-5309");
        insertData("Book Two", 1999, 12, "Books R Us", "1-800-867-5309");
        insertData("Advanced Java", 6999, 1, "Learn 2 Program", "1-800-309-8675");
        queryData();
    }


    private void insertData(String name, int price, int quantity, String supplierName, String supplierPhone){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object mapping values to column names
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_PRODUCTNAME, name);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIERNAME, supplierName);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIERPHONE, supplierPhone);

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
    }

    private void queryData(){
        /**
         * Query the database and log the result
         */

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection of columns
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_PRODUCTNAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIERNAME,
                BookEntry.COLUMN_BOOK_SUPPLIERPHONE};

        // Perform a query on the pets table
        Cursor cursor = db.query(BookEntry.TABLE_NAME, projection,
                null, null, null, null,
                null, null);

        TextView displayView = (TextView) findViewById(R.id.main_text_view);

        try {
            // Create a header with total rows and column names
            displayView.setText(String.format(getString(R.string.book_count), cursor.getCount()));
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_PRODUCTNAME + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIERNAME + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIERPHONE + "\n");

            // Get column indexes
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRODUCTNAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIERNAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIERPHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Extract values with indices
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(productNameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

}

