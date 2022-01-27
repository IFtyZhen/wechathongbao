package top.rowesun.wechathongbao.service

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import top.rowesun.wechathongbao.Config
import top.rowesun.wechathongbao.Runtime

class AutoOpenLuckyMoneyService : AccessibilityService() {

    private val tag = AutoOpenLuckyMoneyService::class.java.simpleName

    /** 是点开了红包 */
    private var isClicked = false

    /** 是正在开红包 */
    private var isOpening = false

    override fun onServiceConnected() {
        serviceInfo.packageNames = arrayOf(Config.WechatPackageName)
        Runtime.IsConnected = true
        Log.d(tag, "Connected..")
    }

    override fun onDestroy() {
        Runtime.IsConnected = false
        Log.d(tag, "Destroy..")
    }

    override fun onInterrupt() {
        Log.d(tag, "Interrupt..")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (Runtime.IsPaused) return

        when {
            event.className == Config.HongBaoReceiveClassName -> {
                //点开了红包UI
                Log.d(tag, "Show Hongbao UI")
                isClicked = true
            }
            isOpening && event.className == Config.HongBaoDetailClassName -> {
                //在红包详情界面 抢到红包了 返回聊天界面
                Log.d(tag, "Successful open. back")
                performGlobalAction(GLOBAL_ACTION_BACK)
                isOpening = false
            }
            isClicked -> {
                isOpening = if (openHongbao(rootInActiveWindow ?: return)) {
                    //点击开按钮
                    Log.d(tag, "Open Hongbao")
                    true
                } else {
                    //不能开 返回聊天界面
                    Log.d(tag, "Cannot open. back")
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    false
                }
                isClicked = false
            }
            findAndClickHongbao(rootInActiveWindow ?: return) -> {
                //找到并点击红包
                Log.d(tag, "Click Hongbao")
            }
        }
    }

    private fun openHongbao(nodeInfo: AccessibilityNodeInfo): Boolean {
        val nodes = nodeInfo.findAccessibilityNodeInfosByViewId(Config.OpenButtonResId)
        if (nodes.size == 0) return false

        val openBtn = nodes[0]

        //过滤不能点击
        if (!openBtn.isClickable) return false
        openBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        return true
    }

    private fun findAndClickHongbao(nodeInfo: AccessibilityNodeInfo): Boolean {
        val list = nodeInfo.findAccessibilityNodeInfosByViewId(Config.HongBaoLayoutResId) ?: return false

        val rootRect = Rect()
        nodeInfo.getBoundsInScreen(rootRect)
        val width = rootRect.width()

        for (i in list.size - 1 downTo 0) {
            val node = list[i]

            //根据左下角“微信红包”资源id过滤红包消息
            if (node.findAccessibilityNodeInfosByViewId(Config.HongBaoTextResId).size == 0) continue

            //过滤已领取|已过期
            if (node.findAccessibilityNodeInfosByViewId(Config.HongBaoExpiredResId).size > 0) continue

            //过滤自己发的
            if (Runtime.NeedFilterSelf) {
                val rect = Rect()
                node.getBoundsInScreen(rect)
                //红包矩形位置离右边更近
                if (rect.left > width - rect.right) continue
            }

            if (!node.isClickable) continue
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        }
        return false
    }
}