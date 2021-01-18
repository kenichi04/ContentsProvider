package jp.techacademy.kenichi04.contentsprovider

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android6.0 以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            }
        // Android5 系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                // 許可ダイアログでユーザーが許可した場合
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    // 画像の情報を取得
    private fun getContentsInfo() {
        // ContentsResolver：ContentsProviderのデータを参照するクラス
        // Activityの継承元のContextのcontentResolverプロパティで取得
        val resolver = contentResolver
        // queryメソッドで条件指定して検索し情報を取得
        // 結果はCursorクラス：データベース上の検索結果を格納
        val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // データの種類:今回は外部ストレージの画像
                null,     // 項目（null = 全項目）
                null,      // フィルタ条件（null = フィルタなし）
                null,  // フィルタ用パラメータ
                null      // ソート（null = ソートなし）
        )

        // 検索結果の最初のデータを指す（戻り値がtrueの場合）
//        if (cursor!!.moveToFirst()) {
//            do {
//                // index からIDを取得し、そのIDから画像のURIを取得
//                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)  // 画像のIDがセットされている位置を取得
//                val id = cursor.getLong(fieldIndex)   // 画像のIDを取得
//                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//
//                Log.d("ANDROID", "URI: " + imageUri.toString())
//            } while (cursor.moveToNext())  // 次が情報を持っていればtrueが返る
//        }

        if (cursor!!.moveToFirst()) {
            // index からIDを取得し、そのIDから画像のURIを取得
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)  // 画像のIDがセットされている位置を取得
            val id = cursor.getLong(fieldIndex)   // 画像のIDを取得
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }

        cursor.close()
    }

}