package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.commons.exceptions.InternalServerException;
import com.github.julionaponucena.financedesktop.models.BlockRegister;
import com.github.julionaponucena.financedesktop.models.Category;
import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.category.repository.CategoryRepository;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CreateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.CreateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.ListCategoryRelOUT;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CreateRegisterService {
    private final RegisterRepository repository;
    private final CategoryRepository categoryRepository;

    public CreateRegisterOUT execute(CreateRegisterInput input) throws InternalServerException {
        List<Category> categories = input.categoriesId().stream().map(integer -> new Category(integer,null)).toList();

        BlockRegister blockRegister = new BlockRegister(input.idBlock());

        Register register = new Register(input.name(),input.date(),categories,input.value(),blockRegister);

        this.repository.create(register);

        this.repository.addCategories(input.categoriesId(),register.getId());

        List<Category> categories1 = this.categoryRepository.findAll(input.categoriesId());

        List<ListCategoryRelOUT> categoryOUTS = categories1.stream()
                .map(category -> new ListCategoryRelOUT(category.getId(),category.getName())).toList();

        return new CreateRegisterOUT(register.getId(), register.getTitle(),register.getDate() ,categoryOUTS,register.getValue());
    }
}
