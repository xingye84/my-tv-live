package com.lizongying.mytv

import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.ImageView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.lizongying.mytv.models.TVViewModel

class CardPresenter(
    private val owner: LifecycleOwner,
) : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = object :
            ImageCardView(ContextThemeWrapper(parent.context, R.style.CustomImageCardTheme)) {}

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val tvViewModel = item as TVViewModel
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = tvViewModel.getTV().title
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        cardView.tag = tvViewModel.videoUrl.value

        Glide.with(viewHolder.view.context)
            .load(tvViewModel.getTV().logo)
            .centerInside()
            .into(cardView.mainImageView)

        cardView.setBackgroundColor(Color.WHITE)
        cardView.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE)

        val epg = tvViewModel.epg.value?.filter { it.beginTime < Utils.getDateTimestamp() }
        if (!epg.isNullOrEmpty()) {
            cardView.contentText = epg.last().title
        } else {
            cardView.contentText = ""
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImage = null
    }

    companion object {
        private const val TAG = "CardPresenter"
        private const val CARD_WIDTH = 300
        private const val CARD_HEIGHT = 101
    }
}