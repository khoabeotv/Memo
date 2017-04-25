package teambandau.memo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.NoteAdapter;
import application.NoteApplication;
import model.Note;
import model.NoteManager;

public class MainActivity extends AppCompatActivity {

  private MainFragment mainFragment;
  private boolean doublePressBack = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mainFragment = new MainFragment();
    initFragmentStack();


  }

  @Override
  public void onBackPressed() {
    if (NoteManager.getParentId() == 0) {
      if (!doublePressBack) {
        doublePressBack = true;
        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            doublePressBack = false;
          }
        }, 2000);
        return;
      } else {
        super.onBackPressed();
        return;
      }
    }
    mainFragment.onBackPressed();
  }

  private void initFragmentStack() {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(R.id.activity_main, mainFragment);
    transaction.commit();
  }

  public void changeFragmentStack(Fragment fragment) {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.show(fragment);
    transaction.commit();
  }
}
