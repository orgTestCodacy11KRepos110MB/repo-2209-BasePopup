package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupSelectAnimateBinding;
import razerdp.demo.utils.FillViewUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：选择动画
 */
public class PopupSelectDismissAnimate extends BasePopupWindow {


    PopupSelectAnimateBinding mBinding;

    OnSelectedResultListener mListener;

    List<Info> animations = new ArrayList<>();

    public PopupSelectDismissAnimate(Context context) {
        super(context);
        setContentView(R.layout.popup_select_animate);
        setMaxHeight(UIHelper.getScreenHeight() >> 1);
        generateAnimation();
        FillViewUtil.fillView(animations,
                              mBinding.layoutAnimation,
                              R.layout.item_popup_animate,
                              creator);
        mBinding.tvGo.setOnClickListener(v -> ok());
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupSelectAnimateBinding.bind(contentView);
    }

    private void generateAnimation() {
        Info info = new Info();
        info.name = "AlphaOut";
        info.animation = AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.OUT)
                .toDismiss();
        animations.add(info);

        info = new Info();
        info.name = "ScaleOut";
        info.animation = AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromLeft";
        info.animation = createScaleAnimation(1f, 0f, 1f, 1f, 0f, 0.5f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromTop";
        info.animation = createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromRight";
        info.animation = createScaleAnimation(1f, 0f, 1f, 1f, 1f, 0.5f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromBottom";
        info.animation = createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 1f);
        animations.add(info);


        info = new Info();
        info.name = "ScaleOutFromLeftTop";
        info.animation = createScaleAnimation(1, 0f, 1f, 0f, 0f, 0f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromLeftBottom";
        info.animation = createScaleAnimation(1f, 0f, 1f, 0f, 0f, 1f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromRightTop";
        info.animation = createScaleAnimation(1f, 0f, 1f, 0f, 1f, 0f);
        animations.add(info);

        info = new Info();
        info.name = "ScaleOutFromRightBottom";
        info.animation = createScaleAnimation(1f, 0f, 1f, 0f, 1f, 1f);
        animations.add(info);


        info = new Info();
        info.name = "TranslateFromTop";
        info.animation = AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_TOP)
                .toDismiss();
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromLeft";
        info.animation = getHorizontalAnimation(0f, -1f, 500);
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromRight";
        info.animation = getHorizontalAnimation(0f, -1f, 500);
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromBottom";
        info.animation = AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromLeftTop";
        info.animation = createTranslateAnimation(0f, -1f, 0f, -1f);
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromLeftBottom";
        info.animation = createTranslateAnimation(0f, -1f, 0f, 1f);
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromRightTop";
        info.animation = createTranslateAnimation(0f, 1f, 0f, -1f);
        animations.add(info);

        info = new Info();
        info.name = "TranslateFromRightBottom";
        info.animation = createTranslateAnimation(0f, 1f, 0f, 1f);
        animations.add(info);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }


    Animation getHorizontalAnimation(float start, float end, int durationMillis) {
        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                                                              start,
                                                              Animation.RELATIVE_TO_PARENT,
                                                              end,
                                                              Animation.RELATIVE_TO_PARENT,
                                                              0,
                                                              Animation.RELATIVE_TO_PARENT,
                                                              0);
        translateAnimation.setDuration(durationMillis);
        return translateAnimation;
    }

    Animation createTranslateAnimation(float fromX, float toX, float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                                                     fromX,
                                                     Animation.RELATIVE_TO_SELF,
                                                     toX,
                                                     Animation.RELATIVE_TO_SELF,
                                                     fromY,
                                                     Animation.RELATIVE_TO_SELF,
                                                     toY);
        animation.setDuration(500);
        return animation;
    }

    Animation createScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotXValue, float pivotYValue) {
        Animation scaleAnimation = new ScaleAnimation(fromX,
                                                      toX,
                                                      fromY,
                                                      toY,
                                                      Animation.RELATIVE_TO_SELF,
                                                      pivotXValue,
                                                      Animation.RELATIVE_TO_SELF,
                                                      pivotYValue);
        scaleAnimation.setDuration(500);
        return scaleAnimation;
    }

    void ok() {
        Info animation = null;

        for (Info info : animations) {
            if (info.selected) {
                animation = info;
                break;
            }
        }

        if (mListener != null) {
            if (animation != null) {
                mListener.onSelected(animation.name, animation.animation);
            } else {
                mListener.onSelected(null, null);
            }
        }
        dismiss();
    }

    void notifyDataSetChange(Info info) {
        if (info != null) {
            for (Info animation : animations) {
                if (animation != info) {
                    animation.selected = false;
                }
            }
        }
        final int childCount = mBinding.layoutAnimation.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = mBinding.layoutAnimation.getChildAt(i);
            InnerViewHolder holder = ToolUtil.cast(FillViewUtil.getHolder(v),
                                                   InnerViewHolder.class);
            if (holder != null) {
                holder.update();
            }
        }

    }

    private FillViewUtil.OnFillViewsListener<Info, InnerViewHolder> creator = new FillViewUtil.OnFillViewsListener<Info, InnerViewHolder>() {
        @Override
        public InnerViewHolder onCreateViewHolder(View itemView, Info data, int position) {
            return new InnerViewHolder(itemView);
        }
    };

    class InnerViewHolder extends FillViewUtil.FillViewHolder<Info> {

        TextView tv;

        InnerViewHolder(View itemView) {
            super(itemView);
            tv = findViewById(R.id.tv_tag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData().selected = !getData().selected;
                    notifyDataSetChange(getData());
                }
            });
        }

        @Override
        public void onBindData(Info data, int position, boolean isLast) {
            update();
        }

        public void update() {
            tv.setSelected(getData().selected);
            tv.setText(getData().name);
        }
    }

    public PopupSelectDismissAnimate setOnSelectedResultListener(OnSelectedResultListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public interface OnSelectedResultListener {
        void onSelected(@Nullable String name, @Nullable Animation animation);
    }


    static class Info {
        String name;
        Animation animation;
        boolean selected;
    }
}
