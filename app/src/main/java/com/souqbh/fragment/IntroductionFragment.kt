package com.souqbh.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.souqbh.data.other.Introduction
import com.souqbh.databinding.FragmentIntroductionBinding
import com.souqbh.utils.constants.AppConstants

class IntroductionFragment : Fragment() {

    private lateinit var binding: FragmentIntroductionBinding

    companion object {
        fun newInstance(bundle: Bundle) = IntroductionFragment().apply {
            arguments = bundle
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val introduction: Introduction = arguments!!.get(AppConstants.INTRODUCTION) as Introduction
        binding.introduction = introduction
    }
}