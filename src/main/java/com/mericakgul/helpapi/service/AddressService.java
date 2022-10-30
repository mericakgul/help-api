package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    private final DtoMapper dtoMapper;

    public List<AddressDto> saveAll(List<AddressDto> addressesRequest){
        List<Address> newAddresses = this.dtoMapper.mapListModel(addressesRequest, Address.class);
        List<Address> savedAddresses = this.addressRepository.saveAll(newAddresses);
        return this.dtoMapper.mapListModel(savedAddresses, AddressDto.class);
    }
}
