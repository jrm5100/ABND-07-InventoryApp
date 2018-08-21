package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    // empty constructor
    private BookContract() {}

    public static final class BookEntry implements BaseColumns {
        /** Table Name*/
        public final static String TABLE_NAME = "books";

        // Column Names
        /** Id */
        public final static String _ID = BaseColumns._ID;

        /** Product Name  = STRING */
        public final static String COLUMN_BOOK_PRODUCTNAME = "name";

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
