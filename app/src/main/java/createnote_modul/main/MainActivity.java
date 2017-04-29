package createnote_modul.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import teambandau.memo.R;


public class MainActivity extends AppCompatActivity {

    private Button btnSupper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnSupper = (Button) findViewById(R.id.btnSupperButton);




        btnSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CreateNoteActivity.class);
                i.putExtra(CreateNoteActivity.NOTE_TITLE_KEY,"abc123");
                i.putExtra(CreateNoteActivity.NOTE_CONTENT_KEY,"abc123");
                i.putExtra(CreateNoteActivity.NOTE_COLOR_KEY,"#faff00");
                i.putExtra(CreateNoteActivity.NOTE_ICON_KEY,R.drawable.expressions0);
                startActivityForResult(i,CreateNoteActivity.REQUEST_CODE_CREATENOTE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case (CreateNoteActivity.REQUEST_CODE_CREATENOTE):{

                if(resultCode == CreateNoteActivity.RESULT_CODE_CREATENOTE){
                    Toast.makeText(this, data.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY) + " " + data.getStringExtra(CreateNoteActivity.NOTE_TITLE_KEY) + " " + data.getStringExtra(CreateNoteActivity.NOTE_COLOR_KEY), Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }
}
