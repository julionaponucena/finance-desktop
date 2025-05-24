package com.github.julionaponucena.financedesktop.modules.blockregister.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;

public interface CreateBlockRegisterObserver {
    void notifyCreation(CreateBlockRegisterOUT createBlockRegisterOUT);
}
