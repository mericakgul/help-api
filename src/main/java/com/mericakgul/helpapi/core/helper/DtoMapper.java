package com.mericakgul.helpapi.core.helper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoMapper {
    private final ModelMapper modelMapper;

    public <T, S> T mapModel(S source, Class<T> target){
        return this.modelMapper.map(source, target);
    }

    public<S, T> List<T> mapListModel(List<S> source, Class<T> target){
        List<T> mappedList = source
                .stream()
                .map(element -> this.mapModel(element, target))
                .collect(Collectors.toList());
        return mappedList;
    }
}
