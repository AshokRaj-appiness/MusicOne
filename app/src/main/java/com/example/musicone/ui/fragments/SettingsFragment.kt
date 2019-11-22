package com.example.musicone.ui.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.musicone.R

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    var myActivity: Activity?=null

    object Statified{
        var MY_PREF_NAME = "shakeFeature"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pref = myActivity?.getSharedPreferences(Statified.MY_PREF_NAME,Context.MODE_PRIVATE)
        val isAllowed = pref?.getBoolean("feature",false)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myActivity = activity
    }


}
