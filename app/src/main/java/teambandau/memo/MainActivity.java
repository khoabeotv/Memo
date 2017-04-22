package teambandau.memo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.NoteAdapter;
import application.NoteApplication;
import model.Note;
import model.NoteManager;

public class MainActivity extends AppCompatActivity {

  private MainFragment mainFragment;

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
      super.onBackPressed();
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
