package mazouri.androidhooks.click

import android.content.Context
import android.view.View
import java.lang.reflect.Proxy

/**
 * @Description:
 * @Author: wangdongdong
 * @Email: wangdd_bj@163.com
 * @Github: https://github.com/mazouri
 * @CSDN: https://blog.csdn.net/dongdong230
 * @CreateDate: 2020/6/24 3:10 PM
 * @Version: 1.0
 */
object HookOnClickListener {

    fun hook(context: Context, v: View, before: (v: View?)-> Unit = {}, after: (v: View?)-> Unit = {}) {
        try {
            val method = View::class.java.getDeclaredMethod("getListenerInfo").also {
                it.isAccessible = true
            }
            val mListenerInfo = method.invoke(v)

            val listenerInfoClz = Class.forName("android.view.View\$ListenerInfo")
            val field = listenerInfoClz.getDeclaredField("mOnClickListener")
            val onClickListenerInstance = field.get(mListenerInfo) as View.OnClickListener

            val proxyOnClickListener = Proxy.newProxyInstance(
                context.applicationContext.classLoader, arrayOf<Class<*>>(View.OnClickListener::class.java)
            ) { _, method, args ->
                before(v)
                // onClick argument 1 has type android.view.View, got java.lang.Object[]
                val result = method.invoke(onClickListenerInstance, args[0])
                after(v)
                result
            }

            field.set(mListenerInfo, proxyOnClickListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}