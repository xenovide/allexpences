package myexpp.vaibhav.android.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Home extends ListActivity {
	String activities[] = { "New Entry", "View Pocket", "Update Pocket", "View Monthly Record" };
	SQLiteDatabase ourDb;

    private View mContentView;

    private int mShortAnimationDuration;

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		// getListView().setBackgroundColor(Color.parseColor("#8F8FBC"));
		setListAdapter(new ArrayAdapter<String>(Home.this,
				android.R.layout.simple_list_item_1, activities));


            mContentView=(View)findViewById(R.id.main_activity);
            mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


            Button help=(Button)findViewById(R.id.buttonHelp);
            Button clear=(Button)findViewById(R.id.buttonPro);
            ImageButton im_new=(ImageButton)findViewById(R.id.img);


		    help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ih = new Intent(Home.this, HelpFile.class);
                    startActivity(ih);
                }
            });

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dib=new AlertDialog.Builder(Home.this);
                    dib.setTitle("Clear Data");
                    dib.setMessage("All your expense data will be deleted");
                    dib.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //To delete the database
                            DbCreate info = new DbCreate(Home.this);

                            info.open();

                            Home.this.deleteDatabase(info.DATABASE_NAME);

                            Toast.makeText(Home.this,"Your data has been deleted",Toast.LENGTH_LONG).show();
                            info.open();
                            info.close();
                        }
                    });
                    dib.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dib.show();
                }
            });

            im_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ih = new Intent(Home.this, NewEntry.class);
                    startActivity(ih);
                }
            });
		}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String activity = "";
        try {
        if (position == 0)
			activity = "NewEntry";
		else if (position == 1)
			activity = "ViewPocket";
		else if (position == 2)
			activity = "UpdatePocket";
		else if (position == 3)
			activity = "MonthlyRecord";

			Class c = Class.forName("myexpp.vaibhav.android.myapplication."+activity);
			Intent i = new Intent(Home.this, c);
			startActivity(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main, menu);
		// return false;
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.help:
				Intent ih = new Intent(Home.this, HelpFile.class);
				startActivity(ih);
				break;
			case R.id.clear:
				AlertDialog.Builder dib=new AlertDialog.Builder(this);
				dib.setTitle("Clear Data");
				dib.setMessage("All your expense data will be deleted");
				dib.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//To delete the database
						DbCreate info = new DbCreate(Home.this);
						
						info.open();

                        Home.this.deleteDatabase(info.DATABASE_NAME);

						Toast.makeText(Home.this,"Your data has been deleted",Toast.LENGTH_LONG).show();
						info.open();
						info.close();
					}
				});
				dib.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				dib.show();
				break;
		}
		return super.onOptionsItemSelected(item);	
	}
	
}
