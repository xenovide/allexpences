package myexpp.vaibhav.android.myapplication;

import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMonth extends ListActivity {
	TextView monthTitle, monthTotalSpent, yearTitle, monthDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_month);
		Bundle extras = new Bundle();
		extras = getIntent().getExtras();
		int id = (int) extras.getLong("id");
		initialize();
		DbCreate info = new DbCreate(ViewMonth.this);
		info.open();
		Cursor c = info.ourDb.rawQuery("select * from " + info.DATABASE_TABLE3+ " where " + info.KEY_ROWID + "=" + id, null);
		if (c.moveToFirst()) {
			c.moveToFirst();
			monthTitle.setText(c.getString(1));
			yearTitle.setText(c.getString(2));
			monthTotalSpent.setText("Total Spent this month Rs."
					+ c.getString(3));

			GetMonth gm = new GetMonth();
			String month = gm.getMonthInNumeric(c.getString(1).toString());

			String year = c.getString(2);
			String date = year + "-" + month;
			c.close();
			//String data = "";
			try {
				c = info.ourDb.rawQuery("select * from " + info.DATABASE_TABLE
						+ " where " + info.KEY_DATE + " like '" + date + "%'",null);
				startManagingCursor(c);
				String coloumns[]=new String[]{info.KEY_PURPOSE,info.KEY_AMOUNT,info.KEY_DATE};
				int to[]=new int[]{R.id.purpose,R.id.amount,R.id.date};
				SimpleCursorAdapter mAdapter=new SimpleCursorAdapter(ViewMonth.this, R.layout.view_entry, c, coloumns, to);
				setListAdapter(mAdapter);
				//monthDetail.setText(data);
			} catch (Exception e) {
				Dialog d = new Dialog(ViewMonth.this);
				TextView t = new TextView(ViewMonth.this);
				t.setText(e.toString());
				d.setContentView(t);
				d.show();
			}
		} else
			Toast.makeText(ViewMonth.this, "hello", Toast.LENGTH_LONG).show();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		monthTitle = (TextView) findViewById(R.id.monthTitle);
		monthTotalSpent = (TextView) findViewById(R.id.monthTotalSpend);
		yearTitle = (TextView) findViewById(R.id.yearTitle);
		//monthDetail = (TextView) findViewById(R.id.monthDetail);
	}

}
