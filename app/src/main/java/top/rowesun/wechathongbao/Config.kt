package top.rowesun.wechathongbao

object Config {
    /** 微信包名 */
    const val WechatPackageName = "com.tencent.mm"

    /** 点开红包弹窗类名 */
    const val HongBaoReceiveClassName = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI"

    /** 红包详情类名 */
    const val HongBaoDetailClassName = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI"

    /** 红包父布局资源id */
    const val HongBaoLayoutResId = "com.tencent.mm:id/b47"

    /** 左下角“微信红包”资源id */
    const val HongBaoTextResId = "com.tencent.mm:id/y4"

    /** 中间的“已过期|以领取”资源id */
    const val HongBaoExpiredResId = "com.tencent.mm:id/xs"

    /** “开”图片按钮资源id */
    const val OpenButtonResId = "com.tencent.mm:id/gix"
}