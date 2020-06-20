package razerdp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;

import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2020/6/11
 * <p>
 * Description：动画构建
 */
public class PopupAnimationBuilder {

    private PopupAnimationBuilder() {
    }


    public static AnimationBuilder asAnimation() {
        return new AnimationBuilder();
    }

    public static AnimatorBuilder asAnimator() {
        return new AnimatorBuilder();
    }

    public enum Direction {
        LEFT(-1, 0, -1, 0, 0, 0),
        TOP(0, -1, 0, -1, 0, 0),
        RIGHT(1, 0, 1, 0, 1, 0),
        BOTTOM(0, 1, 0, 1, 0, 1),
        CENTER(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f),
        CENTER_HORIZONTAL(0.5f, 0, 0.5f, 0, 0.5f, 0),
        CENTER_VERTICAL(0, 0.5f, 0, 0.5f, 0, 0.5f);

        float transFromX, transFromY, transToX, transToY;
        float scalePivotX, scalePivotY;

        Direction(float transFromX, float transFromY, float transToX, float transToY, float scalePivotX, float scalePivotY) {
            this.transFromX = transFromX;
            this.transFromY = transFromY;
            this.transToX = transToX;
            this.transToY = transToY;
            this.scalePivotX = scalePivotX;
            this.scalePivotY = scalePivotY;
        }
    }

    public static abstract class AnimationApi<T> {
        Set<BaseConfig> configs;

        private void checkAndInitConfigSet() {
            if (configs == null) {
                configs = new HashSet<>();
            }
        }

        //-----------alpha-------------
        public T withAlpha(@NonNull AlphaConfig config) {
            if (config == null) {
                return (T) this;
            }
            checkAndInitConfigSet();
            configs.add(config);
            return (T) this;
        }

        //-----------scale-------------
        public T withScale(@NonNull ScaleConfig config) {
            if (config == null) {
                return (T) this;
            }
            checkAndInitConfigSet();
            configs.add(config);
            return (T) this;
        }

        //-----------translation-------------
        public T withTranslation(@NonNull TranslationConfig config) {
            if (config == null) {
                return (T) this;
            }
            checkAndInitConfigSet();
            configs.add(config);
            return (T) this;
        }
    }

    public static abstract class BaseConfig<T> {
        protected String TAG = this.getClass().getSimpleName();
        static final long DEFAULT_DURATION = Resources.getSystem().getInteger(android.R.integer.config_shortAnimTime);
        static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
        Interpolator interpolator = DEFAULT_INTERPOLATOR;
        long duration = DEFAULT_DURATION;
        float pivotX;
        float pivotY;
        float pivotX2;
        float pivotY2;
        boolean ignoreRevert;

        public T interpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
            return (T) this;
        }

        public T duration(long duration) {
            this.duration = duration;
            return (T) this;
        }

        public T pivot(@FloatRange(from = 0, to = 1) float x, @FloatRange(from = 0, to = 1) float y) {
            pivotX = x;
            pivotY = y;
            return (T) this;
        }


        public T pivotX(@FloatRange(from = 0, to = 1) float x) {
            pivotX = x;
            return (T) this;
        }

        public T pivotY(@FloatRange(from = 0, to = 1) float y) {
            pivotY = y;
            return (T) this;
        }


        void deploy(Animation animation) {
            if (animation == null) return;
            animation.setDuration(duration);
            animation.setInterpolator(interpolator);
        }

        void deploy(Animator animator) {
            if (animator == null) return;
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
        }

        void log() {
            PopupLog.i(TAG, $toString(), this.toString());
        }

        public String $toString() {
            return "BaseConfig{" +
                    "interpolator=" + interpolator +
                    ", duration=" + duration +
                    ", pivotX=" + pivotX +
                    ", pivotY=" + pivotY +
                    '}';
        }

        final Animation $buildAnimation(boolean isRevert) {
            log();
            return buildAnimation(isRevert);
        }

        final Animator $buildAnimator(boolean isRevert) {
            log();
            return buildAnimator(isRevert);
        }

        protected abstract Animation buildAnimation(boolean isRevert);

