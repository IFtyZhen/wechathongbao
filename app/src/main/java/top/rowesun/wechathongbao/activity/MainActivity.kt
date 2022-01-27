package top.rowesun.wechathongbao.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import top.rowesun.wechathongbao.R
import top.rowesun.wechathongbao.Runtime


class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var statusTextView: TextView
    private lateinit var optButton: Button
    private lateinit var filterSelfCheckBox: CheckBox

    /**
     * 运行状态
     * <li>0: 未授权</li>
     * <li>1: 运行中</li>
     * <li>2: 暂停中</li>
     */
    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusText)
        optButton = findViewById(R.id.optBtn)
        filterSelfCheckBox = findViewById(R.id.filterSelfBtn)
        filterSelfCheckBox.setOnCheckedChangeListener(this)
        filterSelfCheckBox.isChecked = true

        Log.d("main", "AppCreate")
    }

    override fun onStart() {
        super.onStart()
        updateState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0xff) {
            updateState()
        }
    }

    override fun onCheckedChanged(btn: CompoundButton?, v: Boolean) {
        Runtime.NeedFilterSelf = v
    }

    fun onClickButton(view: View) {
        when (status) {
            0 -> startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0xff)
            1 -> {
                Runtime.IsPaused = true
                updateState()
            }
            2 -> {
                Runtime.IsPaused = false
                updateState()
            }
        }
    }

    private fun updateState() {
        val statusText: Int
        val optBtnText: Int
        if (Runtime.IsConnected) {
            if (Runtime.IsPaused) {
                statusText = R.string.status_2
                optBtnText = R.string.opt_2
                status = 2
            } else {
                statusText = R.string.status_1
                optBtnText = R.string.opt_1
                status = 1
            }
        } else {
            statusText = R.string.status_0
            optBtnText = R.string.opt_0
            status = 0
        }

        statusTextView.setText(statusText)
        optButton.setText(optBtnText)
    }
}