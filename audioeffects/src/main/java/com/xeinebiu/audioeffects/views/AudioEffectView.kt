package com.xeinebiu.audioeffects.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xeinebiu.audioeffects.AudioEffectManager
import com.xeinebiu.audioeffects.R

/**
 * Tabbed layout with [EqualizerView] and [BassView]
 * @author xeinebiu
 */
class AudioEffectView(
    private val fragmentManager: FragmentManager,
    private val context: Context,
    private val parent: ViewGroup?,
    private val audioEffectManager: AudioEffectManager,
) {

    /**
     * Create a tabbed layout with [EqualizerView] and [BassView]
     * @author xeinebiu
     */
    fun createView(): View {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_audio_effect, parent, false)
        val tabLayout: TabLayout = view.findViewById(R.id.view_audio_effects_tl_table)
        val viewPager: ViewPager = view.findViewById(R.id.view_audio_effect_vp_pager)

        val equalizerFragment = SimpleFragment().apply {
            onCreateViewCallback = { _, _ ->
                EqualizerView(requireContext(), parent, audioEffectManager).createView()
            }
        }

        val bassBoostFragment = SimpleFragment().apply {
            onCreateViewCallback = { _, _ ->
                BassView(requireContext(), parent, audioEffectManager).createView()
            }
        }

        val tabFragments = arrayListOf(equalizerFragment, bassBoostFragment)
        val tabFragmentsTitles = arrayListOf(
            context.getString(R.string.equalizer),
            context.getString(R.string.bass_boost),
        )
        val adapter = FragmentAdapter(fragmentManager, tabFragments, tabFragmentsTitles)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    /**
     * Adapter responsible to render [Fragment] as Pages
     * @author xeinebiu
     */
    private inner class FragmentAdapter(
        fragmentManager: FragmentManager,
        private val tabFragments: List<Fragment>,
        private val titleFragments: List<String>,
    ) : FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
    ) {

        override fun getItem(position: Int): Fragment = tabFragments[position]

        override fun getCount(): Int = tabFragments.size

        override fun getPageTitle(position: Int): CharSequence = titleFragments[position]
    }
}

/**
 * Reusable Fragment to show any [View]
 * @author xeinebiu
 */
class SimpleFragment : Fragment() {
    /**
     * Called when [onCreateView] is invoked from [Fragment] lifecycle
     * @author xeinebiu
     */
    var onCreateViewCallback: ((LayoutInflater, ViewGroup?) -> View?)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = onCreateViewCallback?.invoke(inflater, container)
}
