package com.zt.zeus.transfer.custom;

import com.zt.zeus.transfer.enums.Carrier;
import com.zt.zeus.transfer.enums.SearchModel;
import com.zt.zeus.transfer.enums.StorageMode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RichParameters {

    private final StorageMode storageMode;

    private final SearchModel searchModel;

    private final String fromType;

    private final List<Carrier> carrier;
}
