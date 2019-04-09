package ir.fanap.chattestapp.application.ui.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.fanap.chattestapp.R
import android.R.attr.start
import android.view.ViewAnimationUtils
import android.animation.Animator
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.design.circularreveal.CircularRevealCompat
import android.support.v7.widget.CardView


class ChatFragment : Fragment() {
    private lateinit var atach_file: AppCompatImageView

    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /*private <T extends View & CircularRevealWidget> void circularRevealFromMiddle(@NonNull final T circularRevealWidget) {
    circularRevealWidget.post(new Runnable() {
        @Override
        public void run() {
            int viewWidth = circularRevealWidget.getWidth();
            int viewHeight = circularRevealWidget.getHeight();

            int viewDiagonal = (int) Math.sqrt(viewWidth * viewWidth + viewHeight * viewHeight);

            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    CircularRevealCompat.createCircularReveal(circularRevealWidget, viewWidth / 2, viewHeight / 2, 10, viewDiagonal / 2),
                    ObjectAnimator.ofArgb(circularRevealWidget, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, Color.RED, Color.TRANSPARENT));

            animatorSet.setDuration(5000);
            animatorSet.start();
        }
    });
}*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        var cicural_card: CardView = view.findViewById(R.id.ccv_attachment_reveal)
        val isOpen = false
        atach_file = view.findViewById(R.id.atach_file)
        atach_file.setOnClickListener {

            if (!isOpen) {

                val x = cicural_card.right
                val y = cicural_card.bottom

                val startRadius = 0
                val endRadius = Math.hypot(cicural_card.width.toDouble(), cicural_card.height.toDouble())

                val anim: Animator = CircularRevealCompat.createCircularReveal(layoutButtons, x, y, startRadius, endRadius)

                layoutButtons.setVisibility(View.VISIBLE)
                anim.start()

                isOpen = true

            } else {

                int x = layoutButtons.getRight ()
                int y = layoutButtons.getBottom ()

                int startRadius = Math . max (layoutContent.getWidth(), layoutContent.getHeight())
                int endRadius = 0


                Animator anim = ViewAnimationUtils . createCircularReveal (layoutButtons, x, y, startRadius, endRadius)
                anim.addListener(new Animator . AnimatorListener () {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.start()

                isOpen = false
            }
            return view
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }
    }