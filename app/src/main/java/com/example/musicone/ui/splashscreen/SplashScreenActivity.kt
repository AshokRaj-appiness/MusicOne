package com.example.musicone.ui.splashscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.musicone.ui.activities.MainActivity
import com.example.musicone.R

class SplashScreenActivity : AppCompatActivity() {

    var permissionStrings = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.PROCESS_OUTGOING_CALLS,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if(!hasPermissions(this@SplashScreenActivity,*permissionStrings)){
            ActivityCompat.requestPermissions(this,permissionStrings,131)
        }else{
            Handler().postDelayed({
                val intent = Intent(this@SplashScreenActivity,
                    MainActivity::class.java)
                startActivity(intent)
                this.finish()
            },1000)
        }
    }

    fun hasPermissions(context:Context,vararg permissions:String):Boolean{
        var hasAllPermissions = true
        for(permission in permissions){
            val res = context.checkCallingOrSelfPermission(permission)
            if(res != PackageManager.PERMISSION_GRANTED){
                hasAllPermissions = false
            }
        }
        return hasAllPermissions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            131 ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED
                ){
                    Handler().postDelayed({
                        val intent = Intent(this@SplashScreenActivity,
                            MainActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    },1000)
                }else{
                    Toast.makeText(this,"Please grant all permissions",Toast.LENGTH_SHORT).show()
                }

            }
            else ->{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }

    }
}
