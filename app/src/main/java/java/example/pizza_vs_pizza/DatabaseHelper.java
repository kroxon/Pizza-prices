package java.example.pizza_vs_pizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

//    private static final String TAG = "DatabaseHelper";
    private Context context;
    private static final String DATABASE_NAME = "Units.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "units";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_UNIT = "unit";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CURRENCY + " TEXT, " +
                COLUMN_UNIT + " TEXT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData (String currency, String unit){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CURRENCY, currency);
        contentValues.put(COLUMN_UNIT, unit);
        long result = sqLiteDatabase.insert(TABLE_NAME, null,  contentValues);
//        if (result == -1)
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(context, "Added succesfully!", Toast.LENGTH_SHORT).show();
    }

    public void updateData(String row_id, String currency, String unit){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CURRENCY, currency);
        contentValues.put(COLUMN_UNIT, unit);

        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, "_id=?", new String[]{row_id});
//        if (result == -1)
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(context, "Updated succesfully!", Toast.LENGTH_SHORT).show();
    }

    Cursor readData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }


}
