package com.example.test.service;

import com.example.test.entity.NumberEntity;
import com.example.test.repository.NumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NumberService {
    
    private final NumberRepository numberRepository;
    
    @Transactional
    public List<Integer> addNumberAndGetSorted(Integer number) {
        // Сохраняем число в БД
        NumberEntity entity = new NumberEntity();
        entity.setValue(number);
        numberRepository.save(entity);
        
        // Возвращаем отсортированный список всех чисел
        return numberRepository.findAllValuesSorted();
    }
}

