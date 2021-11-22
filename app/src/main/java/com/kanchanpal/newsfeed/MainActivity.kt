package com.kanchanpal.newsfeed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.kanchanpal.newsfeed.databinding.ActivityMainBinding
import com.kanchanpal.newsfeed.helper.UserStorage
import com.kanchanpal.newsfeed.news.NewsListFragmentDirections
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity() , HasSupportFragmentInjector{
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var navController: NavController
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    lateinit var userStorage: UserStorage
    private var mTime: Long = 30000

    override fun supportFragmentInjector(): AndroidInjector<Fragment>  = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        userStorage = UserStorage(this)
        navController = findNavController(R.id.nav_fragment)
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable {
            Toast.makeText(applicationContext, "User inactive for ${mTime/1000} secs!, logging out . . .", Toast.LENGTH_LONG).show()
            if (userStorage.getUser() != null){
                userStorage.delUser()
            }
            navController.navigate(R.id.action_to_splash)

        }

        // Start the handler
        startHandler()
    }

    override fun onUserInteraction() {

        super.onUserInteraction()
        // Removes the handler callbacks (if any)
        stopHandler()

        // Runs the handler (for the specified time)
        // If any touch or motion is detected before
        // the specified time, this override function is again called
        startHandler()
    }
    // When the screen is touched or motion is detected
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        // Removes the handler callbacks (if any)
//        stopHandler()
//
//        // Runs the handler (for the specified time)
//        // If any touch or motion is detected before
//        // the specified time, this override function is again called
//        startHandler()
//
//        return super.onTouchEvent(event)
//    }

    // start handler function
    private fun startHandler(){
        mHandler.postDelayed(mRunnable, mTime)
    }

    // stop handler function
    private fun stopHandler(){
        mHandler.removeCallbacks(mRunnable)
    }
}
