package myexpp.vaibhav.android.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPocket extends ListActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pocket);
		String balance = "";
		TextView tv = (TextView) findViewById(R.id.viewPocketTitle);
		TextView tv1 = (TextView) findViewById(R.id.viewPocketPurposeTitle);
		TextView tv2 = (TextView) findViewById(R.id.viewPocketAmountTitle);
		TextView tv3 = (TextView) findViewById(R.id.viewPocketDateTitle);
		TextView dailyAvg = (TextView) findViewById(R.id.dailyAvg);
        ImageButton im_new=(ImageButton)findViewById(R.id.img2);



		DbCreate info = new DbCreate(ViewPocket.this);
        try{info.open();
		dailyAvg.setText("Daily Average Spendings Rs."
				+ info.calculateDailyAvg());
		balance = info.readData("check_amount");
		}
		catch(Exception e){Toast.makeText(this, "Exception1",Toast.LENGTH_SHORT).show();
        e.printStackTrace();}

		Cursor c = info.ourDb.rawQuery("select * from " + info.DATABASE_TABLE
				+ " order by " + info.KEY_ROWID + " DESC", null);
		startManagingCursor(c);
		String[] columns = new String[] { info.KEY_PURPOSE, info.KEY_AMOUNT,
				info.KEY_DATE };
		int[] to = new int[] { R.id.purpose, R.id.amount, R.id.date };
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
				R.layout.view_entry, c, columns, to);
		if (mAdapter.isEmpty()) {
			tv.setText("No Records");
			tv1.setText("Balance Left " + balance);
			tv2.setText("");
			tv3.setText("");
		} else {
			tv.setText("Amount Left Rs. " + balance);
			this.setListAdapter(mAdapter);
		}
		info.close();

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long id) {
				DbCreate info = new DbCreate(ViewPocket.this);
				info.open();
				int ida = (int) id;
				Cursor c = info.ourDb.rawQuery("select " + info.KEY_PURPOSE
						+ " from " + info.DATABASE_TABLE + " where "
						+ info.KEY_ROWID + "=" + ida, null);
				c.moveToFirst();
				info.close();
				if ((!c.getString(0).equals("AMOUNT DEPOSITED"))
						&& (!c.getString(0).equals("AMOUNT WITHDRAWN"))) {
					showDialog((int) id);
				}
				return false;
			}
		});


        im_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ih = new Intent(ViewPocket.this, NewEntry.class);
                startActivity(ih);
                finish();
            }
        });

    }

	@Override
	protected Dialog onCreateDialog(final int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(ViewPocket.this);
		alert.setTitle("Delete or Refund ?");

		String select[] = new String[] { "Delete", "Refund" };
		alert.setSingleChoiceItems(select, id,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							DbCreate info = new DbCreate(ViewPocket.this);
							info.open();
							info.ourDb.delete(info.DATABASE_TABLE,info.KEY_ROWID + "=" + id, null);
							info.close();

							Class c;
							try {
								c = Class.forName("myexpp.vaibhav.android.myapplication.ViewPocket");
								Intent i = new Intent(ViewPocket.this, c);
								startActivity(i);
								finish();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (which == 1) {
                            try {
							DbCreate info = new DbCreate(ViewPocket.this);
							info.open();

							info.refundAndDelete(id);
							info.close();

							Class c;
								c = Class
										.forName("myexpp.vaibhav.android.myapplication");
								Toast.makeText(ViewPocket.this,
										"Amount has been Refunded",
										Toast.LENGTH_LONG).show();
								Intent i = new Intent(ViewPocket.this, c);
								startActivity(i);
								finish();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(ViewPocket.this, "Canceled ",
								Toast.LENGTH_SHORT).show();
					}
				});
		return alert.create();
	}

}
