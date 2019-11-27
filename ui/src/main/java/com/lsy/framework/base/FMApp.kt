package com.lsy.framework.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/**
 * 建议做法, 继承该类的 app 中不要包含第三方初始化，
 * 引入 ConfigManager 和 ParamsManager 来做初始化，保证 application的干净整洁
 */
open class FMApp : Application() {

    // The list of all mActivities.
    private val mActivities = LinkedList<Activity?>()

    override fun onCreate() {
        super.onCreate()
        /* 我们使用的 utils包下的 context 的初始化*/
        registerActivity()
    }

    protected open fun initConfig(isDebug: Boolean, globalTag: String) {
        /* log 初始化 */

    }

    /**
     * 这里管理 activity ，即不需要再activity中去做处理
     */
    private fun registerActivity() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                mActivities.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                mActivities.add(activity)
            }
        })
    }

    /**
     * @param cls The class must extends [Activity] or subclass of it.
     * @return If the activity is exist in [.mActivities], return true.
     */
    fun isExist(cls: Class<*>): Boolean {
        for (activity in mActivities) {
            if (activity != null) {
                if (activity.javaClass.simpleName == cls.simpleName) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Finish mActivities in an appointed list.
     *
     * @param activities Activities to be finished.
     */
    fun finishActivity(vararg activities: Activity) {
        for (activity in activities) {
            activity.finish()
        }
    }

    /**
     * Finish all mActivities except an appointed list.
     *
     * @param except The exceptional list.
     */
    fun finishAllActivities(vararg except: Class<*>) {
        for (activity in mActivities) {
            if (activity != null) {
                for (c in except) {
                    if (activity.javaClass.name != c.name) {
                        activity.finish()
                    }
                }
            }
        }
    }

}