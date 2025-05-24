package com.github.julionaponucena.financedesktop.modules.registers.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.registers.data.outs.CreateRegisterOUT;

public interface CreateRegisterObserver {
    void notifyCreation(CreateRegisterOUT registerOUT);
}
