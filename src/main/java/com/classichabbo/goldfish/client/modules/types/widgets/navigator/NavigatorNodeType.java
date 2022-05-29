package com.classichabbo.goldfish.client.modules.types.widgets.navigator;

import java.util.ArrayList;
import java.util.List;

public enum NavigatorNodeType {
    CATEGORY(0),
    PUBLIC_ROOM(1),
    PRIVATE_ROOM(2);

    private final int typeId;

    NavigatorNodeType(int typeId) {
        this.typeId = typeId;
    }

    public static NavigatorNodeType getTypeById(int id) {
        for (NavigatorNodeType type : values()) {
            if (type.typeId == id) {
                return type;
            }
        }

        return null;
    }

    public int getTypeId() {
        return typeId;
    }
}
