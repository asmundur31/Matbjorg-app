package is.hi.hbv601g.matbjorg_app.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hér kemur heimasvæði notanda");
    }

    public LiveData<String> getText() {
        return mText;
    }
}