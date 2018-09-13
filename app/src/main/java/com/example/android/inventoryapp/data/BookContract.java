package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    // empty constructor
    private BookContract() {}

    /** Uri constants */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKS = "books";

    public static final class BookEntry implements BaseColumns {

        /** The content URI to access the book data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /** Table Name*/
        public final static String TABLE_NAME = "books";

        /** The MIME type for a list of books */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /** The MIME type for a single book */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // Column Names
        /** Id */
        public final static String _ID = BaseColumns._ID;

        /** Book Name  = STRING */
        public final static String COLUMN_BOOK_NAME = "name";

        /** Price (cents) = INTEGER */
        public final static String COLUMN_BOOK_PRICE = "price";

        /** Quantity = INTEGER */
        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        /** Supplier Name = STRING */
        public final static String COLUMN_BOOK_SUPPLIERNAME = "supplier_name";

        /** Supplier Phone = STRING */
        public final static String COLUMN_BOOK_SUPPLIERPHONE = "supplier_phone";
    }

}
