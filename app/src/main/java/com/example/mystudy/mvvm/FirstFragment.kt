package com.example.mystudy.mvvm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.transition.ChangeBounds
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.example.mystudy.R
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_third.*

class FirstFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        L.e("FirstFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.e("FirstFragment", "onViewCreated")
        tvFirst.setOnClickListener {
            findNavController().navigate(R.id.action_first_to_second, Bundle().apply {
                putString("data", "first")
            }, navOptions {
                this.launchSingleTop = true
                this.popUpTo = R.id.secondFragment
            }, FragmentNavigatorExtras(tvFirst to "tvFirst"))
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        L.e("FirstFragment", "onHiddenChanged")
    }

    override fun onResume() {
        super.onResume()
        L.e("FirstFragment", "onResume")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        L.e("FirstFragment", "setUserVisibleHint")
    }

}

class SecondFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        L.e("SecondFragment", "onCreateView")

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 2000
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 2000
        }
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val data = it.getString("data")
            L.e("SecondFragment", "onCreateView->$data")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvParent.transitionName = "tvFirst"
            }
            tvParent.text = data
        }
        tvSecond.setOnClickListener {
            findNavController().navigate(R.id.action_second_to_third)
        }
    }
}

class ThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvThird.setOnClickListener {
            findNavController().navigate(R.id.action_third_to_first)
        }
    }

}
