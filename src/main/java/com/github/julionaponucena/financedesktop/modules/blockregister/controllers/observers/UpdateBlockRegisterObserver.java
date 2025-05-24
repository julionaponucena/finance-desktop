package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.UpdateBlockRegisterOUT;

public interface UpdateBlockRegisterObserver {
    void notifyUpdate(UpdateBlockRegisterOUT updateBlockRegisterOUT);
}
