package com.deviprasaddayal.depiciat.listeners

import java.io.File

interface OnFileActionListener {
    fun onFilePathCreatedForCamera(imagePath: String, imageFile: File)

    fun onFileAddRequest()

    fun onFileViewRequest(position: Int)

    fun onFileDeleted(position: Int, removedFileName: String)
}
