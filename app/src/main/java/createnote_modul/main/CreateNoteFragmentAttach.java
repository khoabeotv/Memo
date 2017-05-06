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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.NoteApplication;
import createnote_modul.modul_attach.AttachmentAdapter;
import teambandau.memo.R;

/**
 * Created by l on 5/1/2017.
 */

public class CreateNoteFragmentAttach extends Fragment implements FragmentLifecycle, View.OnClickListener, AdapterView.OnItemClickListener {

  public static final List<String> selectedImages = new ArrayList<>();

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  private FloatingActionButton fabAttach;
  private GridView gvAttachments;
  private TextView tvInBlank;
  private ImageView imPicked;
  private ImageView imBack;
  private AttachmentAdapter attachmentAdapter;
  private Uri imageToUploadUri;


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
    gvAttachments.setOnItemClickListener(this);

    tvInBlank = (TextView) view.findViewById(R.id.tv_attach);
    if (selectedImages.size() > 0)
      tvInBlank.setVisibility(View.GONE);
    imPicked = (ImageView) view.findViewById(R.id.im_picked);
    imBack = (ImageView) view.findViewById(R.id.im_back);
    imBack.setOnClickListener(this);

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
    }
    attachmentAdapter.notifyDataSetChanged();
  }

  @Override
  public void onClick(View v) {
    if (v.equals(fabAttach)) {
      final CharSequence[] items = {
              "Take a picture", "Select a picture from gallery", "Document"
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
          }
        }
      });
      AlertDialog alert = builder.create();
      alert.show();
    } else if (v.equals(imBack)) {
      imBack.setVisibility(View.GONE);
      imPicked.setVisibility(View.GONE);
      gvAttachments.setVisibility(View.VISIBLE);
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
    imPicked.setImageURI(Uri.parse(selectedImages.get(position)));
    imPicked.setVisibility(View.VISIBLE);
    imBack.setVisibility(View.VISIBLE);
    gvAttachments.setVisibility(View.GONE);
  }
}
