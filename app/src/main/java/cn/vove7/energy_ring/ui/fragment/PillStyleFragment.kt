package cn.vove7.energy_ring.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.vove7.energy_ring.R
import cn.vove7.energy_ring.floatwindow.FloatRingWindow
import cn.vove7.energy_ring.listener.PowerEventReceiver
import cn.vove7.energy_ring.listener.RotationListener
import cn.vove7.energy_ring.ui.adapter.ColorsAdapter
import cn.vove7.energy_ring.util.Config
import cn.vove7.energy_ring.util.antiColor
import cn.vove7.energy_ring.util.pickColor
import kotlinx.android.synthetic.main.fragment_pill_style.bg_color_view
import kotlinx.android.synthetic.main.fragment_pill_style.charging_rotateDuration_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.color_list
import kotlinx.android.synthetic.main.fragment_pill_style.default_rotateDuration_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.posx_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.posy_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.size_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.spacing_seek_bar
import kotlinx.android.synthetic.main.fragment_pill_style.strokeWidth_seek_bar

/**
 * # PillStyleFragment
 *
 * @author Vove
 * 2020/5/12
 */
class PillStyleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pill_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        color_list.adapter = ColorsAdapter()
        refreshData()
        listenSeekBar(view)
    }
    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {

        bg_color_view.setBackgroundColor(Config.ringBgColor)
        bg_color_view.setTextColor(Config.ringBgColor.antiColor)

        strokeWidth_seek_bar?.progress = Config.strokeWidthF.toInt()
        posx_seek_bar?.progress = Config.posXf
        posy_seek_bar?.progress = Config.posYf
        size_seek_bar?.progress = Config.size
        charging_rotateDuration_seek_bar?.progress = (charging_rotateDuration_seek_bar.maxVal + 1 - Config.chargingRotateDuration / 1000)
        default_rotateDuration_seek_bar?.progress = (default_rotateDuration_seek_bar.maxVal + default_rotateDuration_seek_bar.minVal -
                (Config.defaultRotateDuration) / 1000)
        spacing_seek_bar?.progress = Config.spacingWidthF
        color_list?.adapter?.notifyDataSetChanged()

    }

    private fun listenSeekBar(view: View): Unit = view.run {

        bg_color_view.setOnClickListener {
            pickColor(context!!, initColor = Config.ringBgColor) { c ->
                bg_color_view.setBackgroundColor(c)
                bg_color_view.setTextColor(c.antiColor)
                Config.ringBgColor = c
                FloatRingWindow.update()
            }
        }
        charging_rotateDuration_seek_bar?.onStop { progress ->
            Config.chargingRotateDuration = (charging_rotateDuration_seek_bar.maxVal + 1 - progress) * 1000
            if (PowerEventReceiver.isCharging) {
                FloatRingWindow.reloadAnimation()
            }
        }
        default_rotateDuration_seek_bar?.onStop { progress -> //[60,180]
            Config.defaultRotateDuration = (default_rotateDuration_seek_bar.maxVal - (progress - default_rotateDuration_seek_bar.minVal)) * 1000
            if (!PowerEventReceiver.isCharging) {
                FloatRingWindow.reloadAnimation()
            }
        }
        strokeWidth_seek_bar?.onChange { progress, user ->
            if (!user) return@onChange
            Config.strokeWidthF = progress.toFloat()
            FloatRingWindow.update()
        }
        posx_seek_bar?.onChange { progress, user ->
            if (!user) return@onChange
            Config.posXf = progress
            FloatRingWindow.update()
        }
        posy_seek_bar?.onChange { progress, user ->
            if (!user) return@onChange
            Config.posYf = progress
            FloatRingWindow.update()
        }
        size_seek_bar?.onChange { progress, user ->
            if (!user) return@onChange
            Config.size = progress
            FloatRingWindow.update()
        }
        spacing_seek_bar?.onChange { progress, user ->
            if (!user) return@onChange
            Config.spacingWidthF = progress
            FloatRingWindow.update()
        }
    } ?: Unit

}