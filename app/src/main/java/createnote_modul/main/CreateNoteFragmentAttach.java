package createnote_modul.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import createnote_modul.modul_attach.AttachmentAdapter;
import teambandau.memo.R;

/**
 * Created by l on 5/1/2017.
 */

public class CreateNoteFragmentAttach extends Fragment implements FragmentLifecycle, View.OnClickListener {

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  private FloatingActionButton fabAttach;
  public static final List<String> selectedImages = new ArrayList<>();
  private GridView gvAttachments;
  private AttachmentAdapter attachmentAdapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = getActivity().getSharedPreferences(CreateNoteActivity.NAME_OF_SHARED_PREFERENCES_CREATENOTEACTIVITY, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
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
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case 0:
//        if (resultCode == Activity.RESULT_OK) {
//          selectedImages.add(getImageUri(getActivity(), (Bitmap) data.getExtras().get("data")).toString());
//        }
        break;
      case 1:
        if (resultCode == Activity.RESULT_OK) {
          selectedImages.add(data.getData().toString());
        }
        break;
    }
    attachmentAdapter.notifyDataSetChanged();
  }

  @Override
  public void onClick(View v) {
    final CharSequence[] items = {
            "Take a picture", "Select a picture from gallery", "Document"
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setItems(items, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int item) {
        switch (item) {
          case 0:
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
            break;
          case 1:
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
            break;
        }
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
  }

//  public Uri getImageUri(Context inContext, Bitmap inImage) {
//    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//    return Uri.parse(path);
//  }
}