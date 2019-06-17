package com.deviprasaddayal.depiciat.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

/**
 * Created by AND on 6/27/2018.
 */

object FileUtils {
    val TAG = FileUtils::class.java.canonicalName

    fun getFileRealName(filepath: String): String {
        val fileNameParts = filepath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return fileNameParts[fileNameParts.size - 1]
    }

    /**
     * This method is called to create one, if file does not exist
     *
     * @param directory name for the folder to be created
     */
    private fun mkDir(directory: File): File {
        if (!directory.exists())
            directory.mkdirs()
        return directory
    }

    fun getCompressedFile(context: Context, filePaths: ArrayList<String>): ArrayList<File> {
        val compressedFiles = ArrayList<File>()

        for (i in filePaths.indices) {
            //            Log.i(TAG, "getCompressedFile: " + filePaths.get(i));
            compressedFiles.add(getCompressedFile(context, filePaths[i]))
        }

        return compressedFiles
    }

    fun getCompressedFile(context: Context, filePath: String?): File {
        val imgFile = File(filePath)
        try {
            var length = imgFile.length()
            length = length / 1024
            if (length > 100) {
                /*compressedFile = Compressor.getDefault(context).compressToFile(imgFile);*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imgFile
    }

    fun createImageThumbnail(context: Context, uri: Uri, file: File) {
        try {
            val THUMBNAIL_SIZE = 64
            val compressedFile = getCompressedFile(context, uri.path)

            val fis = FileInputStream(compressedFile.absolutePath)
            var imageBitmap = BitmapFactory.decodeStream(fis)

            val width = imageBitmap.width.toFloat()
            val height = imageBitmap.height.toFloat()
            val ratio = width / height
            imageBitmap =
                Bitmap.createScaledBitmap(imageBitmap, (THUMBNAIL_SIZE * ratio).toInt(), THUMBNAIL_SIZE, false)

            saveThumbnail(file, imageBitmap)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun createVideoThumbnail(context: Context, uri: Uri, file: File) {
        val bitmap = ThumbnailUtils.createVideoThumbnail(uri.path, MediaStore.Video.Thumbnails.MINI_KIND)
        saveThumbnail(file, bitmap)
    }

    fun createDocumentThumbnail(context: Context, uri: Uri, file: File) {
        val pageNumber = 0
        /*PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument);

            saveThumbnail(file, bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private fun saveThumbnail(file: File, bmp: Bitmap) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getTempFileSuffix(uri: String): String {
        val splitUri = uri.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Log.i(TAG, "getTempFileSuffix: ." + splitUri[splitUri.size - 1])
        return "." + splitUri[splitUri.size - 1]
    }

    fun getPlaybackDuration(milliseconds: Int): String {
        val min: String
        val sec: String
        val seconds = milliseconds / 1000
        val mins = seconds / 60
        val secs = seconds % 60
        if (mins < 10)
            min = "0$mins"
        else
            min = "" + mins

        if (secs < 10)
            sec = "0$secs"
        else
            sec = "" + secs

        return "$min:$sec"
    }

    fun getFileSize(size: Long): String {
        val oneMB = (1024 * 1024).toLong()
        val oneKB: Long = 1024
        val oneMBThousandth = 1000 / oneMB
        val oneKBThousandth = 1000 / oneKB
        return if (size / oneMB > 0) {
            (size / oneMB).toString() + "." + oneMBThousandth * (size % oneMB) + " MB"
        } else
            (size / oneKB).toString() + "." + oneKBThousandth * (size % oneKB) + " KB"
    }

    @Throws(IOException::class)
    fun getInternalStoragePath(context: Context, uri: Uri): String? {
        //check here to KITKAT or new version
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            //DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun isSDCardAvailable(context: Context): Boolean {
        val storages = ContextCompat.getExternalFilesDirs(context, null)
        return storages.size > 1 && storages[0] != null && storages[1] != null
    }

    private fun getSDCardRotPath(context: Context): String {
        val storages = ContextCompat.getExternalFilesDirs(context, null)
        val storageInSDCard = storages[1]
        val removableSDCardAsRootFile = storageInSDCard.parentFile.parentFile.parentFile.parentFile
        Log.i(TAG, "getSDCardRotPath: $removableSDCardAsRootFile")
        return removableSDCardAsRootFile.absolutePath
    }

    fun getExternalStoragePath(context: Context, uri: Uri): String? {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            val sdcardPath = getSDCardRotPath(context)
            return sdcardPath + "/" + split[1]
        } else
            return null
    }

    fun getMimeType(uri: String): String {
        val fileNameParts = File(uri).name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Log.i(TAG, "getMimeType: " + fileNameParts[fileNameParts.size - 1])
        when (fileNameParts[fileNameParts.size - 1]) {
            // text extensions
            "txt" -> return FileExtension.txt
            // image extensions
            "bmp" -> return FileExtension.bmp
            "cgm" -> return FileExtension.cgm
            "gif" -> return FileExtension.gif
            "jpeg" -> return FileExtension.jpeg
            "jpg" -> return FileExtension.jpg
            "mdi" -> return FileExtension.mdi
            "psd" -> return FileExtension.psd
            "png" -> return FileExtension.png
            "svg" -> return FileExtension.svg
            // audio extensions
            "adp" -> return FileExtension.adp
            "aac" -> return FileExtension.aac
            "mpga" -> return FileExtension.mpga
            "mp4a" -> return FileExtension.mp4a
            "oga" -> return FileExtension.oga
            "wav" -> return FileExtension.wav
            "mp3" -> return FileExtension.mp3
            // video extensions
            "3gp" -> return FileExtension.a3gp
            "3g2" -> return FileExtension.a3g2
            "avi" -> return FileExtension.avi
            "xlv" -> return FileExtension.xlv
            "m4v" -> return FileExtension.m4v
            "mp4" -> return FileExtension.mp4
            // documents extension
            "rar" -> return FileExtension.rar
            "zip" -> return FileExtension.zip
            "pdf" -> return FileExtension.pdf
            "dvi" -> return FileExtension.dvi
            "karbon" -> return FileExtension.karbon
            "mdb" -> return FileExtension.mdb
            "xls" -> return FileExtension.xls
            "pptx" -> return FileExtension.pptx
            "docx" -> return FileExtension.docx
            "ppt" -> return FileExtension.ppt
            "doc" -> return FileExtension.doc
            "oxt" -> return FileExtension.oxt
            else -> return "file/*"
        }
    }

    interface FileExtension {
        companion object {
            // Text Extensions
            val txt = "text/plain"

            // Image Extensions
            val bmp = "image/bmp"
            val cgm = "image/cgm"
            val gif = "image/gif"
            val jpeg = "image/jpeg"
            val jpg = "image/jpg"
            val mdi = "image/vnd.ms-modi"
            val psd = "image/vnd.adobe.photoshop"
            val png = "image/png"
            val svg = "image/svg"

            // Audio Extensions
            val adp = "audio/adpcm"
            val aac = "audio/x-aac"
            val mpga = "audio/mpeg"
            val mp4a = "audio/mp4"
            val oga = "audio/ogg"
            val wav = "audio/x-wav"
            val mp3 = "audio/mp3"

            // Video Extensions
            val a3gp = "video/3gpp"
            val a3g2 = "video/3gpp2"
            val avi = "video/x-msvideo"
            val xlv = "video/x-flv"
            val m4v = "video/x-m4v"
            val mp4 = "video/mp4"

            // Document Extensions
            val pdf = "application/pdf"
            val dvi = "application/x-dvi"
            val karbon = "application/vnd.kdee.karbon"
            val mdb = "application/x-msaccess"
            val xls = "application/vnd.ms-excel"
            val pptx = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            val docx = "application/vnd.openxmlformats-officedocument.spreadsheet.template"
            val ppt = "application/vnd.ms-powerpoint"
            val doc = "application/vnd.ms-word"
            val oxt = "application/vnd.openofficeorg.extension"
            val rar = "application/x-rar-compressed"
            val zip = "application/zip"

            // For all
            val all = "file/*"
        }
    }

    fun getFileCategory(uri: String): String {
        val mimeType = getMimeType(uri)
        Log.i(TAG, "getFileCategory. " + mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
        return mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    interface FileCategory {
        companion object {
            val application = "application"
            val audio = "audio"
            val image = "image"
            val text = "text"
            val video = "video"
        }
    }
}
