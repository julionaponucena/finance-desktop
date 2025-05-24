package com.github.julionaponucena.financedesktop.modules.registers.controllers.observers;

import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;

public interface UpdateRegisterObserver {
    void onUpdateRegister(UpdateRegisterOUT registerOUT);
}
