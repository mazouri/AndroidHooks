package mazouri.androidhooks.sample

import android.app.Application
import mazouri.androidhooks.Instrumentation.HookInstrumentation

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/24 4:43 PM
 * @Version: 1.0
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        HookInstrumentation.hookActivityRenderTime()
    }
}