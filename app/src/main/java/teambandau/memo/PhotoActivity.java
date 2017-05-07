package teambandau.memo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity {

  private PhotoView photoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo);

    photoView = (PhotoView) findViewById(R.id.im_picked);
    Intent i = getIntent();
    photoView.setImageURI(Uri.parse(i.getStringExtra("uri_string")));
  }
}
