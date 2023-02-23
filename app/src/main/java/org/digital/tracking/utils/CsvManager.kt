package org.digital.tracking.utils


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets


object CsvManager {

    private const val TAG = "CsvWriter"

//    @Throws(IOException::class, JSONException::class)
//    fun saveCsv(outerArray: JSONArray) {
//        val rootPath: String = Environment.getExternalStorageDirectory().absolutePath + "/test/"
//        val dir = File(rootPath)
//        if (!dir.exists()) {
//            dir.mkdir()
//        }
//        var file: File? = null
//        file = File(rootPath, "test4.csv")
//        if (!file.exists()) {
//            file.createNewFile()
//        }
//        var a = 0
//        if (file.exists()) {
//            val writer = CSVWriter(FileWriter(file), ',')
//            for (i in 0 until outerArray.length() - 1) {
//                val innerJsonArray = outerArray.getJSONObject(i) as JSONObject
//                var arrayOfArrays: Array<String?>? = arrayOfNulls(innerJsonArray.length())
//                val stringArray1 = arrayOfNulls<String>(innerJsonArray.length())
//                stringArray1[0] = innerJsonArray.getString("type")
//                stringArray1[1] = innerJsonArray.getString("title")
//                stringArray1[2] = ""
//                val jsonArray = innerJsonArray.getJSONArray("answer") as JSONArray
//                for (j in 0 until jsonArray.length()) {
//                    stringArray1[2] += jsonArray[j].toString()
//                    stringArray1[2] += ","
//                }
//                arrayOfArrays = stringArray1
//                a++
//                writer.writeNext(arrayOfArrays)
//            }
//            writer.close()
//        }
//    }

    fun loadJSONFromAsset(activity: Context, jsonFile: String): JSONObject? {
        val json: String
        val jsonObject: JSONObject
        try {
            var buffer: ByteArray
            activity.applicationContext.assets.open(jsonFile).use { `is` ->
                val size = `is`.available()
                buffer = ByteArray(size)
                val byteSize = `is`.read(buffer)
            }
            json = String(buffer, StandardCharsets.UTF_8)
            jsonObject = JSONObject(json)
        } catch (ex: Exception) {
            Log.e(TAG, "exception: " + ex.message)
            return null
        }
        return jsonObject
    }

    fun writeFile(context: Context, jsonString: String): File {
        val jsonTree = ObjectMapper().readTree(jsonString)
        val csvSchemaBuilder: CsvSchema.Builder = CsvSchema.builder()
        val firstObject = jsonTree.elements().next()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            firstObject.fieldNames().forEachRemaining { fieldName: String? ->
                csvSchemaBuilder.addColumn(fieldName)
            }
        } else {
            firstObject.fieldNames().forEach { fieldName: String? ->
                csvSchemaBuilder.addColumn(fieldName)
            }
        }
        val csvSchema: CsvSchema = csvSchemaBuilder.build().withHeader()
        val filesDir: File? = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            Environment.getExternalStorageDirectory()
        }
        if (!filesDir!!.exists()) {
            val mkdirs = filesDir.mkdirs()
            Log.i("CsvWriter", "Directory has been created: $mkdirs")
        }
        val saveFile = File(filesDir, "Demo.csv")
        val csvMapper = CsvMapper()
        csvMapper.writerFor(JsonNode::class.java)
            .with(csvSchema)
            .writeValue(saveFile, jsonTree)
        return saveFile
    }


}