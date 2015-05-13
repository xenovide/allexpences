package myexpp.vaibhav.android.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MonthlyRecord extends ListActivity implements OnItemClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_record);

        DbCreate info = new DbCreate(this);
		info.open();
		Cursor cur=info.ourDb.rawQuery("select SUM("+info.KEY_AMOUNT+") AS total from "+info.DATABASE_TABLE3,null);
		cur.moveToFirst();
		String total =cur.getString(cur.getColumnIndex("total"));
		cur.close();
		TextView tv=(TextView)findViewById(R.id.totalSpent);
		tv.setText("Total Spent : Rs."+total);
		String col[] = new String[] { info.KEY_MONTH, info.KEY_YEAR,
				info.KEY_AMOUNT };
		cur = info.ourDb.rawQuery("select * from "
				+ info.DATABASE_TABLE3, null);
		startManagingCursor(cur);
		int to[] = new int[] { R.id.tvMonth, R.id.tvYear, R.id.tvAmount };
		try {
			setListAdapter(new SimpleCursorAdapter(MonthlyRecord.this,R.layout.monthly_entry, cur, col, to));
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		info.close();
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
		// TODO Auto-generated method stub
		Intent i=new Intent(MonthlyRecord.this,ViewMonth.class);
		i.putExtra("id", id);
		startActivity(i);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
