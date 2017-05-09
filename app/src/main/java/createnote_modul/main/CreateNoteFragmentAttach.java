package createnote_modul.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.NoteApplication;
import createnote_modul.modul_attach.AttachmentAdapter;
import teambandau.memo.PhotoActivity;
import teambandau.memo.R;

/**
 * Created by l on 5/1/2017.
 */

public class CreateNoteFragmentAttach extends Fragment implements FragmentLifecycle, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

  public static final List<String> attaches = new ArrayList<>();

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
    if (attaches.size() == 0)
      attaches.addAll(sharedPreferences.getStringSet(CreateNoteActivity.NOTE_ATTACH_KEY, CreateNoteActivity.DEFAULT_ATTACH));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.attach_fragment, container, false);
    fabAttach = (FloatingActionButton) view.findViewById(R.id.fab_attach);
    fabAttach.setOnClickListener(this);

    gvAttachments = (GridView) view.findViewById(R.id.gv_attach);
    attachmentAdapter = new AttachmentAdapter(getActivity(), attaches);
    gvAttachments.setAdapter(attachmentAdapter);
    gvAttachments.setOnItemClickListener(this);
    gvAttachments.setOnItemLongClickListener(this);

    tvInBlank = (TextView) view.findViewById(R.id.tv_attach);
    if (attaches.size() > 0)
      tvInBlank.setVisibility(View.GONE);

    return view;
  }

  @Override
  public void onResumeFragment() {
  }

  @Override
  public void onPauseFragment() {
    Set<String> sets = new HashSet<>(attaches);
    editor.putStringSet(CreateNoteActivity.NOTE_ATTACH_KEY, sets);
    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 0:
        if (resultCode == Activity.RESULT_OK) {
          Log.d("khoa", imageToUploadUri.toString());
          attaches.add(imageToUploadUri.toString());
          tvInBlank.setVisibility(View.GONE);
        }
        break;
      case 1:
        if (resultCode == Activity.RESULT_OK) {
          attaches.add(data.getData().toString());
          tvInBlank.setVisibility(View.GONE);
        }
        break;
      case 2:
        if (resultCode == Activity.RESULT_OK) {
          String path = getPath(NoteApplication.getInstance(), data.getData());
          Uri fileUri = Uri.fromFile(new File(path));
          String fileName = fileUri.getLastPathSegment();
          Log.d("khoa", fileName);
          if (!fileName.contains(".pdf") && !fileName.contains(".txt") && !fileName.contains(".docx")) {
            Toast.makeText(getActivity(), "File is not supported", Toast.LENGTH_SHORT).show();
          } else {
            attaches.add(fileUri.toString());
            tvInBlank.setVisibility(View.GONE);
          }
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
                      Intent.createChooser(intent, "Select a File to Attach"),
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
    Uri fileUri = Uri.parse(attaches.get(position));
    String fileName = fileUri.getLastPathSegment();
    if (fileName.contains(".pdf")) {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(fileUri, "application/pdf");
      intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
      startActivity(Intent.createChooser(intent, "Open File"));
    } else if (fileName.contains(".txt")) {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(fileUri, "text/plain");
      startActivity(intent);
    } else if (fileName.contains(".docx")) {
      Intent intent = new Intent();
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setAction(Intent.ACTION_VIEW);
      String type = "application/msword";
      intent.setDataAndType(fileUri, type);
      startActivity(intent);
    } else {
      Intent i = new Intent(getActivity(), PhotoActivity.class);
      i.putExtra("uri_string", attaches.get(position));
      startActivity(i);
    }
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
            attaches.remove(position);
            attachmentAdapter.notifyDataSetChanged();
            break;
        }
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
    return true;
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static String getPath(Context context, Uri uri) {
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
        // TODO handle non-primary volumes
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
        return getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};
        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
      // Return the remote address
      if (isGooglePhotosUri(uri))
        return uri.getLastPathSegment();
      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }
    return null;
  }

  public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {column};
    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is Google Photos.
   */
  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }
}
