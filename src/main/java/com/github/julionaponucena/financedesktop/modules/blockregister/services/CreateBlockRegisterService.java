package com.github.julionaponucena.financedesktop.modules.blockregister.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.BlockRegister;
import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.blockregister.data.outs.CreateBlockRegisterOUT;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateBlockRegisterService {
    private final BlockRegisterRepository blockRegisterRepository;

    public CreateBlockRegisterOUT execute(String title)throws InternalServerException {
        BlockRegister blockRegister=this.blockRegisterRepository.create(title);

        return new CreateBlockRegisterOUT(blockRegister.getId(), blockRegister.getTitle());
    }
}
