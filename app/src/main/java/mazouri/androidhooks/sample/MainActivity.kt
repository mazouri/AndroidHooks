package mazouri.androidhooks.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import mazouri.androidhooks.click.HookOnClickListener

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            Log.d(TAG, "click")
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }

        HookOnClickListener.hook(this, button, { before(button) }, { after()})
    }

    private fun before(view: View) {
        Log.d(TAG, "before: ${view.id}")
//        Toast.makeText(this, "before", Toast.LENGTH_SHORT).show()
    }

    private fun after() {
        Log.d(TAG, "after")
//        Toast.makeText(this, "after", Toast.LENGTH_SHORT).show()
    }
}