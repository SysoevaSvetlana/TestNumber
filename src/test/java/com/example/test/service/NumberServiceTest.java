package com.example.test.service;

import com.example.test.entity.NumberEntity;
import com.example.test.repository.NumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NumberServiceTest {

    @Mock
    private NumberRepository numberRepository;

    private NumberService numberService;

    @BeforeEach
    void setUp() {
        numberService = new NumberService(numberRepository);
    }

    @Test
    void addNumberAndGetSorted_shouldSaveNumberAndReturnSortedList() {
        // Given
        Integer numberToAdd = 3;
        List<Integer> expectedSortedList = Arrays.asList(1, 2, 3);
        
        when(numberRepository.findAllValuesSorted()).thenReturn(expectedSortedList);
        
        // When
        List<Integer> result = numberService.addNumberAndGetSorted(numberToAdd);
        
        // Then
        ArgumentCaptor<NumberEntity> entityCaptor = ArgumentCaptor.forClass(NumberEntity.class);
        verify(numberRepository).save(entityCaptor.capture());
        
        NumberEntity savedEntity = entityCaptor.getValue();
        assertEquals(numberToAdd, savedEntity.getValue());
        
        verify(numberRepository).findAllValuesSorted();
        assertEquals(expectedSortedList, result);
    }

    @Test
    void addNumberAndGetSorted_shouldReturnSingleElementList_whenFirstNumber() {
        // Given
        Integer numberToAdd = 5;
        List<Integer> expectedSortedList = Arrays.asList(5);
        
        when(numberRepository.findAllValuesSorted()).thenReturn(expectedSortedList);
        
        // When
        List<Integer> result = numberService.addNumberAndGetSorted(numberToAdd);
        
        // Then
        verify(numberRepository).save(any(NumberEntity.class));
        verify(numberRepository).findAllValuesSorted();
        assertEquals(expectedSortedList, result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0));
    }

    @Test
    void addNumberAndGetSorted_shouldMaintainSortOrder() {
        // Given
        Integer numberToAdd = 2;
        List<Integer> expectedSortedList = Arrays.asList(1, 2, 3, 4);
        
        when(numberRepository.findAllValuesSorted()).thenReturn(expectedSortedList);
        
        // When
        List<Integer> result = numberService.addNumberAndGetSorted(numberToAdd);
        
        // Then
        verify(numberRepository).save(any(NumberEntity.class));
        assertEquals(expectedSortedList, result);
        
        // Verify list is sorted
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }
}

