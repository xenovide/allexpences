

package myexpp.vaibhav.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewEntry extends Activity implements OnClickListener {
    String purpose;
    int amount;
    Button submit;
    EditText p, a;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);
        initialize();
        submit.setOnClickListener(this);
    }

    private void initialize() {
        // TODO Auto-generated method stub
        submit = (Button) findViewById(R.id.bSubmit);
        p = (EditText) findViewById(R.id.etPurpose);
        a = (EditText) findViewById(R.id.etAmount);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bSubmit:
                boolean fg = true;
                if (p.getText().toString().trim().length() > 0
                        && a.getText().toString().trim().length() > 0) {
                    purpose = p.getText().toString();
                    amount = Integer.parseInt(a.getText().toString());
                    boolean did = true;
                    try {
                        DbCreate entry = new DbCreate(NewEntry.this);
                        entry.open();
                        fg = entry.createEntry(purpose, amount, "record");
                        entry.close();

                    } catch (Exception e) {
                        did = false;
                        // Toast.makeText(NewEntry.this, e.toString(),
                        // Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } finally {
                        if (did && fg) {

                            Toast.makeText(NewEntry.this, "New Record Updated",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NewEntry.this, "Amount Exceeded",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    Class c;
                    try {
                        if (fg) {
                            c = Class.forName("myexpp.vaibhav.android.myapplication.ViewPocket");
                            startActivity(new Intent(NewEntry.this, c));
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(NewEntry.this, "Enter Data", Toast.LENGTH_LONG).show();
                }
                // hide keyboard
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(a.getWindowToken(), 0);

                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
