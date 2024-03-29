package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.databinding.ActivityIssue226Binding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.PopupInput;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/26
 * <p>
 * Description：https://github.com/razerdp/BasePopup/issues/226
 */
public class Issue226TestActivity extends BaseBindingActivity<ActivityIssue226Binding> {

    PopupInput popupInput;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue226Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue226Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.tvShow.setOnClickListener(v -> show());
    }

    void show() {
        if (popupInput == null) {
            popupInput = new PopupInput(this);
        }
        int flag = BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT | BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN;

        if (mBinding.checkForceAdjust.isChecked()) {
            flag |= BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST;
        }
        popupInput.setKeyboardAdaptive(true)
                .setKeyboardAdaptionMode(flag)
                .showPopupWindow();
    }

}
