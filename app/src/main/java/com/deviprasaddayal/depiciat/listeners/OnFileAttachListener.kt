package com.deviprasaddayal.depiciat.listeners

import java.io.File

interface OnFileAttachListener {
    fun onFileAttached(path: String, file: File)
}
