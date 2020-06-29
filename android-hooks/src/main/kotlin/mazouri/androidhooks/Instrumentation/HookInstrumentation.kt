package mazouri.androidhooks.Instrumentation

import android.app.Instrumentation

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/24 4:11 PM
 * @Version: 1.0
 */
object HookInstrumentation {

    fun hookActivityRenderTime() {
        try {
            // 1.根据需求确定 要hook的对象——mInstrumentation：getDeclaredField
            // 2.寻找要hook的对象的持有者——ActivityThread-currentActivityThread()-mInstrumentation：Class.forName、activityThread.getDeclaredMethod
            val activityThread = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThread = activityThread.getDeclaredMethod("currentActivityThread")
            sCurrentActivityThread.isAccessible = true
            val activityThreadObject = sCurrentActivityThread.invoke(activityThread)
            val field = activityThread.getDeclaredField("mInstrumentation")
            field.isAccessible = true
            // 3.拿到要hook的对象——field[]
            val instrumentation =
                field[activityThreadObject] as Instrumentation
            //4.定义“要hook的对象”的代理类，并且创建该类的对象
            //5.使用上一步创建出来的对象，替换掉要hook的对象，达成 偷梁换柱的最终目的——field[]
            field[activityThreadObject] = ActivityRenderTimeInstrumentation(instrumentation)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}