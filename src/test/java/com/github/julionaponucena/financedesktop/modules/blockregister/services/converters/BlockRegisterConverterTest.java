package com.github.julionaponucena.financedesktop.modules.blockregister.services.converters;

import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.ListBlockRegisterOUTFX;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.projs.BlockRegisterProj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BlockRegisterConverterTest {
    private final BlockRegisterConverter converter = new BlockRegisterConverter();

    @Test
    void convertFromCreateBlockRegisterOUT() {
        CreateBlockRegisterOUT blockRegisterOUT = new CreateBlockRegisterOUT(1,"teste");

        ListBlockRegisterOUTFX blockRegisterOUTFX = this.converter.convert(blockRegisterOUT);

        Assertions.assertEquals(blockRegisterOUT.id(),blockRegisterOUTFX.id());
        Assertions.assertEquals(blockRegisterOUT.title(),blockRegisterOUTFX.title().getValue());
        Assertions.assertEquals(BigDecimal.ZERO,blockRegisterOUTFX.value());
    }

    @Test
    void convertFromBlockRegisterProj() {
        BlockRegisterProj proj = new BlockRegisterProj(1,"teste",new BigDecimal(100),false);

        ListBlockRegisterOUTFX blockRegisterOUTFX = this.converter.convert(proj);

        Assertions.assertEquals(proj.id(),blockRegisterOUTFX.id());
        Assertions.assertEquals(proj.title(),blockRegisterOUTFX.title().getValue());
        Assertions.assertEquals(proj.value(),blockRegisterOUTFX.value());
        Assertions.assertEquals(proj.containRegister(),blockRegisterOUTFX.containRegister());
    }
}