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
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePocket extends Activity implements OnClickListener {

    TextView t;
    EditText et;
    Button btn;
    Button btnSubtract;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pocket);
        t = (TextView) findViewById(R.id.tvBalance);
        et = (EditText) findViewById(R.id.etBalance);
        btn = (Button) findViewById(R.id.bUpdateBalance);
        btnSubtract = (Button) findViewById(R.id.bUpdateBalanceSubtract);

        DbCreate info = new DbCreate(UpdatePocket.this);
        info.open();
        try{
            String data = info.readData("check_amount");
            info.close();
            t.setText("Rs."+data);}
        catch(Exception e){Toast.makeText(this, "Exception1",Toast.LENGTH_SHORT).show();}

        btn.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int amt=0;
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bUpdateBalance:
                if (et.getText().toString().trim().length()>0) {
                    amt = Integer.parseInt(et.getText().toString());
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    DbCreate info = new DbCreate(UpdatePocket.this);
                    info.open();
                    info.createEntry("a", amt, "update");
                    info.close();
                    Toast.makeText(UpdatePocket.this, "Pocket Money Updated",
                            Toast.LENGTH_LONG).show();
                  //  onCreate(null);

                    Intent i=new Intent(UpdatePocket.this, ViewPocket.class);
                    startActivity(i);
                }else{
                    Toast.makeText(UpdatePocket.this, "Enter Data",
                            Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.bUpdateBalanceSubtract:
                if (et.getText().toString().trim().length()>0) {
                    amt = Integer.parseInt(et.getText().toString());
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    DbCreate info = new DbCreate(UpdatePocket.this);
                    info.open();
                    info.createEntry("a", amt, "subtract");
                    info.close();
                    Toast.makeText(UpdatePocket.this, "Pocket Money Updated",
                            Toast.LENGTH_LONG).show();
                   // onCreate(null);

                    Intent i=new Intent(UpdatePocket.this, ViewPocket.class);
                    startActivity(i);
                }else{
                    Toast.makeText(UpdatePocket.this, "Enter Data",
                            Toast.LENGTH_LONG).show();
                }

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
