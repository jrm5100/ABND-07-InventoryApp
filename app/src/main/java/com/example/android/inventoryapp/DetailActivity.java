package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.BookContract;
import com.example.android.inventoryapp.data.BookContract.BookEntry;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the data loader */
    private static final int EXISTING_BOOK_LOADER = 0;

    /** Text views that get data added */
    TextView mNameText;
    TextView mPriceText;
    TextView mQuantityText;
    TextView mSupplierNameText;
    ImageButton phoneButton;

    /** Actual data values */
    String name;
    int price;
    int quantity;
    String supplierName;
    String supplierPhone;

    /** Content URI for the existing book (null if it's a new one) */
    private Uri mCurrentBookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize the data loader
        getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
    }


    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Get the Uri that was passed in
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // Define a projection that contains all columns from the table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIERNAME,
                BookEntry.COLUMN_BOOK_SUPPLIERPHONE};

        Log.v("TEST", mCurrentBookUri.toString());

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                this,       // Parent activity context
                mCurrentBookUri,   // Query the content URI for the current book
                projection,        // Columns to include in the resulting Cursor
                null,      // No selection clause
                null,  // No selection arguments
                null);    // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIERNAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIERPHONE);

            // Extract out the value from the Cursor for the given column index
            name = cursor.getString(nameColumnIndex);
            price = cursor.getInt(priceColumnIndex);
            quantity = cursor.getInt(quantityColumnIndex);
            supplierName = cursor.getString(supplierNameColumnIndex);
            supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Find all relevant views that we will need to fill in
            mNameText = (TextView) findViewById(R.id.book_name);
            mPriceText = (TextView) findViewById(R.id.price);
            mQuantityText = (TextView) findViewById(R.id.quantity);
            mSupplierNameText = (TextView) findViewById(R.id.supplier_name);

            // Update the views with the values from the database
            mNameText.setText(name);
            mPriceText.setText(Integer.toString(price));
            mQuantityText.setText(Integer.toString(quantity));
            mSupplierNameText.setText(supplierName);

            // Set an intent on the phone button
            phoneButton = findViewById(R.id.phone_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + supplierPhone));
                    startActivity(phoneIntent);
                }
            });

            // Add functionality to the edit button
            Button editButton = findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                    // Set Uri on the data field of the intent
                    intent.setData(mCurrentBookUri);
                    // Start the edit activity
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameText.setText("");
        mPriceText.setText("");
        mQuantityText.setText("");
        mSupplierNameText.setOnClickListener(null); // Remove onClickListener
    }


    public void updateQuantity(View view) {
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.editor_update_quantity_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_update_quantity_passed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseQuantity(View view) {
        if (quantity > 1) {
            quantity -= 1;
            updateQuantity(view);
        } else if (quantity == 1) {
            quantity -= 1;
            updateQuantity(view);
            // TODO: Disable decrease button
        } else {
            // Do nothing
        }
    }

    public void increaseQuantity(View view) {
        quantity += 1;
        updateQuantity(view);
    }

}
