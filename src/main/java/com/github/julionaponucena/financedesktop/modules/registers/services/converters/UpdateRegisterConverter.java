package com.github.julionaponucena.financedesktop.modules.registers.services.converters;

import com.github.julionaponucena.financedesktop.models.Register;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.CategoryPersistenceInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.UpdateRegisterInput;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.enums.CategoryPersistenceState;
import com.github.julionaponucena.financedesktop.modules.registers.data.outs.UpdateRegisterOUT;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceVO;
import com.github.julionaponucena.financedesktop.modules.registers.data.vos.CategoryPersistenceWrapperDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class UpdateRegisterConverter {
    public CategoryPersistenceWrapperDTO mapCategoryPersistence(List<CategoryPersistenceInput> categories) {
        Set<Integer> deleteIds = new HashSet<>();
        Set<Integer> insertIds = new HashSet<>();

        for (CategoryPersistenceInput categoryPersistenceInput: categories){
            if(categoryPersistenceInput.persistenceState()== CategoryPersistenceState.CREATED){
                insertIds.add(categoryPersistenceInput.id());
            } else if (categoryPersistenceInput.persistenceState() == CategoryPersistenceState.DELETED) {
                deleteIds.add(categoryPersistenceInput.id());
            }
        }

        return new CategoryPersistenceWrapperDTO(insertIds, deleteIds);
    }

    public UpdateRegisterOUT convertToOUT(Register register, List<CategoryPersistenceInput> categories) {
        List<CategoryPersistenceVO> categoryPersistenceVOS =categories.stream()
                .flatMap(categoryPersistenceInput -> categoryPersistenceInput.currentState()
                != CategoryPersistenceState.DELETED ?
                Stream.of(new CategoryPersistenceVO(categoryPersistenceInput.id(),categoryPersistenceInput.name()))
                : Stream.empty()).toList();

        return new UpdateRegisterOUT(register.getId(), register.getTitle(),register.getDate(),categoryPersistenceVOS,register.getValue());
    }

    public Register convertInputToEntity(UpdateRegisterInput input) {
        return new Register(input.id(),input.title(),input.date(),input.value());
    }
}
