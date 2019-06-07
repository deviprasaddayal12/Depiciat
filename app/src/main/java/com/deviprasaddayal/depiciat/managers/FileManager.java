package com.deviprasaddayal.depiciat.managers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.deviprasaddayal.depiciat.BuildConfig;
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener;
import com.deviprasaddayal.depiciat.listeners.OnFileAttachListener;
import com.deviprasaddayal.depiciat.listeners.OnFileSourceChoiceListener;
import com.deviprasaddayal.depiciat.utils.DateUtils;
import com.deviprasaddayal.depiciat.utils.DialogUtils;
import com.deviprasaddayal.depiciat.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager implements OnFileSourceChoiceListener {
    public static final String TAG = FileManager.class.getCanonicalName();

    public interface Permissions {
        int CAMERA = 123;
        int GALLERY = 124;
        int BROWSER = 125;
        int STORAGE = 126;
    }

    public interface Requests {
        int CAMERA = 126;
        int GALLERY = 127;
        int BROWSER = 128;
    }

    private Activity activity;
    private OnFileActionListener onFileActionListener;
    private int module;

    private AlertDialog dialogViewer;

    public FileManager(Activity activity, OnFileActionListener onFileActionListener) {
        this.activity = activity;
        this.onFileActionListener = onFileActionListener;
    }

    public FileManager(Activity activity, OnFileActionListener onFileActionListener, int module) {
        this.activity = activity;
        this.onFileActionListener = onFileActionListener;
        this.module = module;
    }

    /**
     * Checks for the file manager permission during the runtime, if already granted, takes to the startFileBrowser()
     */
    private void requestDirectories() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permissions.STORAGE);
            } else {
                makeDirectories();
            }
        } else {
            makeDirectories();
        }
    }

    /**
     * This method is used to create, if unavailable, directories for saving files
     * It checks for the existence of folders for the respective file type and category
     */
    private void makeDirectories() {

    }

    public void showFileSourceDialog() {
        DialogUtils.showFileSourceDialog(activity, this);
    }

    @Override
    public void onCameraChosen() {
        gotoCamera();
    }

    @Override
    public void onGalleryChosen() {
        gotoGallery();
    }

    @Override
    public void onFileManagerChosen() {
        gotoFileBrowser();
    }

    public void gotoCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA}, Permissions.CAMERA);
            } else
                startCamera();
        } else
            startCamera();
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();

                onFileActionListener.onFilePathCreatedForCamera(photoFile.getAbsolutePath(), photoFile);
            } catch (IOException ex) {
                // Todo : handle exceptions while creating files for version M and higher
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(intent, Requests.CAMERA);
            }
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized File createImageFile() throws IOException {
        return File.createTempFile("IMG_" + DateUtils.getImageTimeStamp(), ".jpg",
                activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    public void gotoGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Permissions.GALLERY);
            } else
                startGallery();
        } else
            startGallery();
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            activity.startActivityForResult(intent, Requests.GALLERY);
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoFileBrowser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permissions.BROWSER);
            } else
                startFileBrowser();
        } else
            startFileBrowser();
    }

    private void startFileBrowser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, Requests.BROWSER);
    }

    public void attachFiles(Intent data, ArrayList<String> pathList, OnFileAttachListener onFileAttachListener) {
        try {
            if (data.getData() != null) {
                String imagePath = FileUtils.getInternalStoragePath(activity, data.getData());
                if (imagePath == null) {
                    if (FileUtils.isSDCardAvailable(activity)) {
                        imagePath = FileUtils.getExternalStoragePath(activity, data.getData());
                    }
                    if (imagePath == null) {
                        DialogUtils.showFailureDialog(activity, "Unable to fetch file from SDCard." +
                                "\nMove the file into Device Memory and retry.");
                        return;
                    }
                }
                if (!pathList.contains(imagePath)) {
                    onFileAttachListener.onFileAttached(imagePath, new File(imagePath));
                } else {
                    Toast.makeText(activity, "File already chosen.", Toast.LENGTH_SHORT).show();
                }
            } else if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    String imagePath = FileUtils.getInternalStoragePath(activity, data.getClipData().getItemAt(i).getUri());
                    if (imagePath == null) {
                        DialogUtils.showFailureDialog(activity, "Unable to fetch file from SDCard." +
                                "\nMove the file into Device Memory and retry.");
                        return;
                    }
                    if (!pathList.contains(imagePath)) {
                        onFileAttachListener.onFileAttached(imagePath, new File(imagePath));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showViewerWithDeleteOnly(ArrayList<String> pathList) {
        if (pathList.size() > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, true, false, onFileActionListener);
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.");
        }
    }

    public void showViewerWithAddOnly(ArrayList<String> pathList) {
        if (pathList.size() > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, false, true, onFileActionListener);
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.");
        }
    }

    public void showViewerWithAddAndDelete(ArrayList<String> pathList) {
        if (pathList.size() > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, true, true, onFileActionListener);
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.");
        }
    }

    public static boolean deleteFileFromStorage(ArrayList<File> files){
        boolean deleted = true;
        try{
            for (File file : files)
                deleted = deleted && deleteFileFromStorage(file);
        } catch(Exception e){
            Log.e(TAG, "deleteFileFromStorage: " +  e.getMessage());
        }
        return deleted;
    }

    public static boolean deleteFileFromStorage(File file){
        try{
            return file.delete();
        } catch(Exception e){
            Log.e(TAG, "deleteFileFromStorage: " +  e.getMessage());
            return false;
        }
    }

    public boolean dismissViewer(){
        if (dialogViewer != null && dialogViewer.isShowing()){
            dialogViewer.dismiss();
            return true;
        } else
            return false;
    }
}
