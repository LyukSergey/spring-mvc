package com.lss.l9springDataJpaCustom.holder;

import java.util.HashSet;
import java.util.Set;

public class MyJpaMetadataHolder {

    private static Set<String> entityClassNames = new HashSet<>();

    public static void setEntityClassNames(Set<String> names) {
        entityClassNames = names;
    }

    public static Set<String> getEntityClassNames() {
        return entityClassNames;
    }
}
