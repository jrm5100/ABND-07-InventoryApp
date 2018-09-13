package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.BookContract.BookEntry;


public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
         // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Find the columns of attributes that we're interested in
        int _idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        // Read the attributes from the Cursor for the current book
        int _id = cursor.getInt(_idColumnIndex);
        String bookName = cursor.getString(nameColumnIndex);
        int price = cursor.getInt(priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);

        // Make Uri
        Uri currentBookUri= ContentUris.withAppendedId(BookEntry.CONTENT_URI, _id);

        // Update the TextViews with the attributes
        nameTextView.setText(bookName);
        priceTextView.setText(context.getString(R.string.price_formatter, price));
        quantityTextView.setText(context.getString(R.string.quantity_formatter, quantity));

        // Set onClickListener for the sale button
        Button saleButton = view.findViewById(R.id.sale_button);
        setupDecreaseQuantButton(saleButton, quantity, currentBookUri, context);
    }


    private void setupDecreaseQuantButton(final Button btn, final Integer quantity, final Uri currentBookUri, final Context context) {
        if (quantity >= 1) {
            btn.setEnabled(true);
            btn.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity >= 1) {
                        ContentValues values = new ContentValues();
                        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity - 1);
                        int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);
                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(context, context.getString(R.string.editor_update_quantity_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(context, context.getString(R.string.editor_update_quantity_passed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            btn.setEnabled(false);
            btn.setBackgroundColor(context.getResources().getColor(R.color.gray));
            btn.setOnClickListener(null);
        }
    }
}