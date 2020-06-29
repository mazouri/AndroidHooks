package mazouri.androidhooks.Instrumentation

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle
import android.os.Looper
import android.util.Log

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/24 4:15 PM
 * @Version: 1.0
 */
class ActivityRenderTimeInstrumentation constructor(private val base: Instrumentation): Instrumentation() {

    companion object {
        const val TAG = "ActivityRenderTime"
    }

    private var launchCreateBeginTimestamp: Long = 0

    private var pauseTotalTimestamp: Long = 0
    private var launchCreateTotalTimestamp: Long = 0
    private var launchStartTotalTimestamp: Long = 0
    private var renderTotalTimestamp: Long = 0

    override fun callActivityOnCreate(activity: Activity?, icicle: Bundle?) {
        launchCreateBeginTimestamp = System.currentTimeMillis()
        base.callActivityOnCreate(activity, icicle)
        launchCreateTotalTimestamp =
            System.currentTimeMillis() - launchCreateBeginTimestamp
    }

    override fun callActivityOnStart(activity: Activity?) {
        val launchStartBeginTimestamp = System.currentTimeMillis()
        base.callActivityOnStart(activity)
        launchStartTotalTimestamp = System.currentTimeMillis() - launchStartBeginTimestamp
    }

    override fun callActivityOnPause(activity: Activity?) {
        val pauseBeginTimestamp = System.currentTimeMillis()
        base.callActivityOnPause(activity)
        pauseTotalTimestamp = System.currentTimeMillis() - pauseBeginTimestamp
    }

    override fun callActivityOnResume(activity: Activity?) {
        val renderBeginTimestamp = System.currentTimeMillis()
        base.callActivityOnResume(activity)
        if (launchCreateBeginTimestamp == 0L) {
            return
        }

        Looper.myQueue().addIdleHandler {
            val className = activity!!.javaClass.simpleName
            renderTotalTimestamp = System.currentTimeMillis() - renderBeginTimestamp
            val launchAndRender =
                System.currentTimeMillis() - launchCreateBeginTimestamp
            Log.d(TAG, className +
                        " 耗时[" +
                        "总耗时:" + (launchAndRender + pauseTotalTimestamp) + ", " +
                        "上个页面Pause耗时:" + pauseTotalTimestamp + ", " +
                        "onCreated到onStart耗时:" + (launchCreateTotalTimestamp + launchStartTotalTimestamp) + ", " +
                        "渲染耗时（onResume开始到渲染完成）:" + renderTotalTimestamp + "]"
            )
            launchCreateBeginTimestamp = 0
            pauseTotalTimestamp = 0
            launchCreateTotalTimestamp = 0
            launchStartTotalTimestamp = 0
            renderTotalTimestamp = 0
            false
        }
    }
}