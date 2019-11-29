package com.lsy.base

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.lsy.utils.LogUtil
import java.lang.ref.WeakReference
import kotlin.jvm.javaClass as javaClass1

/**
 * 生命周期
 */
class LifeCircleMgr private constructor(app: Application) {

    fun init() {
        registerActivity()
    }

    companion object {

        private var mInstance: LifeCircleMgr? = null

        fun getInstance(app: Application): LifeCircleMgr {
            if (mInstance == null) {
                mInstance = LifeCircleMgr(app)
            }
            return mInstance as LifeCircleMgr
        }
    }


    private val mApp = app
    val mActivities: ArrayList<WeakReference<Activity?>> = ArrayList()
    var mCurrentActivity: WeakReference<Activity>? = null

    private fun registerActivity() {
        mApp.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                LogUtil.d(
                    "Framework",
                    "onActivityCreated  : activity is ${activity.javaClass1.simpleName}"
                )
                mActivities.add(WeakReference(activity))
            }

            override fun onActivityResumed(activity: Activity) {
                LogUtil.d(
                    "Framework",
                    "onActivityResumed before\nthe Activity is ${activity.javaClass1.simpleName}\n" +
                            "currentActivity is ${mCurrentActivity?.get()?.javaClass1?.simpleName}"
                )
                if (mCurrentActivity?.get()?.javaClass1?.simpleName != activity.javaClass1.simpleName) {
                    /* 如果类名相同，就不用重新赋值了 */
                    mCurrentActivity = WeakReference(activity)
                }
                LogUtil.d(
                    "Framework",
                    "onActivityResumed after\nthe Activity is ${activity.javaClass1.simpleName}\n" +
                            "currentActivity is ${mCurrentActivity?.get()?.javaClass1?.simpleName}"
                )
            }

            override fun onActivityDestroyed(activity: Activity) {
                LogUtil.d(
                    "Framework",
                    "onActivityDestroyed  : activity is ${activity.javaClass1.simpleName}"
                )
                for (item in mActivities) {
                    if (item.get() == activity) {
                        mActivities.remove(item)
                        break
                    }
                }

            }

            override fun onActivityPaused(activity: Activity) {
                LogUtil.d(
                    "Framework",
                    "onActivityPaused  : activity is ${activity.javaClass1.simpleName}"
                )
            }

            override fun onActivityStarted(activity: Activity) {
                LogUtil.d(
                    "Framework",
                    "onActivityStarted  : activity is ${activity.javaClass1.simpleName}"
                )
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                LogUtil.d(
                    "Framework",
                    "onActivitySaveInstanceState  : activity is ${activity.javaClass1.simpleName}"
                )
            }

            override fun onActivityStopped(activity: Activity) {
                LogUtil.d(
                    "Framework",
                    "onActivityStopped  : activity is ${activity.javaClass1.simpleName}"
                )
            }
        })
    }

    /**
     * @param cls The class must extends [Activity] or subclass of it.
     * @return If the activity is exist in [.mActivities], return true.
     */
    fun isExist(cls: Class<*>): Boolean {
        for (activity in mActivities) {
            if (activity.get()?.javaClass1?.simpleName == cls.simpleName) {
                return true
            }
        }
        return false
    }

    /**
     * Finish all mActivities except an appointed list.
     *
     * @param except The exceptional list.
     */
    fun finishAllActivities(vararg except: Class<*>) {
        for (activity in mActivities) {
            for (c in except) {
                if (activity.get()?.javaClass1?.name != c.name) {
                    activity.get()?.finish()
                }
            }
        }
    }
}