package is.hi.hbv601g.matbjorg_app.models;

import java.util.Arrays;

public enum Tag {
    GRÆNMETI,
    MJÓLKURVARA,
    ÞURRVARA,
    BRAUÐ,
    BAKKELSI,
    ÁVEXTIR,
    DRYKKJARVARA,
    VEITINGASTAÐUR,
    SÆLGÆTI,
    HEILSUVARA,
    KJÖT,
    BAUNIR,
    VEGAN,
    GRÆNMETISFÆÐI;

    public static String[] getTags() {
        Tag[] tags = values();
        String[] names = new String[tags.length];
        for (int i = 0; i < tags.length; i++) {
            names[i] = tags[i].name();
        }
        return names;
    }
}
