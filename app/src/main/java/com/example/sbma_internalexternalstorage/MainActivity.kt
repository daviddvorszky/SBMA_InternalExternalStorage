package com.example.sbma_internalexternalstorage

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

internal const val FILENAME = "myAssignmentFile.txt";

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val file = File(this.filesDir, FILENAME)
        var fileExists = file.exists()
        val lines = mutableListOf<String>()

        if(fileExists){
            Log.i("pengb", "File exists")
            file.forEachLine { lines.add(it)}
            Log.i("pengb", "Lines added")
        }else{
            Log.i("pengb", "File does not exist")
            val isFileCreated = file.createNewFile()
            if(isFileCreated){
                Log.i("pengb", "File created successfully")
            }else{
                Log.e("pengb", "Something went really wrong creating the file...")
            }
        }

        setContent {
            FileManager(app = this.application, lines)
        }
    }
}

@Composable
fun FileManager(app: Application, lines: MutableList<String>) {

    var text by remember { mutableStateOf("")}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
            }
        )
        Button(
            onClick = {
                app.openFileOutput(FILENAME, Context.MODE_APPEND).use {
                    it.write("$text\n".toByteArray())
                }
                lines.add(text)
                text = ""
                Log.i("pengb", "Line added")
            }
        ) {
            Text("Add line")
        }
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            items(lines){ line ->
                Text(line)
            }
        }
    }
}