        protected abstract Animator buildAnimator(boolean isRevert);

    }

    public static class AlphaConfig extends BaseConfig<AlphaConfig> {
        float alphaFrom;
        float alphaTo;

        public AlphaConfig() {
            ignoreRevert = true;
        }

        public AlphaConfig from(@FloatRange(from = 0, to = 1) float from) {
            alphaFrom = from;
            return this;
        }

        public AlphaConfig to(@FloatRange(from = 0, to = 1) float to) {
            alphaTo = to;
            return this;
        }

        public AlphaConfig from(int from) {
            alphaFrom = (float) (from / 255) + 0.5f;
            return this;
        }

        public AlphaConfig to(int to) {
            alphaFrom = (float) (to / 255) + 0.5f;
            return this;
        }

        @Override
        public String toString() {
            return "AlphaConfig{" +
                    "alphaFrom=" + alphaFrom +
                    ", alphaTo=" + alphaTo +
                    '}';
        }

        @Override
        protected Animation buildAnimation(boolean isRevert) {
            AlphaAnimation animation = new AlphaAnimation(alphaFrom, alphaTo);
            deploy(animation);
            return animation;
        }

        @Override
        protected Animator buildAnimator(boolean isRevert) {
            Animator animator = ObjectAnimator.ofFloat(null, View.ALPHA, alphaFrom, alphaTo);
            deploy(animator);
            return animator;
        }
    }

    public static class ScaleConfig extends BaseConfig<ScaleConfig> {
        float scaleFromX = 1;
        float scaleFromY = 1;
        float scaleToX = 1;
        float scaleToY = 1;
        boolean relativeToParent;


        public ScaleConfig scale(float from, float to) {
            ignoreRevert = true;
            scaleFromX = scaleFromY = from;
            scaleToX = scaleToY = to;
            return this;
        }

        public ScaleConfig from(Direction... from) {
            if (from != null) {
                scaleFromX = scaleFromY = 0;
                for (Direction direction : from) {
                    pivotX += direction.scalePivotX;
                    pivotY += direction.scalePivotY;
                }
            }
            return this;
        }

        public ScaleConfig to(Direction... to) {
            if (to != null) {
                scaleToX = scaleToY = 1;
                for (Direction direction : to) {
                    pivotX2 += direction.scalePivotX;
                    pivotY2 += direction.scalePivotY;
                }
            }
            return this;
        }

        public ScaleConfig scaleX(float from, float to) {
            ignoreRevert = true;
            scaleFromX = from;
            scaleToX = to;
            return this;
        }

        public ScaleConfig sclaeY(float from, float to) {
            ignoreRevert = true;
            scaleFromY = from;
            scaleToY = to;
            return this;
        }


        public ScaleConfig relativeToParent() {
            relativeToParent = true;
            return this;
        }

        public ScaleConfig relativeToSelf() {
            relativeToParent = false;
            return this;
        }

        @Override
        public String toString() {
            return "ScaleConfig{" +
                    "scaleFromX=" + scaleFromX +
                    ", scaleFromY=" + scaleFromY +
                    ", scaleToX=" + scaleToX +
                    ", scaleToY=" + scaleToY +
                    ", relativeToParent=" + relativeToParent +
                    '}';
        }

        /**
         * 0 = fromx
         * 1 = tox
         * 2 = fromy
         * 3 = toy
         * 4 = pivotx
         * 5 = pivoty
         */
        float[] values(boolean isRevert) {
            float[] result = new float[6];
            boolean exchange = !ignoreRevert && isRevert;
            result[0] = exchange ? scaleToX : scaleFromX;
            result[1] = exchange ? scaleFromX : scaleToX;
            result[2] = exchange ? scaleToY : scaleFromY;
            result[3] = exchange ? scaleFromY : scaleToY;
            result[4] = exchange ? pivotX2 : pivotX;
            result[5] = exchange ? pivotY2 : pivotY;
            return result;
        }

        @Override
        protected Animation buildAnimation(boolean isRevert) {
            float[] values = values(isRevert);
            Animation animation = new ScaleAnimation(values[0], values[1], values[2], values[3],
                                                     relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF, values[4],
                                                     relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF, values[5]);
            deploy(animation);
            return animation;
        }

        @Override
        protected Animator buildAnimator(boolean isRevert) {
            final float[] values = values(isRevert);
            AnimatorSet animatorSet = new AnimatorSet();
            final Animator scaleX = ObjectAnimator.ofFloat(null, View.SCALE_X, values[0], values[1]);
            final Animator scaleY = ObjectAnimator.ofFloat(null, View.SCALE_Y, values[2], values[3]);
            scaleX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Object target = ((ObjectAnimator) animation).getTarget();
                    if (target instanceof View) {
                        ((View) target).setPivotX(((View) target).getWidth() * values[4]);
                    }
                }
            });
            scaleY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Object target = ((ObjectAnimator) animation).getTarget();
                    if (target instanceof View) {
                        ((View) target).setPivotY(((View) target).getHeight() * values[5]);
                    }
                }
            });
            animatorSet.playTogether(scaleX, scaleY);
            deploy(animatorSet);
            return animatorSet;
        }
    }

    public static class TranslationConfig extends BaseConfig<TranslationConfig> {
        float fromX, toX;
        float fromY, toY;
        float coordinateX, coordinateY;
        boolean relativeToParent;

        public TranslationConfig from(Direction... directions) {
            if (directions != null) {
                fromX = fromY = 0;
                for (Direction direction : directions) {
                    fromX += direction.transFromX;
                    fromY += direction.transFromY;
                }
            }
            return this;
        }

        public TranslationConfig to(Direction... directions) {
            if (directions != null) {
                toX = toY = 0;
                for (Direction direction : directions) {
                    toX += direction.transToX;
                    toY += direction.transToY;
                }
            }
            return this;
        }

        public TranslationConfig fromX(float fromX) {
            this.fromX = fromX;
            return this;
        }

        public TranslationConfig toX(float toX) {
            this.toX = toX;
            return this;
        }

        public TranslationConfig fromY(float fromY) {
            this.fromY = fromY;
            return this;
        }

        public TranslationConfig toY(float toY) {
            this.toY = toY;
            return this;
        }

        public TranslationConfig coordinateX(float x) {
            this.coordinateX = x;
            return this;
        }

        public TranslationConfig coordinateY(float y) {
            this.coordinateY = y;
            return this;
        }

        public TranslationConfig relativeToParent() {
            relativeToParent = true;
            return this;
        }

        public TranslationConfig relativeToSelf() {
            relativeToParent = false;
            return this;
        }

        @Override
        public String toString() {
            return "TranslationConfig{" +
                    "fromX=" + fromX +
                    ", toX=" + toX +
                    ", fromY=" + fromY +
                    ", toY=" + toY +
                    ", coordinateX=" + coordinateX +
                    ", coordinateY=" + coordinateY +
                    ", relativeToParent=" + relativeToParent +
                    '}';
        }

        @Override
        protected Animation buildAnimation(boolean isRevert) {
            Animation animation = new TranslateAnimation(relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF,
                                                         fromX,
                                                         relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF,
                                                         toX,
                                                         relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF,
                                                         fromY,
                                                         relativeToParent ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF,
                                                         toY);
            deploy(animation);
            return animation;
        }

        @Override
        protected Animator buildAnimator(boolean isRevert) {
            AnimatorSet animatorSet = new AnimatorSet();
            Animator scaleX = ObjectAnimator.ofFloat(null, View.TRANSLATION_X, fromX, toX);
            Animator scaleY = ObjectAnimator.ofFloat(null, View.TRANSLATION_Y, fromY, toY);
            animatorSet.playTogether(scaleX, scaleY);
            deploy(animatorSet);
            return animatorSet;
        }
    }

    public static class AnimationBuilder extends AnimationApi<AnimationBuilder> {


        public Animation buildShown() {
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (BaseConfig config : configs) {
                    set.addAnimation(config.$buildAnimation(false));
                }
            }
            return set;
        }


        public Animation buildDismiss() {
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (BaseConfig config : configs) {
                    set.addAnimation(config.$buildAnimation(true));
                }
            }
            return set;
        }
    }

    public static class AnimatorBuilder extends AnimationApi<AnimatorBuilder> {

        public Animator buildShow() {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (BaseConfig config : configs) {
                    set.playTogether(config.$buildAnimator(false));
                }
            }
            return set;
        }

        public Animator buildDismiss() {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (BaseConfig config : configs) {
                    set.playTogether(config.$buildAnimator(true));
                }
            }
            return set;
        }
    }


}
