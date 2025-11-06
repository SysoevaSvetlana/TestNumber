package com.example.test.service;

import com.example.test.entity.NumberEntity;
import com.example.test.repository.NumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberService {
    
    private final NumberRepository numberRepository;
    
    @Transactional
    public List<Integer> addNumberAndGetSorted(Integer number) {
        log.info("Adding number: {}", number);

        // Сохраняем число в БД
        NumberEntity entity = new NumberEntity();
        entity.setValue(number);
        numberRepository.save(entity);

        // Возвращаем отсортированный список всех чисел
        List<Integer> result = numberRepository.findAllValuesSorted();
        log.info("Returning {} sorted numbers", result.size());

        return result;
    }
}

