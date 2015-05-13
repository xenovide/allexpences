package myexpp.vaibhav.android.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class DbCreate {
    public static final String KEY_PURPOSE = "Purpose";
    public static final String KEY_AMOUNT = "Amount";
    public static final String KEY_DATE = "entryDate";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_MONTH = "month_entry";
    public static final String KEY_YEAR = "year_entry";

    public static final String DATABASE_NAME = "pocketMoney";
    public static final String DATABASE_TABLE = "expense";
    public static final String DATABASE_TABLE2 = "balance";
    public static final String DATABASE_TABLE3 = "monthly_record";
    public static final int DATABASE_VERSION = 1;

    public DbHelper ourHelper;
    public final Context ourContext;
    public SQLiteDatabase ourDb;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + DATABASE_TABLE + "(" + KEY_ROWID
                    + " integer PRIMARY KEY AUTOINCREMENT," + KEY_PURPOSE
                    + " varchar(50), " + KEY_AMOUNT + " integer(5), "
                    + KEY_DATE + " DATE);");
            db.execSQL("create table " + DATABASE_TABLE2 + "(" + KEY_AMOUNT
                    + " integer(5) default 0);");
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT, 0);
            db.insert(DATABASE_TABLE2, null, cv);
            cv.clear();
            db.execSQL("create table " + DATABASE_TABLE3 + "(" + KEY_ROWID
                    + " integer PRIMARY KEY AUTOINCREMENT, " + KEY_MONTH
                    + " varchar(10), " + KEY_YEAR + " integer, " + KEY_AMOUNT
                    + " integer(5) );");
            Calendar cal = Calendar.getInstance();
            int year;
            String month;
            GetMonth gm = new GetMonth();
            month = gm.getMonth(cal.get(Calendar.MONTH));
            year = cal.get(Calendar.YEAR);
            cv.put(KEY_MONTH, month);
            cv.put(KEY_YEAR, year);
            cv.put(KEY_AMOUNT, 0);
            db.insert(DATABASE_TABLE3, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table if exists " + DATABASE_TABLE);
            db.execSQL("Drop table if exists " + DATABASE_TABLE2);
            db.execSQL("Drop table if exists " + DATABASE_TABLE3);
            onCreate(db);

        }


    }

    public DbCreate(Context c) {
        ourContext = c;
    }

    public DbCreate open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDb = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public boolean createEntry(String purpose, int amount, String key) {
        int amt = 0, prevAmt = 0;
        boolean fg = true;
        String[] col = new String[] { KEY_AMOUNT };
        Cursor c = ourDb.query(DATABASE_TABLE2, col, null, null, null, null,
                null);
        c.moveToFirst();
        prevAmt = Integer.parseInt(c.getString(0));
        if (key == "record") {
            if (amount > prevAmt) {
                fg = false;
            } else {
                ContentValues cv = new ContentValues();
                cv.put(KEY_PURPOSE, purpose);
                cv.put(KEY_AMOUNT, amount);
                String date = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date(System.currentTimeMillis()));
                cv.put(KEY_DATE, date);
                ourDb.insert(DATABASE_TABLE, null, cv);
                // ourDb.rawQuery("insert into "+DATABASE_TABLE+" ("+KEY_PURPOSE+","+KEY_AMOUNT+","+KEY_DATE+")values( '"+purpose+"', "+amount+",NOW());",null);
                cv.clear();
                amt = prevAmt - amount;
                cv.put(KEY_AMOUNT, amt);
                ourDb.update(DATABASE_TABLE2, cv, KEY_AMOUNT + "=" + prevAmt,
                        null);
                cv.clear();
                Calendar cal = Calendar.getInstance();
                GetMonth gm = new GetMonth();
                String presentMonth = gm.getMonth(cal.get(Calendar.MONTH));
                int presentYear = cal.get(Calendar.YEAR);
                Cursor cur = ourDb.rawQuery("select * from " + DATABASE_TABLE3
                        + " where " + KEY_MONTH + " = '" + presentMonth
                        + "' and " + KEY_YEAR + " = " + presentYear, null);
                if (!cur.moveToFirst()) {
                    cv.put(KEY_AMOUNT, amount);
                    cv.put(KEY_MONTH, presentMonth);
                    cv.put(KEY_YEAR, presentYear);
                    ourDb.insert(DATABASE_TABLE3, null, cv);
                } else {
                    cur.moveToFirst();
                    cur.getString(1);
                    Integer.parseInt(cur.getString(cur
                            .getColumnIndex(KEY_YEAR)));
                    prevAmt = Integer.parseInt(cur.getString(cur
                            .getColumnIndex(KEY_AMOUNT)));
                    amt = prevAmt + amount;

                    cv.put(KEY_AMOUNT, amt);
                    ourDb.update(DATABASE_TABLE3, cv, KEY_MONTH + " = '"
                            + presentMonth + "' and " + KEY_YEAR + " = "
                            + presentYear, null);

                }

                fg = true;
            }
        } else if (key == "update") {
            ContentValues cv = new ContentValues();
            cv.put(KEY_PURPOSE, "AMOUNT DEPOSITED");
            cv.put(KEY_AMOUNT, amount);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
                    System.currentTimeMillis()));
            cv.put(KEY_DATE, date);
            ourDb.insert(DATABASE_TABLE, null, cv);
            cv.clear();
            amount += prevAmt;

            cv.put(KEY_AMOUNT, amount);
            ourDb.update(DATABASE_TABLE2, cv, KEY_AMOUNT + "=" + prevAmt, null);
            fg = true;
        } else if (key == "subtract") {
            amt = 0;
            if (amount <= prevAmt) {
                amt = prevAmt - amount;
            } else {
                amt = 0;
            }
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT, amt);
            ourDb.update(DATABASE_TABLE2, cv, KEY_AMOUNT + "=" + prevAmt, null);
            cv.clear();
            cv.put(KEY_PURPOSE, "AMOUNT WITHDRAWN");
            cv.put(KEY_AMOUNT, amount);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
                    System.currentTimeMillis()));
            cv.put(KEY_DATE, date);
            ourDb.insert(DATABASE_TABLE, null, cv);
            fg = true;
        }
        return fg;
    }

    public String readData(String key) {
        String res = "";
        if (key != "check_amount") {
            String[] columns = new String[] { KEY_PURPOSE, KEY_AMOUNT };
            Cursor c = ourDb.query(DATABASE_TABLE, columns, null, null, null,
                    null, null);
            if (key == "purpose") {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    res += c.getString(c.getColumnIndex(KEY_PURPOSE))
                            + "  ---  Rs."
                            + c.getString(c.getColumnIndex(KEY_AMOUNT))
                            + "\n\n";

                }
            }
        } else if (key == "check_amount") {
            String[] column = new String[] { KEY_AMOUNT };
            Cursor c = ourDb.query(DATABASE_TABLE2, column, null, null, null,
                    null, null);
            c.moveToFirst();
            res += c.getString(c.getColumnIndex(KEY_AMOUNT));

        }
        return res;
    }

    public String[] getData() {
        String[] columns = new String[] { KEY_PURPOSE, KEY_AMOUNT };
        int size = getSize();
        String array[] = new String[size];
        int i = 0;
        Cursor c = ourDb.query(DATABASE_TABLE, columns, null, null, null, null,
                null);
        for (c.moveToFirst(), i = 0; !c.isAfterLast(); c.moveToNext(), i++) {
            array[i] = c.getString(c.getColumnIndex(KEY_PURPOSE))
                    + "  ---  Rs." + c.getString(c.getColumnIndex(KEY_AMOUNT));
        }
        return array;
    }

    public int getSize() {
        String[] columns = new String[] { KEY_PURPOSE, KEY_AMOUNT };
        int i = 0;
        Cursor c = ourDb.query(DATABASE_TABLE, columns, null, null, null, null,
                null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            i++;
        }
        return i;
    }

    public String getPurpose(long id) throws SQLException {
        String[] col = new String[] { KEY_ROWID, KEY_PURPOSE, KEY_AMOUNT };
        Cursor c = ourDb.query(DATABASE_TABLE, col, KEY_ROWID + "=" + id, null,
                null, null, null);

        if (c != null) {
            c.moveToFirst();
            String purpose = c.getString(1);
            return purpose;
        } else
            return null;
    }

    public String getAmount(long id) throws SQLException {
        String[] col = new String[] { KEY_ROWID, KEY_PURPOSE, KEY_AMOUNT };
        // id++;
        Cursor c = ourDb.query(DATABASE_TABLE, col, KEY_ROWID + "=" + id, null,
                null, null, null);

        if (c != null) {
            c.moveToFirst();
            String amount = c.getString(2);
            return amount;
        } else
            return null;
    }

    public String getDate(long id) throws SQLException {
        String[] col = new String[] { KEY_ROWID, KEY_PURPOSE, KEY_AMOUNT,
                KEY_DATE };
        // id++;
        Cursor c = ourDb.query(DATABASE_TABLE, col, KEY_ROWID + "=" + id, null,
                null, null, null);

        if (c != null) {
            c.moveToFirst();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            // String date = c.getString(3);
            String date = format.format(c.getString(3));
            return date;
        } else
            return null;
    }

    public void refundAndDelete(long id) {
        int amount;
        Cursor c = ourDb.rawQuery("select * from " + DATABASE_TABLE + " where "
                + KEY_ROWID + " = " + id, null);
        if (c.moveToFirst()) {
            c.moveToFirst();
            int prevAmt = 0;
            amount = Integer.parseInt(c.getString(2));
            String date = c.getString(3);
            StringTokenizer st = new StringTokenizer(date);

            String year = st.nextToken("-");
            ourDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null);
            Cursor c2 = ourDb
                    .rawQuery("select * from " + DATABASE_TABLE2, null);
            c2.moveToFirst();
            int amt = 0;
            prevAmt = Integer.parseInt(c2.getString(0));
            amt = amount + prevAmt;
            ContentValues cv = new ContentValues();
            cv.put(KEY_AMOUNT, amt);
            ourDb.update(DATABASE_TABLE2, cv, KEY_AMOUNT + "=" + prevAmt, null);

            c2.close();

            Cursor c3 = ourDb.rawQuery("select * from " + DATABASE_TABLE3
                    + " where " + KEY_YEAR + "=" + year, null);
            if (c3.moveToFirst()) {
                c3.moveToFirst();
                prevAmt = Integer.parseInt(c3.getString(3));
                amt = 0;
                amt = prevAmt - amount;
                cv.clear();
                cv = new ContentValues();
                cv.put(KEY_AMOUNT, amt);
                ourDb.update(DATABASE_TABLE3, cv, KEY_YEAR + "=" + year, null);
            }
        }
    }

    public int calculateDailyAvg() {
        Date date1, date2;
        int avg = 0, date = 1;
        int total = 0;
        Cursor c = ourDb.rawQuery("select " + KEY_DATE + "," + KEY_AMOUNT + ","
                + KEY_PURPOSE + " from " + DATABASE_TABLE, null);
        if (c.moveToFirst()) {
            date1 = Date.valueOf(c.getString(0));

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                if ((!c.getString(2).equals("AMOUNT DEPOSITED"))
                        && (!c.getString(2).equals("AMOUNT WITHDRAWN"))) {
                    total += Integer.parseInt(c.getString(1));
                }
            }
            c.moveToLast();
            date2 = Date.valueOf(c.getString(0));
            date = (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
            date++;
        }
        if (date != 0) {
            avg = total / date;
        } else if (date == 0) {
            avg = total;
        }
        return avg;
    }

}
