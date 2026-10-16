package com.cheonsa.liriope;

import com.cheonsa.liriope.api.LiriopeAPI;
import org.hanayori.aster.component.Inject;
import org.hanayori.aster.plugin.Plugin;

public final class Liriope extends Plugin {

    @Inject
    private LiriopeAPIImpl api;

    @Override
    public void onPluginEnable() {
        LiriopeAPI.setApi(this.api);
    }

    @Override
    public void onPluginDisable() {
        LiriopeAPI.setApi(null);
    }
}
