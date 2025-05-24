package com.github.julionaponucena.financedesktop.modules.blockregister.services.converters;

import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class BlockRegisterConverter {
    public ListBlockRegisterOUTFX convert(CreateBlockRegisterOUT blockRegisterOUT) {
        return new ListBlockRegisterOUTFX(blockRegisterOUT.id(),new SimpleStringProperty(blockRegisterOUT.title()), BigDecimal.ZERO,false);
    }

    public ListBlockRegisterOUTFX convert(BlockRegisterProj proj) {
        return new ListBlockRegisterOUTFX(proj.id(),new SimpleStringProperty(proj.title()), proj.value(),proj.containRegister());
    }
}
