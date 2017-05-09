package createnote_modul.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import createnote_modul.modul_attach.AttachmentAdapter;
import teambandau.memo.PhotoActivity;
import teambandau.memo.R;

/**
 * Created by l on 5/1/2017.
 */

public class CreateNoteFragmentAttach extends Fragment implements FragmentLifecycle, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

  public static final List<String> selectedImages = new ArrayList<>();

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  private FloatingActionButton fabAttach;
  private GridView gvAttachments;
  private TextView tvInBlank;
  private AttachmentAdapter attachmentAdapter;
  private Uri imageToUploadUri;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATE_NOTE_ACTIVITY, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
    if (selectedImages.size() == 0)
      selectedImages.addAll(sharedPreferences.getStringSet(CreateNoteActivity.NOTE_ATTACH_KEY, CreateNoteActivity.DEFAULT_ATTACH));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.attach_fragment, container, false);
    fabAttach = (FloatingActionButton) view.findViewById(R.id.fab_attach);
    fabAttach.setOnClickListener(this);

    gvAttachments = (GridView) view.findViewById(R.id.gv_attach);
    attachmentAdapter = new AttachmentAdapter(getActivity(), selectedImages);
    gvAttachments.setAdapter(attachmentAdapter);
    gvAttachments.setOnItemClickListener(this);
    gvAttachments.setOnItemLongClickListener(this);

    tvInBlank = (TextView) view.findViewById(R.id.tv_attach);
    if (selectedImages.size() > 0)
      tvInBlank.setVisibility(View.GONE);

    return view;
  }

  @Override
  public void onResumeFragment() {
  }

  @Override
  public void onPauseFragment() {
    Set<String> sets = new HashSet<>(selectedImages);
    editor.putStringSet(CreateNoteActivity.NOTE_ATTACH_KEY, sets);
    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 0:
        if (resultCode == Activity.RESULT_OK) {
          selectedImages.add(imageToUploadUri.toString());
          tvInBlank.setVisibility(View.GONE);
        }
        break;
      case 1:
        if (resultCode == Activity.RESULT_OK) {
          selectedImages.add(data.getData().toString());
          tvInBlank.setVisibility(View.GONE);
        }
        break;
      case 2:
        if (resultCode == Activity.RESULT_OK) {
          String uri = data.getDataString();
          Log.d("URI", String.format("onActivityResult: %s", uri));
          //if (fileType.equals("pdf")) {
//          Intent intent = new Intent(Intent.ACTION_VIEW);
//          intent.setDataAndType(uri, "application/pdf");
//          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//          startActivity(Intent.createChooser(intent, "Open File"));

//          } else if (fileType.equals("txt")) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "text/plain");
//            startActivity(intent);
//
//          } else if (fileType.equals(".docx")) {
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction(Intent.ACTION_VIEW);
//            String type = "application/msword";
//            intent.setDataAndType(uri, type);
//            startActivity(intent);
//          }
        }
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
        break;
    }
    attachmentAdapter.notifyDataSetChanged();
  }

  @Override
  public void onClick(View v) {
    if (v.equals(fabAttach)) {
      final CharSequence[] items = {
              "Take photo", "Select image", "Document"
      };

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setItems(items, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {
          switch (item) {
            case 0:
              Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              imageToUploadUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile());
              cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
              startActivityForResult(cameraIntent, 0);
              break;
            case 1:
              Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              startActivityForResult(pickPhoto, 1);
              break;
            case 2:
              Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("*/*");
              intent.addCategory(Intent.CATEGORY_OPENABLE);
              startActivityForResult(
                      Intent.createChooser(intent, "Select a File to Upload"),
                      2);
              break;
          }
        }
      });
      AlertDialog alert = builder.create();
      alert.show();
    }
  }

  private static File getOutputMediaFile() {
    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "Camera");

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator +
            "IMG_" + timeStamp + ".jpg");
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent i = new Intent(getActivity(), PhotoActivity.class);
    i.putExtra("uri_string", selectedImages.get(position));
    startActivity(i);
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
    final CharSequence[] items = {
            "Delete"
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setItems(items, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int item) {
        switch (item) {
          case 0:
            selectedImages.remove(position);
            attachmentAdapter.notifyDataSetChanged();
            break;
        }
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
    return true;
  }
}
