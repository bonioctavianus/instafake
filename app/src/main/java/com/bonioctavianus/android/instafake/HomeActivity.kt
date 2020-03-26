package com.bonioctavianus.android.instafake

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bonioctavianus.android.instafake.utils.CheckPermission
import com.bonioctavianus.android.instafake.utils.PermissionGranted
import com.bonioctavianus.android.instafake.utils.isPermissionGranted
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var mEventBus: EventBus

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        title = ""
    }

    override fun onStart() {
        super.onStart()
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this)
        }
    }

    @Subscribe
    fun onEvent(event: CheckPermission) {
        if (isPermissionGranted()) {
            mEventBus.post(PermissionGranted)
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            createRequestPermissionDialog()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun createRequestPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_request_permission_title)
            .setCancelable(false)
            .setMessage(R.string.dialog_request_permission_description)
            .setPositiveButton(R.string.dialog_request_permission_positive_action) { _, _ ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                    )
                )
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    mEventBus.post(PermissionGranted)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this)
        }
    }
}
