package is.hi.hbv601g.matbjorg_app.ui.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hér kemur valmynd");
    }

    public LiveData<String> getText() {
        return mText;
    }
}