package fr.eseoye.eseoye.beans;

import fr.eseoye.eseoye.io.objects.FetchPostFilter.FetchOrderEnum;

public class FetchOrder {

    private final String displayName;
    private final FetchOrderEnum value;

    public FetchOrder(String displayName, FetchOrderEnum value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public FetchOrderEnum getValue() {
        return value;
    }
}
