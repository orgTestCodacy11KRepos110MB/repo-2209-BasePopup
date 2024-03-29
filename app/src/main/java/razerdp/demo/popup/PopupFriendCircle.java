package razerdp.demo.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupFriendCircleCommentBinding;
import razerdp.demo.model.friendcircle.FriendCircleInfo;
import razerdp.demo.utils.ViewUtil;
import razerdp.demo.utils.rx.RxCall;
import razerdp.demo.utils.rx.RxHelper;

/**
 * Created by 大灯泡 on 2019/9/24
 * <p>
 * Description：朋友圈
 */
public class PopupFriendCircle extends BasePopupWindow {
    PopupFriendCircleCommentBinding mBinding;


    FriendCircleInfo info;
    ValueAnimator valueAnimator;

    public static boolean outSideTouch = false;
    public static boolean link = false;
    public static boolean blur = false;

    public PopupFriendCircle(Context context) {
        super(context);
        setContentView(R.layout.popup_friend_circle_comment);
        ViewUtil.setViewPivotRatio(mBinding.ivStar, 0.5f, 0.5f);
        setBackgroundColor(0);
        mBinding.tvStar.setOnClickListener(v -> onStarClick());
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupFriendCircleCommentBinding.bind(contentView);
    }


    @Override
    public void showPopupWindow(View anchorView) {
        setBlurBackgroundEnable(blur);
        if (outSideTouch) {
            setOutSideTouchable(true);
            setOutSideDismiss(false);
        } else {
            setOutSideDismiss(true);
            setOutSideTouchable(false);
        }
        linkTo(link ? anchorView : null);
        super.showPopupWindow(anchorView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                                                     1f,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     0,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     0,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(350);
        return animation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                                                     0f,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     1f,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     0,
                                                     Animation.RELATIVE_TO_PARENT,
                                                     0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(350);
        return animation;
    }

    public PopupFriendCircle setInfo(FriendCircleInfo info) {
        this.info = info;
        mBinding.tvStar.setText(info.started ? "取消" : "赞");
        return this;
    }

    void onStarClick() {
        info.started = !info.started;

        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(1f, 2f, 1f);
            valueAnimator.setDuration(400);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    mBinding.tvStar.setScaleX(scale);
                    mBinding.tvStar.setScaleY(scale);
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setInfo(info);
                    RxHelper.delay(150, new RxCall<Long>() {
                        @Override
                        public void onCall(Long data) {
                            dismiss();
                        }
                    });
                }
            });
        }
        valueAnimator.cancel();
        valueAnimator.start();

    }
}
