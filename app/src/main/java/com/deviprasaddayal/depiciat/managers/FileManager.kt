package com.deviprasaddayal.depiciat.managers

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.deviprasaddayal.depiciat.BuildConfig
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.listeners.OnFileAttachListener
import com.deviprasaddayal.depiciat.listeners.OnFileSourceChoiceListener
import com.deviprasaddayal.depiciat.utils.DateUtils
import com.deviprasaddayal.depiciat.utils.DialogUtils
import com.deviprasaddayal.depiciat.utils.FileUtils

import java.io.File
import java.io.IOException
import java.util.ArrayList

class FileManager(private val activity: Activity, private val onFileActionListener: OnFileActionListener) : OnFileSourceChoiceListener {

    private var namePrefix: String? = null
    private var storageDest: File? = null

    private var dialogViewer: AlertDialog? = null

    interface Permissions {
        companion object {
            val CAMERA = 123
            val GALLERY = 124
            val BROWSER = 125
            val STORAGE = 126
        }
    }

    interface Requests {
        companion object {
            val CAMERA = 126
            val GALLERY = 127
            val BROWSER = 128
        }
    }

    init {
        this.storageDest = null
    }

    /**
     * Checks for the file manager permission during the runtime, if already granted, takes to the startFileBrowser()
     */
    private fun requestDirectories() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Permissions.STORAGE
                )
            } else {
                makeDirectories()
            }
        } else {
            makeDirectories()
        }
    }

    /**
     * This method is used to create, if unavailable, directories for saving files
     * It checks for the existence of folders for the respective file type and category
     */
    private fun makeDirectories() {

    }

    fun showFileSourceDialog() {
        DialogUtils.showFileSourceDialog(activity, this)
    }

    override fun onCameraChosen() {
        gotoCamera()
    }

    override fun onGalleryChosen() {
        gotoGallery()
    }

    override fun onFileManagerChosen() {
        gotoFileBrowser()
    }

    fun gotoCamera(namePrefix: String) {
        this.namePrefix = namePrefix
        gotoCamera()
    }

    fun gotoCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CAMERA), Permissions.CAMERA
                )
            } else
                startCamera()
        } else
            startCamera()
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()

                onFileActionListener.onFilePathCreatedForCamera(photoFile.absolutePath, photoFile)
            } catch (ex: IOException) {
                // Todo : handle exceptions while creating files for version M and higher
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    BuildConfig.APPLICATION_ID + ".provider", photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(intent, Requests.CAMERA)
            }
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show()
        }
    }

    @Synchronized
    @Throws(IOException::class)
    private fun createImageFile(): File {
        storageDest =
            if (storageDest == null) activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) else storageDest

        namePrefix = if (namePrefix == null) "IMG_" else namePrefix

        val fileName = namePrefix!! + DateUtils.imageTimeStamp
        val fullFileName = "$fileName.jpg"

        //        File generatedFile;
        //        if (storageDest != null)
        //            generatedFile = new File(storageDest, fullFileName);
        //        else
        //            generatedFile = File.createTempFile(fileName, ".jpg", storageDest);
        //
        //        File generatedFile = File.createTempFile(fileName, ".jpg", storageDest);
        //        File renamedFile = new File(storageDest, fullFileName);
        //
        //        Log.i(TAG, "createImageFile: " + generatedFile.getAbsolutePath());
        //
        //        if (generatedFile.renameTo(renamedFile))
        //            Log.i(TAG, "createImageFile: renamedFile = " + renamedFile.getAbsolutePath());
        //        else
        //            Log.i(TAG, "createImageFile: generatedFile = " + generatedFile.getAbsolutePath());

        val imageFile = File(storageDest, fullFileName)
        Log.i(TAG, "createImageFile: imageFile = " + imageFile.absolutePath)

        return imageFile
    }

    fun gotoGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Permissions.GALLERY
                )
            } else
                startGallery()
        } else
            startGallery()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(activity.packageManager) != null) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            activity.startActivityForResult(intent, Requests.GALLERY)
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show()
        }
    }

    fun gotoFileBrowser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Permissions.BROWSER
                )
            } else
                startFileBrowser()
        } else
            startFileBrowser()
    }

    private fun startFileBrowser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        activity.startActivityForResult(intent, Requests.BROWSER)
    }

    fun attachFiles(data: Intent, pathList: ArrayList<String>, onFileAttachListener: OnFileAttachListener) {
        try {
            if (data.data != null) {
                var imagePath = FileUtils.getInternalStoragePath(activity, data.data)
                if (imagePath == null) {
                    if (FileUtils.isSDCardAvailable(activity)) {
                        imagePath = FileUtils.getExternalStoragePath(activity, data.data)
                    }
                    if (imagePath == null) {
                        DialogUtils.showFailureDialog(
                            activity,
                            "Unable to fetch file from SDCard." + "\nMove the file into Device Memory and retry."
                        )
                        return
                    }
                }
                if (!pathList.contains(imagePath)) {
                    onFileAttachListener.onFileAttached(imagePath, File(imagePath))
                } else {
                    Toast.makeText(activity, "File already chosen.", Toast.LENGTH_SHORT).show()
                }
            } else if (data.clipData != null) {
                for (i in 0 until data.clipData!!.itemCount) {
                    val imagePath = FileUtils.getInternalStoragePath(activity, data.clipData!!.getItemAt(i).uri)
                    if (imagePath == null) {
                        DialogUtils.showFailureDialog(
                            activity,
                            "Unable to fetch file from SDCard." + "\nMove the file into Device Memory and retry."
                        )
                        return
                    }
                    if (!pathList.contains(imagePath)) {
                        onFileAttachListener.onFileAttached(imagePath, File(imagePath))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showViewerWithDeleteOnly(pathList: ArrayList<String>) {
        if (pathList.size > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, true, false, onFileActionListener)
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.")
        }
    }

    fun showViewerWithAddOnly(pathList: ArrayList<String>) {
        if (pathList.size > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, false, true, onFileActionListener)
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.")
        }
    }

    fun showViewerWithAddAndDelete(pathList: ArrayList<String>) {
        if (pathList.size > 0) {
            dialogViewer = DialogUtils.showFilesDialog(activity, pathList, true, true, onFileActionListener)
        } else {
            DialogUtils.showFailureDialog(activity, "No files to show. Please add files to view.")
        }
    }

    fun dismissViewer(): Boolean {
        if (dialogViewer != null && dialogViewer!!.isShowing) {
            dialogViewer!!.dismiss()
            return true
        } else
            return false
    }

    companion object {
        val TAG = FileManager::class.java.canonicalName

        fun deleteFileFromStorage(files: ArrayList<File>): Boolean {
            var deleted = true
            try {
                for (file in files)
                    deleted = deleted && deleteFileFromStorage(file)
            } catch (e: Exception) {
                Log.e(TAG, "deleteFileFromStorage: " + e.message)
            }

            return deleted
        }

        fun deleteFileFromStorage(file: File): Boolean {
            try {
                return file.delete()
            } catch (e: Exception) {
                Log.e(TAG, "deleteFileFromStorage: " + e.message)
                return false
            }

        }
    }
}
